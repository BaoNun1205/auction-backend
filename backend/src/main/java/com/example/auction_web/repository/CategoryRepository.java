package com.example.auction_web.repository;

import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Boolean existsByCategoryName(String categoryName);
    List<Category> findByDelFlag(Boolean delFlag);
    List<Category> findByCategoryNameContainingIgnoreCase(String keyword);
    List<Category> findByDelFlagAndCategoryNameContainingIgnoreCase(Boolean delFlag, String keyword);
}
