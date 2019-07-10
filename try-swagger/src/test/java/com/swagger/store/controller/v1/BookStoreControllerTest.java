package com.swagger.store.controller.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swagger.Application;
import com.swagger.book.model.v1.Book;
import com.swagger.book.service.BookService;
import com.swagger.error.handler.BookStoreExceptionHandler;
import com.swagger.error.model.v1.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(Application.class)
@Slf4j
//@AutoConfigureRestDocs(uriHost = "home.com")
public class BookStoreControllerTest {
  @Rule public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

  private MockMvc mvc = MockMvcBuilders.standaloneSetup(new BookStoreController(getBookServiceMock()))
          .setControllerAdvice(new BookStoreExceptionHandler())
          .apply(
                  documentationConfiguration(restDocumentation)
                          .operationPreprocessors()
                          .withRequestDefaults(Preprocessors.prettyPrint())
                          .withResponseDefaults(Preprocessors.prettyPrint())
                          .and()
                          .uris()
                          .withHost("home.com")
                          .withScheme("http"))
          .build();

  @Autowired
  private MockMvc swaggerApi;

  private Gson gson = new GsonBuilder()
          .setDateFormat(DateFormat.FULL, DateFormat.FULL)
          .setPrettyPrinting()
          .create();

  @Test
  public void getAllBooksOkTest() throws Exception {
    MvcResult result = mvc.perform(get("/store/api/v1/books"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-all-books-v1"))
            .andReturn();

    Type listObject = new TypeToken<ArrayList<Book>>() {
    }.getType();
    List<Book> books = gson.fromJson(result.getResponse().getContentAsString(), listObject);

    assertThat(books.size()).isEqualTo(1);
  }

  @Test
  public void addBookOkTest() throws Exception {
    Book input = getBook();

    MvcResult result = mvc.perform(
            post("/store/api/v1/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(input))
    )
            .andDo(print())
            .andDo(document("add-book-v1"))
            .andExpect(status().isCreated())
            .andReturn();

    String[] url = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("/");

    long newBookId = Long.valueOf(url[url.length - 1]);

    Book book = gson.fromJson(result.getResponse().getContentAsString(), Book.class);

    assertThat(book.getId()).isEqualTo(newBookId);
  }

  @Test
  public void getBookByIdOkTest() throws Exception {
    MvcResult result = mvc.perform(get("/store/api/v1/books/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-book-by-id-v1"))
            .andReturn();

    Book retrievedBook = gson.fromJson(result.getResponse().getContentAsString(), Book.class);

    assertThat(retrievedBook.getId()).isEqualTo(1);
  }

  @Test
  public void getBookByIdNotFoundTest() throws Exception {
    MvcResult result = mvc.perform(get("/store/api/v1/books/2"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();

    ErrorResponse errorResponse = gson.fromJson(result.getResponse().getContentAsString(), ErrorResponse.class);

    assertThat(errorResponse.getErrorMessage()).isEqualToIgnoringCase("Error message");
  }

  @Test
  public void getBookByIdInternalServerErrorTest() throws Exception {
    MvcResult result = mvc.perform(get("/store/api/v1/books/3"))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andReturn();

    ErrorResponse errorResponse = gson.fromJson(result.getResponse().getContentAsString(), ErrorResponse.class);

    assertThat(errorResponse.getErrorMessage()).isEqualToIgnoringCase("Bad error");
  }

  @Test
  public void updateBookOkTest() throws Exception {
    Book newBook = Book.builder()
            .title("New title")
            .build();

    MvcResult result = mvc.perform(patch("/store/api/v1/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(newBook))
    )
            .andDo(print())
            .andDo(document("update-book-v1"))
            .andExpect(status().isOk())
            .andReturn();

    Book book = gson.fromJson(result.getResponse().getContentAsString(), Book.class);

    assertThat(book.getTitle()).isEqualToIgnoringCase("New Title");
  }

  @Test
  public void deleteBookByIdOkTest() throws Exception {
    mvc.perform(delete("/store/api/v1/books/3"))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("delete-book-v1"))
            .andReturn();
  }

  @Test
  public void generateSwaggerDoc() throws Exception {
    String swaggerJson = swaggerApi.perform(get("/v2/api-docs")
            .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    try (Writer writer = new FileWriter(new File("target/generated-sources/swagger.json"))) {
      writer.append(swaggerJson);
    }
  }

  private Book getBook() {
    return Book.builder()
            .author("Author")
            .title("Title")
            .id(1L)
            .build();
  }

  private BookService getBookServiceMock() {
    BookService bookServiceMock = Mockito.mock(BookService.class);

    when(bookServiceMock.getAllBooks()).thenReturn(Collections.singletonList(getBook()));

    Book book = getBook();
    when(bookServiceMock.addBook(any(Book.class))).thenReturn(book);

    when(bookServiceMock.getBookById(1L)).thenReturn(book);

    when(bookServiceMock.getBookById(2L)).thenThrow(new NoSuchElementException("Error message"));

    when(bookServiceMock.getBookById(3L)).thenThrow(new RuntimeException("Bad error"));

    book.setTitle("New Title");
    when(bookServiceMock.updateBook(any(Book.class))).thenReturn(book);

    return bookServiceMock;
  }
}
