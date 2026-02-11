package io.propenxixi.nasa_library_be.book.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.propenxixi.nasa_library_be.book.dto.request.AddBookRequestDTO;
import io.propenxixi.nasa_library_be.book.dto.request.UpdateBookRequestDTO;
import io.propenxixi.nasa_library_be.book.dto.response.BookResponseDTO;
import io.propenxixi.nasa_library_be.book.model.Book;
import io.propenxixi.nasa_library_be.book.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Override
    public BookResponseDTO createBook(AddBookRequestDTO dto) {
        if (bookRepository.findByIsbnCode(dto.getIsbnCode()).isPresent()) {
            throw new IllegalArgumentException("Buku dengan ISBN Code " + dto.getIsbnCode() + " sudah terdaftar");
        }
        
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbnCode(dto.getIsbnCode())
                .length(dto.getLength())
                .language(dto.getLanguage())
                .imageUrl(dto.getImageUrl())
                .shelfLocation(dto.getShelfLocation())
                .totalCopies(dto.getTotalCopies() != null ? dto.getTotalCopies() : 0)
                .availableCopies(dto.getAvailableCopies() != null ? dto.getAvailableCopies() : 0)
                .condition(dto.getCondition())
                .build();
        
        Book savedBook = bookRepository.save(book);
        bookRepository.flush();
        savedBook = bookRepository.findById(savedBook.getId()).orElse(savedBook);
        
        return convertToBookResponseDTO(savedBook);
    }
    
    @Override
    public List<BookResponseDTO> getAllBooks(Boolean isDeleted, String search) {
        List<Book> allBooks = bookRepository.findAll();
        List<BookResponseDTO> books = allBooks.stream()
                .map(this::convertToBookResponseDTO)
                .collect(Collectors.toList());
        
        // Filter by isDeleted if provided
        if (isDeleted != null) {
            books = books.stream()
                    .filter(b -> b.getIsDeleted().equals(isDeleted))
                    .collect(Collectors.toList());
        }
        
        // Filter by search (title, author, isbn) - case-insensitive
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            books = books.stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(searchLower) ||
                            b.getAuthor().toLowerCase().contains(searchLower) ||
                            b.getIsbnCode().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }
        
        // Sort by ID ascending
        books = books.stream()
                .sorted((b1, b2) -> b1.getId().compareTo(b2.getId()))
                .toList();
        
        return books;
    }
    
    @Override
    public List<BookResponseDTO> getBooksByTitle(String title) {
        List<Book> bookList = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookList.stream()
                .map(this::convertToBookResponseDTO)
                .sorted((b1, b2) -> b1.getId().compareTo(b2.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookResponseDTO> getBooksByAuthor(String author) {
        List<Book> bookList = bookRepository.findByAuthorContainingIgnoreCase(author);
        return bookList.stream()
                .map(this::convertToBookResponseDTO)
                .sorted((b1, b2) -> b1.getId().compareTo(b2.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookResponseDTO> getBooksByShelfLocation(String shelfLocation) {
        List<Book> bookList = bookRepository.findByShelfLocation(shelfLocation);
        return bookList.stream()
                .map(this::convertToBookResponseDTO)
                .sorted((b1, b2) -> b1.getId().compareTo(b2.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookResponseDTO> getBooksByCondition(String condition) {
        List<Book> bookList = bookRepository.findByCondition(condition);
        return bookList.stream()
                .map(this::convertToBookResponseDTO)
                .sorted((b1, b2) -> b1.getId().compareTo(b2.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public BookResponseDTO getBook(Long id) {
        Optional<Book> book = bookRepository.findActiveById(id);
        if (book.isEmpty()) {
            throw new IllegalArgumentException("Buku dengan ID " + id + " tidak ditemukan");
        }
        return convertToBookResponseDTO(book.get());
    }
    
    @Override
    public BookResponseDTO updateBook(UpdateBookRequestDTO dto) {
        Optional<Book> book = bookRepository.findById(dto.getId());
        if (book.isEmpty()) {
            throw new IllegalArgumentException("Buku dengan ID " + dto.getId() + " tidak ditemukan");
        }
        
        Book existingBook = book.get();
        existingBook.setTitle(dto.getTitle());
        existingBook.setAuthor(dto.getAuthor());
        existingBook.setLength(dto.getLength());
        existingBook.setLanguage(dto.getLanguage());
        existingBook.setImageUrl(dto.getImageUrl());
        existingBook.setShelfLocation(dto.getShelfLocation());
        existingBook.setTotalCopies(dto.getTotalCopies());
        existingBook.setAvailableCopies(dto.getAvailableCopies());
        existingBook.setCondition(dto.getCondition());
        
        Book updatedBook = bookRepository.save(existingBook);
        bookRepository.flush();
        updatedBook = bookRepository.findById(updatedBook.getId()).orElse(updatedBook);
        
        return convertToBookResponseDTO(updatedBook);
    }
    
    @Override
    public BookResponseDTO deleteBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new IllegalArgumentException("Buku dengan ID " + id + " tidak ditemukan");
        }
        
        Book existingBook = book.get();
        existingBook.setIsDeleted(true);
        
        Book deletedBook = bookRepository.save(existingBook);
        bookRepository.flush();
        
        return convertToBookResponseDTO(deletedBook);
    }
    
    @Override
    public BookResponseDTO activateBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new IllegalArgumentException("Buku dengan ID " + id + " tidak ditemukan");
        }
        
        Book existingBook = book.get();
        existingBook.setIsDeleted(false);
        
        Book activatedBook = bookRepository.save(existingBook);
        bookRepository.flush();
        
        return convertToBookResponseDTO(activatedBook);
    }
    
    @Override
    public List<BookResponseDTO> importBooksFromExcel(InputStream fileInputStream) throws IOException {
        List<BookResponseDTO> importedBooks = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            log.info("Starting Excel import. Total rows: " + sheet.getLastRowNum());
            
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.debug("Row " + i + " is null, skipping");
                    continue;
                }
                
                try {
                    // Excel columns: Title(0), Author(s)(1), ISBN Code(2), Length(3), Language(4), Total Copies(5), Shelf Location(6), Image(URL)(7)
                    String title = getCellValueAsString(row.getCell(0));
                    String authors = getCellValueAsString(row.getCell(1));
                    String isbnCode = getCellValueAsString(row.getCell(2));
                    String lengthStr = getCellValueAsString(row.getCell(3));
                    String language = getCellValueAsString(row.getCell(4));
                    String totalCopiesStr = getCellValueAsString(row.getCell(5));
                    String shelfLocation = getCellValueAsString(row.getCell(6));
                    String imageUrl = getCellValueAsString(row.getCell(7));
                    
                    log.debug("Row " + i + ": title=" + title + ", isbn=" + isbnCode);
                    
                    if (title == null || title.trim().isEmpty() ||
                        isbnCode == null || isbnCode.trim().isEmpty()) {
                        log.debug("Row " + i + " has missing required fields, skipping");
                        continue;
                    }
                    
                    Integer length = null;
                    if (lengthStr != null && !lengthStr.trim().isEmpty()) {
                        try {
                            length = Integer.parseInt(lengthStr.trim());
                        } catch (NumberFormatException e) {
                            log.debug("Could not parse length: " + lengthStr);
                            length = null;
                        }
                    }

                    Integer totalCopies = null;
                    if (totalCopiesStr != null && !totalCopiesStr.trim().isEmpty()) {
                        try {
                            totalCopies = Integer.parseInt(totalCopiesStr.trim());
                        } catch (NumberFormatException e) {
                            log.debug("Could not parse totalCopies: " + totalCopiesStr);
                            totalCopies = 0;
                        }
                    }

                    AddBookRequestDTO dto = new AddBookRequestDTO();
                    dto.setTitle(title.trim());
                    dto.setAuthor(authors != null ? authors.trim() : "");
                    dto.setIsbnCode(isbnCode.trim());
                    dto.setLength(length);
                    dto.setLanguage(language != null ? language.trim() : "");
                    dto.setImageUrl(imageUrl != null ? imageUrl.trim() : "");

                    // Set values from XLSX and defaults for library-specific fields
                    dto.setShelfLocation(shelfLocation != null ? shelfLocation.trim() : "");
                    dto.setTotalCopies(totalCopies != null ? totalCopies : 0);
                    dto.setAvailableCopies(totalCopies != null ? totalCopies : 0); // Set available to total initially
                    dto.setCondition("good");
                    
                    try {
                        BookResponseDTO createdBook = createBook(dto);
                        importedBooks.add(createdBook);
                        log.info("Successfully imported book: " + title);
                    } catch (IllegalArgumentException e) {
                        log.warn("Book already exists or validation failed for row " + i + ": " + e.getMessage());
                        continue;
                    }
                    
                } catch (Exception e) {
                    log.error("Error processing row " + i + ": " + e.getMessage(), e);
                    continue;
                }
            }
            
            log.info("Excel import completed. Total imported: " + importedBooks.size());
        }
        
        return importedBooks;
    }
    
    private String getCellValueAsString(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Check if it's a date cell
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date dateValue = cell.getDateCellValue();
                    LocalDate localDate = dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return localDate.toString(); // Returns YYYY-MM-DD format
                } else {
                    // For numeric cells, format as string to preserve large numbers
                    long numValue = (long) cell.getNumericCellValue();
                    return String.valueOf(numValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
    
    private BookResponseDTO convertToBookResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbnCode(),
                book.getLength(),
                book.getLanguage(),
                book.getImageUrl(),
                book.getShelfLocation(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getCondition(),
                book.getCreatedAt(),
                book.getUpdatedAt(),
                book.getIsDeleted()
        );
    }
}
