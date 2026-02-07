package io.propenxixi.nasa_library_be.book.controller;

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

import io.propenxixi.nasa_library_be.book.dto.request.AddBookRequestDTO;
import io.propenxixi.nasa_library_be.book.dto.request.UpdateBookRequestDTO;
import io.propenxixi.nasa_library_be.book.dto.response.BookResponseDTO;
import io.propenxixi.nasa_library_be.book.service.BookService;
import io.propenxixi.nasa_library_be.common.dto.response.BaseResponseDTO;

@RestController
@RequestMapping("/api")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    public static final String BASE_URL = "/book";
    public static final String VIEW_ALL_BOOKS = BASE_URL + "/all";
    public static final String VIEW_BOOK = BASE_URL + "/{id}";
    public static final String CREATE_BOOK = BASE_URL + "/create";
    public static final String UPDATE_BOOK = BASE_URL + "/update";
    public static final String DELETE_BOOK = BASE_URL + "/{id}/delete";
    public static final String ACTIVATE_BOOK = BASE_URL + "/{id}/activate";
    public static final String SEARCH_BOOK_BY_TITLE = BASE_URL + "/search/title";
    public static final String SEARCH_BOOK_BY_AUTHOR = BASE_URL + "/search/author";
    public static final String SEARCH_BOOK_BY_SHELF = BASE_URL + "/search/shelf";
    public static final String SEARCH_BOOK_BY_CONDITION = BASE_URL + "/search/condition";
    
    @GetMapping({VIEW_ALL_BOOKS, "/books/all"})
    public ResponseEntity<BaseResponseDTO<List<BookResponseDTO>>> getAllBooks(
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String search) {
        
        var baseResponseDTO = new BaseResponseDTO<List<BookResponseDTO>>();
        
        try {
            List<BookResponseDTO> books = bookService.getAllBooks(isDeleted, search);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Data buku berhasil diambil");
            baseResponseDTO.setData(books);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal mengambil data buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @GetMapping(VIEW_BOOK)
    public ResponseEntity<BaseResponseDTO<BookResponseDTO>> getBook(
            @PathVariable Long id) {
        
        var baseResponseDTO = new BaseResponseDTO<BookResponseDTO>();
        
        try {
            BookResponseDTO book = bookService.getBook(id);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Data buku berhasil diambil");
            baseResponseDTO.setData(book);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(404);
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal mengambil data buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @PostMapping({CREATE_BOOK, "/books/create"})
    public ResponseEntity<BaseResponseDTO<BookResponseDTO>> createBook(
            @RequestBody AddBookRequestDTO dto) {
        
        var baseResponseDTO = new BaseResponseDTO<BookResponseDTO>();
        
        try {
            BookResponseDTO createdBook = bookService.createBook(dto);
            baseResponseDTO.setStatus(201);
            baseResponseDTO.setMessage("Buku berhasil ditambahkan");
            baseResponseDTO.setData(createdBook);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponseDTO);
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(400);
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal menambahkan buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @PutMapping({UPDATE_BOOK, "/books/update"})
    public ResponseEntity<BaseResponseDTO<BookResponseDTO>> updateBook(
            @RequestBody UpdateBookRequestDTO dto) {
        
        var baseResponseDTO = new BaseResponseDTO<BookResponseDTO>();
        
        try {
            BookResponseDTO updatedBook = bookService.updateBook(dto);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Buku berhasil diperbarui");
            baseResponseDTO.setData(updatedBook);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(404);
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal memperbarui buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @DeleteMapping({DELETE_BOOK, "/books/{id}/delete"})
    public ResponseEntity<BaseResponseDTO<BookResponseDTO>> deleteBook(
            @PathVariable Long id) {
        
        var baseResponseDTO = new BaseResponseDTO<BookResponseDTO>();
        
        try {
            BookResponseDTO deletedBook = bookService.deleteBook(id);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Buku berhasil dihapus");
            baseResponseDTO.setData(deletedBook);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(404);
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal menghapus buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @PutMapping({ACTIVATE_BOOK, "/books/{id}/activate"})
    public ResponseEntity<BaseResponseDTO<BookResponseDTO>> activateBook(
            @PathVariable Long id) {
        
        var baseResponseDTO = new BaseResponseDTO<BookResponseDTO>();
        
        try {
            BookResponseDTO activatedBook = bookService.activateBook(id);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Buku berhasil diaktifkan");
            baseResponseDTO.setData(activatedBook);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (IllegalArgumentException ex) {
            baseResponseDTO.setStatus(404);
            baseResponseDTO.setMessage(ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal mengaktifkan buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @GetMapping(SEARCH_BOOK_BY_TITLE)
    public ResponseEntity<BaseResponseDTO<List<BookResponseDTO>>> getBooksByTitle(
            @RequestParam String title) {
        
        var baseResponseDTO = new BaseResponseDTO<List<BookResponseDTO>>();
        
        try {
            List<BookResponseDTO> books = bookService.getBooksByTitle(title);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Data buku berhasil diambil");
            baseResponseDTO.setData(books);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal mengambil data buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @GetMapping(SEARCH_BOOK_BY_AUTHOR)
    public ResponseEntity<BaseResponseDTO<List<BookResponseDTO>>> getBooksByAuthor(
            @RequestParam String author) {
        
        var baseResponseDTO = new BaseResponseDTO<List<BookResponseDTO>>();
        
        try {
            List<BookResponseDTO> books = bookService.getBooksByAuthor(author);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Data buku berhasil diambil");
            baseResponseDTO.setData(books);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal mengambil data buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @GetMapping(SEARCH_BOOK_BY_SHELF)
    public ResponseEntity<BaseResponseDTO<List<BookResponseDTO>>> getBooksByShelfLocation(
            @RequestParam String shelfLocation) {
        
        var baseResponseDTO = new BaseResponseDTO<List<BookResponseDTO>>();
        
        try {
            List<BookResponseDTO> books = bookService.getBooksByShelfLocation(shelfLocation);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Data buku berhasil diambil");
            baseResponseDTO.setData(books);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal mengambil data buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @GetMapping(SEARCH_BOOK_BY_CONDITION)
    public ResponseEntity<BaseResponseDTO<List<BookResponseDTO>>> getBooksByCondition(
            @RequestParam String condition) {
        
        var baseResponseDTO = new BaseResponseDTO<List<BookResponseDTO>>();
        
        try {
            List<BookResponseDTO> books = bookService.getBooksByCondition(condition);
            baseResponseDTO.setStatus(200);
            baseResponseDTO.setMessage("Data buku berhasil diambil");
            baseResponseDTO.setData(books);
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception ex) {
            baseResponseDTO.setStatus(500);
            baseResponseDTO.setMessage("Gagal mengambil data buku: " + ex.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
}
