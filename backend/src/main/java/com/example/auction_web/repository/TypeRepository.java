package com.example.auction_web.repository;

import com.example.auction_web.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, String> {
    List<Type> findTypesByCategory_CategoryName(String categoryName);
    Page<Type> findAll(Specification<Type> specification, Pageable pageable);
    List<Type> findAll(Specification<Type> specification);
}
