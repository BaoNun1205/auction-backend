package com.example.auction_web.repository;

import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Boolean existsByCategoryName(String categoryName);
    Page<Category> findAll(Specification<Category> specification, Pageable pageable);
}
