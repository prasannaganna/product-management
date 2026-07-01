package com.st.productmanagement.entity;

import com.st.productmanagement.enums.ProductEnums;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data

@Table(name ="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;
     private String name;
     private String brand;
      private long price;
     private int quantity;
     private String category;
     // status have fixes set of the values  create the enum
    // give the data type as the enum
    @Enumerated(EnumType.STRING)
     private ProductEnums status;
     private LocalDateTime createdDate;
     @ManyToOne
     @JoinColumn(name = "user_id")
      private User user;

}
