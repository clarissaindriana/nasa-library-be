package io.propenxixi.nasa_library_be.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {
    
    private String nis;
    
    private String nama;
    
    private String jenisKelamin;
    
    private String kelas;
}
