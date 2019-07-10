package com.swagger.book.service;

import com.swagger.book.model.v1.Book;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Setter
public class BookService {
  private List<Book> books = new ArrayList<>();

  public List<Book> getAllBooks() {
    return books;
  }

  public Book addBook(Book book) {
    book.setId(getNextBookId());

    books.add(book);

    return book;
  }

  public Book getBookById(long bookId) {
    return books.stream()
            .filter(book -> book.getId() == bookId)
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Book with id " + bookId + " not found"));
  }

  public Book updateBook(Book newBook) {
    Book book = getBookById(newBook.getId());

    return updateBook(book, newBook);
  }

  private long getNextBookId() {
    return books.stream().mapToLong(Book::getId).max().orElse(1);
  }

  private Book updateBook(Book oldBook, Book newBook) {
    if (!StringUtils.isEmpty(newBook.getAuthor())) {
      oldBook.setAuthor(newBook.getAuthor());
    }
    if (!StringUtils.isEmpty(newBook.getTitle())) {
      oldBook.setTitle(newBook.getTitle());
    }
    if (!StringUtils.isEmpty(newBook.getPrice())) {
      oldBook.setPrice(newBook.getPrice());
    }

    return oldBook;
  }

  public void deleteBookById(Long id) {
    Book book = getBookById(id);

    books.remove(book);
  }
}
