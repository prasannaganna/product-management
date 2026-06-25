package com.st.productmanagement.controller;

import com.st.productmanagement.dtos.RequestDto;
import com.st.productmanagement.dtos.ResponseDto;
import com.st.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping
    public ResponseDto save(@RequestBody RequestDto requestDto){
        return  productService.save(requestDto);
    }
    @GetMapping("/{id}")
    public  ResponseDto getById(@PathVariable  Long id){
        return  productService.getProductById(id);
    }
}
