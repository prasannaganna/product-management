package com.st.productmanagement.repository;

import com.st.productmanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository  extends JpaRepository<Product,Long>{
    Optional<Product> findByNameAndBrand(String name,String brand);
}
