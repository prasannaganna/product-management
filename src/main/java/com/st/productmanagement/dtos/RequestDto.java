package com.st.productmanagement.dtos;

import lombok.Data;

@Data
public class RequestDto {
    private String name;

    private String brand;

    private Long price;

    private Integer quantity;

    private String category;
}
