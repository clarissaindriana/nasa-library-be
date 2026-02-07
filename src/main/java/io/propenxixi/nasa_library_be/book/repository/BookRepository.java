package io.propenxixi.nasa_library_be.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.propenxixi.nasa_library_be.book.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByIsbnCode(String isbnCode);
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    List<Book> findByShelfLocation(String shelfLocation);
    
    List<Book> findByCondition(String condition);
    
    List<Book> findByIsDeleted(Boolean isDeleted);
    
    @Query("SELECT b FROM Book b WHERE b.id = :id AND b.isDeleted = false")
    Optional<Book> findActiveById(@Param("id") Long id);
    
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false")
    List<Book> findAllActive();
    
    boolean existsByIsbnCodeAndIsDeleted(String isbnCode, Boolean isDeleted);
}
