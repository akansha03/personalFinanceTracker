package com.finance.personalTracker.repositories;

import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.*;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    List<Category> findByType(CategoryType type);

    Optional<Category> findByNameAndType(String name, CategoryType type);

    boolean existsByNameAndType(String name, CategoryType type);
}
