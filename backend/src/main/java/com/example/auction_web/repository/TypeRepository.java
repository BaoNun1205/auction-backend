package com.example.auction_web.repository;

import com.example.auction_web.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, String> {
    List<Type> findTypesByCategory_CategoryName(String categoryName);
    List<Type> findByDelFlag(Boolean delFlag);
    List<Type> findByTypeNameContainingIgnoreCase(String keyword);
    List<Type> findByDelFlagAndTypeNameContainingIgnoreCase(Boolean delFlag, String keyword);
}
