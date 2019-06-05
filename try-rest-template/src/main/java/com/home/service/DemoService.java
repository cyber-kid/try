package com.home.service;

import com.home.dto.DemoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DemoService {
  public DemoDto getDemoDto() {
    return DemoDto.builder()
            .name("Demo dto")
            .createDate(LocalDateTime.now())
            .build();
  }
}
