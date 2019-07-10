package com.swagger.book.model.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Book representation")
public class Book {
  @ApiModelProperty(
          accessMode = ApiModelProperty.AccessMode.READ_ONLY,
          value = "Book id")
  private Long id;

  @ApiModelProperty(
          value = "Book title",
          accessMode = ApiModelProperty.AccessMode.READ_WRITE,
          required = true,
          example = "Some title")
  private String title;

  @ApiModelProperty(
          value = "Book author",
          accessMode = ApiModelProperty.AccessMode.READ_WRITE,
          required = true,
          example = "John Doe")
  private String author;

  @ApiModelProperty(
          value = "Book price",
          accessMode = ApiModelProperty.AccessMode.READ_WRITE,
          required = true,
          example = "25.99")
  private Double price;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return id.equals(book.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
