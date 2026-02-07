package io.propenxixi.nasa_library_be.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequestDTO {
    
    private Long id;
    
    private String title;
    
    private String author;
    
    private String publishedDate;
    
    private Integer length;
    
    private String language;
    
    private String description;
    
    private String imageUrl;
    
    private String shelfLocation;
    
    private Integer totalCopies;
    
    private Integer availableCopies;
    
    private String condition;
}
