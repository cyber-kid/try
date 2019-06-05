package com.home.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DemoDto {
  private String name;
  private LocalDateTime createDate;
}
