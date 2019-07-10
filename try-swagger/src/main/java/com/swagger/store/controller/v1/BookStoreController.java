package com.swagger.store.controller.v1;

import com.swagger.book.model.v1.Book;
import com.swagger.book.service.BookService;
import com.swagger.error.model.v1.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/store/api/v1")
@Api(description = "Public APIs to manage books", tags = {"Book store controller"})
public class BookStoreController {
  private BookService bookService;

  @Autowired
  BookStoreController(BookService bookService) {
    this.bookService = bookService;
  }

  @ApiOperation(value = "An API to get a list of books from the store")
  @ApiResponses(value = {
          @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
  })
  @GetMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Book>> getAllBooks() {
    List<Book> result = bookService.getAllBooks();

    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "An API to add a new book to the store")
  @ApiResponses(value = {
          @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
  })
  @PostMapping(value = "/books", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> addBook(
          @ApiParam(name = "Book", value = "A book to be created")
          @RequestBody Book book) {
    Book result = bookService.addBook(book);

    String locationHeader = "/store/api/v1/books/" + result.getId();

    return ResponseEntity.status(HttpStatus.CREATED).header("Location", locationHeader).body(result);
  }

  @ApiOperation(value = "An API to get a book by its id")
  @ApiResponses(value = {
          @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class),
          @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class)
  })
  @GetMapping(value = "/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> getBookById(
          @ApiParam(name = "Book id", value = "Id of the book to be fetched", example = "42")
          @PathVariable String id) {
    long bookId = Long.valueOf(id);

    Book result = bookService.getBookById(bookId);

    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "An API to update a book by its id")
  @ApiResponses(value = {
          @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class),
          @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class)
  })
  @PatchMapping(value = "/books/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> updateBook(
          @ApiParam(name = "Book id", value = "Id of the book to be updated")
          @PathVariable String id,
          @ApiParam(name = "Book", value = "New values to be used")
          @RequestBody Book newBook) {
    newBook.setId(Long.valueOf(id));

    Book book = bookService.updateBook(newBook);

    return ResponseEntity.ok().body(book);
  }

  @ApiOperation(value = "An API to delete a book by its id")
  @ApiResponses(value = {
          @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class),
          @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class)
  })
  @DeleteMapping(value = "/books/{id}")
  public ResponseEntity<Void> deleteBook(
          @ApiParam(name = "Book id", value = "Id of the book to be deleted")
          @PathVariable String id) {
    bookService.deleteBookById(Long.valueOf(id));

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
