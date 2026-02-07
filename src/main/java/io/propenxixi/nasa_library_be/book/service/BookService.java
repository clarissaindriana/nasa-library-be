package io.propenxixi.nasa_library_be.book.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.propenxixi.nasa_library_be.book.dto.request.AddBookRequestDTO;
import io.propenxixi.nasa_library_be.book.dto.request.UpdateBookRequestDTO;
import io.propenxixi.nasa_library_be.book.dto.response.BookResponseDTO;

public interface BookService {
    
    BookResponseDTO createBook(AddBookRequestDTO dto);
    
    List<BookResponseDTO> getAllBooks(Boolean isDeleted, String search);
    
    List<BookResponseDTO> getBooksByTitle(String title);
    
    List<BookResponseDTO> getBooksByAuthor(String author);
    
    List<BookResponseDTO> getBooksByShelfLocation(String shelfLocation);
    
    List<BookResponseDTO> getBooksByCondition(String condition);
    
    BookResponseDTO getBook(Long id);
    
    BookResponseDTO updateBook(UpdateBookRequestDTO dto);
    
    BookResponseDTO deleteBook(Long id);
    
    BookResponseDTO activateBook(Long id);
    
    List<BookResponseDTO> importBooksFromExcel(InputStream fileInputStream) throws IOException;
}
