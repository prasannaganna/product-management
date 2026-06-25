package com.st.productmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class RequestDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotBlank
    @Size(min = 3, max = 20)
    private String brand;
    @NotNull
    @Positive
    private Long price;
    @NotNull
    @Positive
    private Integer quantity;
    @NotBlank
    @Size(min = 3, max = 20)
    private String category;
}
