package io.propenxixi.nasa_library_be.user.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.propenxixi.nasa_library_be.user.dto.request.AddUserRequestDTO;
import io.propenxixi.nasa_library_be.user.dto.request.UpdateUserRequestDTO;
import io.propenxixi.nasa_library_be.user.dto.response.UserResponseDTO;

public interface UserService {
    
    UserResponseDTO createUser(AddUserRequestDTO dto);
    
    List<UserResponseDTO> getAllUsers(Boolean isDeleted, String search);
    
    List<UserResponseDTO> getUsersByNama(String nama);
    
    List<UserResponseDTO> getUsersByKelas(String kelas);
    
    UserResponseDTO getUser(String nis);
    
    UserResponseDTO updateUser(UpdateUserRequestDTO dto);
    
    UserResponseDTO deleteUser(String nis);
    
    UserResponseDTO activateUser(String nis);
    
    List<UserResponseDTO> importUsersFromExcel(MultipartFile file) throws IOException;
}
