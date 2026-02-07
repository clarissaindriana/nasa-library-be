package io.propenxixi.nasa_library_be.book.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    
    private Long id;
    
    private String title;
    
    private String author;
    
    private String isbnCode;
    
    private String publishedDate;
    
    private Integer length;
    
    private String language;
    
    private String description;
    
    private String imageUrl;
    
    private String shelfLocation;
    
    private Integer totalCopies;
    
    private Integer availableCopies;
    
    private String condition;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private LocalDateTime updatedAt;
    
    private Boolean isDeleted;
}
