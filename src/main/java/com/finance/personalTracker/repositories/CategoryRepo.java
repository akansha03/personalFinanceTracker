package com.finance.personalTracker.repositories;

import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    List<Category> findByType(CategoryType type);

}
