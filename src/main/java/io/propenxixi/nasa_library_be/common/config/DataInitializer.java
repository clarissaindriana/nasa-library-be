package io.propenxixi.nasa_library_be.common.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.propenxixi.nasa_library_be.user.dto.response.UserResponseDTO;
import io.propenxixi.nasa_library_be.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataInitializer implements ApplicationRunner {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("Starting automatic data initialization from Excel file...");
            importExcelData();
            log.info("Data initialization completed successfully!");
        } catch (Exception ex) {
            log.error("Error during data initialization: " + ex.getMessage(), ex);
        }
    }
    
    private void importExcelData() throws IOException {
        try {
            // Load the Excel file from resources/static folder
            Resource resource = new ClassPathResource("static/Daftar-Siswa-Cleaned.xlsx");
            
            if (!resource.exists()) {
                log.warn("Excel file not found in static folder. Skipping data initialization.");
                return;
            }
            
            // Create a custom MultipartFile from the resource
            byte[] fileContent = Files.readAllBytes(Paths.get(resource.getURI()));
            MultipartFile multipartFile = new MultipartFile() {
                @Override
                public String getName() {
                    return "file";
                }
                
                @Override
                public String getOriginalFilename() {
                    return "Daftar-Siswa-Cleaned.xlsx";
                }
                
                @Override
                public String getContentType() {
                    return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                }
                
                @Override
                public boolean isEmpty() {
                    return fileContent.length == 0;
                }
                
                @Override
                public long getSize() {
                    return fileContent.length;
                }
                
                @Override
                public byte[] getBytes() throws IOException {
                    return fileContent;
                }
                
                @Override
                public java.io.InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(fileContent);
                }
                
                @Override
                public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                    Files.write(dest.toPath(), fileContent);
                }
                
                @Override
                public void transferTo(Path dest) throws IOException, IllegalStateException {
                    Files.write(dest, fileContent);
                }
            };
            
            // Import the data
            List<UserResponseDTO> importedUsers = userService.importUsersFromExcel(multipartFile);
            
            log.info("Successfully imported " + importedUsers.size() + " users from Excel file");
            
            if (importedUsers.isEmpty()) {
                log.warn("No new users were imported. All users in the Excel file may already exist in the database.");
            }
            
        } catch (IOException ex) {
            log.error("Error reading Excel file: " + ex.getMessage(), ex);
            throw ex;
        }
    }
}

