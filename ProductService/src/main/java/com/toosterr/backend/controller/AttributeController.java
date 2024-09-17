package com.toosterr.backend.controller;

import com.toosterr.backend.dto.AddAttributeRequest;
import com.toosterr.backend.service.AttributeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AttributeController {

    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @GetMapping("/attributes")
    public ResponseEntity<?> getAllAttributes(){
        return new ResponseEntity<>(attributeService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/attribute/add")
    public ResponseEntity<?> addAttribute(@RequestBody AddAttributeRequest attribute){
        return new ResponseEntity<>(attributeService.add(attribute), HttpStatus.OK);
    }

}
