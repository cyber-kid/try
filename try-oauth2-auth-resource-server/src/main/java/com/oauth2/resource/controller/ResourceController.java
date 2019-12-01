package com.oauth2.resource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping(value = "/foo/")
    public ResponseEntity<String> getFoo() {
        return ResponseEntity.ok("Secured!");
    }
}
