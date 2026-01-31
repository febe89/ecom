package com.example.socialmedia.repository;

import com.example.socialmedia.model.Category;
import com.example.socialmedia.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);

    List<Product> findByProducts(List<Product> products);
}
