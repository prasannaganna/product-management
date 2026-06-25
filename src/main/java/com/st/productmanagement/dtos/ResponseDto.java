package com.st.productmanagement.dtos;

import com.st.productmanagement.ProductEnums;
import lombok.Data;

@Data
public class ResponseDto {
    private String name;
    private Long price;
    private Integer quantity;
    private String brand;
    private String  category;
    private ProductEnums  status;
}
