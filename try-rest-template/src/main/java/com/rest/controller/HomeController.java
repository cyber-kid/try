package com.rest.controller;

import com.rest.dto.DemoDto;
import com.rest.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {
  @Autowired
  private DemoService demoService;

  @GetMapping("/getData")
  public ResponseEntity<DemoDto> getData() {
    return ResponseEntity.ok(demoService.getDemoDto());
  }
}
