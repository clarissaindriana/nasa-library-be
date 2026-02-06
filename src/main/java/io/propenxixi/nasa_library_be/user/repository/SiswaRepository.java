package io.propenxixi.nasa_library_be.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.propenxixi.nasa_library_be.user.model.Siswa;

public interface SiswaRepository extends JpaRepository<Siswa, String> {
    
    List<Siswa> findByNamaContainingIgnoreCase(String nama);
    
    List<Siswa> findByKelas(String kelas);
    
    List<Siswa> findByJenisKelamin(String jenisKelamin);
    
    List<Siswa> findByIsDeleted(Boolean isDeleted);
    
    @Query("SELECT s FROM Siswa s WHERE s.nis = :nis AND s.isDeleted = false")
    Optional<Siswa> findActiveByNis(@Param("nis") String nis);
    
    @Query("SELECT s FROM Siswa s WHERE s.isDeleted = false")
    List<Siswa> findAllActive();
    
    boolean existsByNisAndIsDeleted(String nis, Boolean isDeleted);
}
