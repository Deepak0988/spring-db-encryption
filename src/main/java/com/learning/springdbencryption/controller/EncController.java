package com.learning.springdbencryption.controller;

import com.learning.springdbencryption.service.EncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EncController {

    @Autowired
    EncService encService;

    @GetMapping("/")
    public ResponseEntity getTestData() {

        return new ResponseEntity(encService.get(),HttpStatus.OK);
    }

    @PostMapping ("/")
    public ResponseEntity decrypt() {
        encService.save();
        return new ResponseEntity(HttpStatus.OK);
    }



}
