package io.propenxixi.nasa_library_be.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.propenxixi.nasa_library_be.user.dto.request.AddUserRequestDTO;
import io.propenxixi.nasa_library_be.user.dto.request.UpdateUserRequestDTO;
import io.propenxixi.nasa_library_be.user.dto.response.UserResponseDTO;
import io.propenxixi.nasa_library_be.user.model.Siswa;
import io.propenxixi.nasa_library_be.user.repository.SiswaRepository;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private SiswaRepository siswaRepository;
    
    @Override
    public UserResponseDTO createUser(AddUserRequestDTO dto) {
        if (siswaRepository.existsById(dto.getNis())) {
            throw new IllegalArgumentException("Siswa dengan NIS " + dto.getNis() + " sudah terdaftar");
        }
        
        Siswa siswa = Siswa.builder()
                .nis(dto.getNis())
                .nama(dto.getNama())
                .jenisKelamin(dto.getJenisKelamin())
                .kelas(dto.getKelas())
                .build();
        
        Siswa savedSiswa = siswaRepository.save(siswa);
        siswaRepository.flush();
        savedSiswa = siswaRepository.findById(savedSiswa.getNis()).orElse(savedSiswa);
        
        return convertToUserResponseDTO(savedSiswa);
    }
    
    @Override
    public List<UserResponseDTO> getAllUsers(Boolean isDeleted, String search) {
        List<Siswa> allSiswa = siswaRepository.findAll();
        List<UserResponseDTO> users = allSiswa.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
        
        // Filter by isDeleted if provided
        if (isDeleted != null) {
            users = users.stream()
                    .filter(user -> user.getIsDeleted() == isDeleted)
                    .toList();
        }
        
        // Filter by search (nis or nama) - case-insensitive
        if (search != null && !search.trim().isEmpty()) {
            String q = search.toLowerCase().trim();
            users = users.stream()
                    .filter(user ->
                            (user.getNis() != null && user.getNis().toLowerCase().contains(q)) ||
                            (user.getNama() != null && user.getNama().toLowerCase().contains(q))
                    )
                    .toList();
        }
        
        // Sort by NIS ascending
        users = users.stream()
                .sorted((u1, u2) -> u1.getNis().compareTo(u2.getNis()))
                .toList();
        
        return users;
    }
    
    @Override
    public List<UserResponseDTO> getUsersByNama(String nama) {
        List<Siswa> siswaList = siswaRepository.findByNamaContainingIgnoreCase(nama);
        return siswaList.stream()
                .map(this::convertToUserResponseDTO)
                .sorted((u1, u2) -> u1.getNis().compareTo(u2.getNis()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserResponseDTO> getUsersByKelas(String kelas) {
        List<Siswa> siswaList = siswaRepository.findByKelas(kelas);
        return siswaList.stream()
                .map(this::convertToUserResponseDTO)
                .sorted((u1, u2) -> u1.getNis().compareTo(u2.getNis()))
                .collect(Collectors.toList());
    }
    
    @Override
    public UserResponseDTO getUser(String nis) {
        Optional<Siswa> siswa = siswaRepository.findActiveByNis(nis);
        if (siswa.isEmpty()) {
            throw new IllegalArgumentException("Siswa dengan NIS " + nis + " tidak ditemukan");
        }
        return convertToUserResponseDTO(siswa.get());
    }
    
    @Override
    public UserResponseDTO updateUser(UpdateUserRequestDTO dto) {
        Optional<Siswa> siswa = siswaRepository.findById(dto.getNis());
        if (siswa.isEmpty()) {
            throw new IllegalArgumentException("Siswa dengan NIS " + dto.getNis() + " tidak ditemukan");
        }
        
        Siswa existingSiswa = siswa.get();
        existingSiswa.setNama(dto.getNama());
        existingSiswa.setJenisKelamin(dto.getJenisKelamin());
        existingSiswa.setKelas(dto.getKelas());
        
        Siswa updatedSiswa = siswaRepository.save(existingSiswa);
        siswaRepository.flush();
        updatedSiswa = siswaRepository.findById(updatedSiswa.getNis()).orElse(updatedSiswa);
        
        return convertToUserResponseDTO(updatedSiswa);
    }
    
    @Override
    public UserResponseDTO deleteUser(String nis) {
        Optional<Siswa> siswa = siswaRepository.findById(nis);
        if (siswa.isEmpty()) {
            throw new IllegalArgumentException("Siswa dengan NIS " + nis + " tidak ditemukan");
        }
        
        Siswa existingSiswa = siswa.get();
        existingSiswa.setIsDeleted(true);
        
        Siswa deletedSiswa = siswaRepository.save(existingSiswa);
        siswaRepository.flush();
        
        return convertToUserResponseDTO(deletedSiswa);
    }
    
    @Override
    public UserResponseDTO activateUser(String nis) {
        Optional<Siswa> siswa = siswaRepository.findById(nis);
        if (siswa.isEmpty()) {
            throw new IllegalArgumentException("Siswa dengan NIS " + nis + " tidak ditemukan");
        }
        
        Siswa existingSiswa = siswa.get();
        existingSiswa.setIsDeleted(false);
        
        Siswa activatedSiswa = siswaRepository.save(existingSiswa);
        siswaRepository.flush();
        
        return convertToUserResponseDTO(activatedSiswa);
    }
    
    @Override
    public List<UserResponseDTO> importUsersFromExcel(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File tidak boleh kosong");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            throw new IllegalArgumentException("File harus berformat Excel (.xlsx atau .xls)");
        }
        
        List<UserResponseDTO> importedUsers = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    String nis = getCellValueAsString(row.getCell(0));
                    String nama = getCellValueAsString(row.getCell(1));
                    String jenisKelamin = getCellValueAsString(row.getCell(2));
                    String kelas = getCellValueAsString(row.getCell(3));
                    
                    if (nis == null || nis.trim().isEmpty()) {
                        continue;
                    }
                    
                    AddUserRequestDTO dto = new AddUserRequestDTO();
                    dto.setNis(nis.trim());
                    dto.setNama(nama != null ? nama.trim() : "");
                    dto.setJenisKelamin(jenisKelamin != null ? jenisKelamin.trim() : "");
                    dto.setKelas(kelas != null ? kelas.trim() : "");
                    
                    try {
                        UserResponseDTO createdUser = createUser(dto);
                        importedUsers.add(createdUser);
                    } catch (IllegalArgumentException e) {
                        // Skip if siswa already exists, continue with next row
                        continue;
                    }
                    
                } catch (Exception e) {
                    // Skip rows with errors
                    continue;
                }
            }
        }
        
        return importedUsers;
    }
    
    private String getCellValueAsString(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
    
    private UserResponseDTO convertToUserResponseDTO(Siswa siswa) {
        return new UserResponseDTO(
                siswa.getNis(),
                siswa.getNama(),
                siswa.getJenisKelamin(),
                siswa.getKelas(),
                siswa.getCreatedAt(),
                siswa.getUpdatedAt(),
                siswa.getIsDeleted()
        );
    }
}
