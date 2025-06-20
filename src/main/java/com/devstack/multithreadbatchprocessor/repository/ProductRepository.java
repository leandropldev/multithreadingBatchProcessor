package com.devstack.multithreadbatchprocessor.repository;

import com.devstack.multithreadbatchprocessor.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
