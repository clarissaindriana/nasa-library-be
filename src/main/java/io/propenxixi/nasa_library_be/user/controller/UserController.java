package io.propenxixi.nasa_library_be.user.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.propenxixi.nasa_library_be.common.dto.response.BaseResponseDTO;
import io.propenxixi.nasa_library_be.user.dto.request.AddUserRequestDTO;
import io.propenxixi.nasa_library_be.user.dto.request.UpdateUserRequestDTO;
import io.propenxixi.nasa_library_be.user.dto.response.UserResponseDTO;
import io.propenxixi.nasa_library_be.user.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    public static final String BASE_URL = "/user";
    public static final String VIEW_ALL_USERS = BASE_URL + "/all";
    public static final String VIEW_USER = BASE_URL + "/{nis}";
    public static final String CREATE_USER = BASE_URL + "/create";
    public static final String UPDATE_USER = BASE_URL + "/update";
    public static final String DELETE_USER = BASE_URL + "/{nis}/delete";
    public static final String ACTIVATE_USER = BASE_URL + "/{nis}/activate";
    public static final String SEARCH_USER_BY_NAMA = BASE_URL + "/search/nama";
    public static final String SEARCH_USER_BY_KELAS = BASE_URL + "/search/kelas";
    public static final String IMPORT_USERS_EXCEL = BASE_URL + "/import/excel";
    
    @GetMapping({VIEW_ALL_USERS, "/users/all"})
    public ResponseEntity<BaseResponseDTO<List<UserResponseDTO>>> getAllUsers(
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String search) {
        
        var baseResponseDTO = new BaseResponseDTO<List<UserResponseDTO>>();
        
        try {
            List<UserResponseDTO> users = userService.getAllUsers(isDeleted, search);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(users);
            baseResponseDTO.setMessage("Data Siswa Berhasil Ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(VIEW_USER)
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> getUser(
            @PathVariable String nis) {
        
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        
        try {
            UserResponseDTO user = userService.getUser(nis);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(user);
            baseResponseDTO.setMessage("Data Siswa Berhasil Ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping({CREATE_USER, "/users/create"})
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> createUser(
            @RequestBody AddUserRequestDTO dto) {
        
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        
        try {
            UserResponseDTO createdUser = userService.createUser(dto);
            
            baseResponseDTO.setStatus(HttpStatus.CREATED.value());
            baseResponseDTO.setData(createdUser);
            baseResponseDTO.setMessage("Data Siswa Berhasil Ditambahkan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping({UPDATE_USER, "/users/update"})
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> updateUser(
            @RequestBody UpdateUserRequestDTO dto) {
        
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        
        try {
            UserResponseDTO updatedUser = userService.updateUser(dto);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(updatedUser);
            baseResponseDTO.setMessage("Data Siswa Berhasil Diperbarui");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping({DELETE_USER, "/users/{nis}/delete"})
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> deleteUser(
            @PathVariable String nis) {
        
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        
        try {
            UserResponseDTO deletedUser = userService.deleteUser(nis);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(deletedUser);
            baseResponseDTO.setMessage("Data Siswa Berhasil Dihapus");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping({ACTIVATE_USER, "/users/{nis}/activate"})
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> activateUser(
            @PathVariable String nis) {
        
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        
        try {
            UserResponseDTO activatedUser = userService.activateUser(nis);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(activatedUser);
            baseResponseDTO.setMessage("Data Siswa Berhasil Diaktifkan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(SEARCH_USER_BY_NAMA)
    public ResponseEntity<BaseResponseDTO<List<UserResponseDTO>>> getUsersByNama(
            @RequestParam String nama) {
        
        var baseResponseDTO = new BaseResponseDTO<List<UserResponseDTO>>();
        
        try {
            List<UserResponseDTO> users = userService.getUsersByNama(nama);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(users);
            baseResponseDTO.setMessage("Data Siswa Berhasil Ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(SEARCH_USER_BY_KELAS)
    public ResponseEntity<BaseResponseDTO<List<UserResponseDTO>>> getUsersByKelas(
            @RequestParam String kelas) {
        
        var baseResponseDTO = new BaseResponseDTO<List<UserResponseDTO>>();
        
        try {
            List<UserResponseDTO> users = userService.getUsersByKelas(kelas);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(users);
            baseResponseDTO.setMessage("Data Siswa Berhasil Ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(IMPORT_USERS_EXCEL)
    public ResponseEntity<BaseResponseDTO<List<UserResponseDTO>>> importUsersFromExcel(
            @RequestParam("file") MultipartFile file) {
        
        var baseResponseDTO = new BaseResponseDTO<List<UserResponseDTO>>();
        
        try {
            List<UserResponseDTO> importedUsers = userService.importUsersFromExcel(file);
            
            baseResponseDTO.setStatus(HttpStatus.CREATED.value());
            baseResponseDTO.setData(importedUsers);
            baseResponseDTO.setMessage("Data Siswa Berhasil Diimport dari Excel");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan pada server: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
