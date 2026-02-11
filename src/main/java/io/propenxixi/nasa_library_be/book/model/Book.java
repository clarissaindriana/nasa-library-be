package io.propenxixi.nasa_library_be.book.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "book")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "author", nullable = false)
    private String author;
    
    @Column(name = "isbn_code", unique = true, nullable = false)
    private String isbnCode;

    @Column(name = "length")
    private Integer length;
    
    @Column(name = "language")
    private String language;

    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "shelf_location", nullable = false)
    private String shelfLocation;
    
    @Column(name = "total_copies", nullable = false)
    @Builder.Default
    private Integer totalCopies = 0;
    
    @Column(name = "available_copies", nullable = false)
    @Builder.Default
    private Integer availableCopies = 0;
    
    @Column(name = "condition", nullable = false)
    private String condition;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
