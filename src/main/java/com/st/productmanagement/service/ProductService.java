package com.st.productmanagement.service;

import com.st.productmanagement.ProductEnums;
import com.st.productmanagement.dtos.RequestDto;
import com.st.productmanagement.dtos.ResponseDto;
import com.st.productmanagement.entity.Product;
import com.st.productmanagement.exception.ProductAlreadyExistException;
import com.st.productmanagement.exception.ProductNotFoundException;
import com.st.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ResponseDto save(RequestDto requestDto) {

        Optional<Product> existingProduct =
                productRepository.findByNameAndBrand(requestDto.getName(), requestDto.getBrand());

        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistException("Product already exists");
        }
        //mapping the dtos into object
        Product product = new Product();
        product.setName(requestDto.getName());
        product.setBrand(requestDto.getBrand());
        product.setPrice(requestDto.getPrice());
        product.setQuantity(requestDto.getQuantity());
        product.setCategory(requestDto.getCategory());
        product.setStatus(ProductEnums.AVAILABLE);
        product.setCreatedDate(LocalDateTime.now());
        // save the object
        productRepository.save(product);
        // mapping the object to dtos sending the response
        ResponseDto responseDto = new ResponseDto();
        responseDto.setName(product.getName());
        responseDto.setBrand(product.getBrand());
        responseDto.setCategory(product.getCategory());
        responseDto.setQuantity(product.getQuantity());
        responseDto.setPrice(product.getPrice());
        responseDto.setStatus(product.getStatus());
        return responseDto;
    }
    public ResponseDto getProductById(Long id) {

        Product product= productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found exception"));
        ResponseDto responseDto = new ResponseDto();
        responseDto.setName(product.getName());
        responseDto.
setBrand(product.getBrand());
       responseDto.setPrice(product.getPrice());
       responseDto.setCategory(product.getCategory());
       responseDto.setQuantity(product.getQuantity());
       responseDto.setStatus(product.getStatus());
        return  responseDto;
    }
}