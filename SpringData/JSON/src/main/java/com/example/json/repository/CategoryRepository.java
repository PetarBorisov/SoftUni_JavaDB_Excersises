package com.example.json.repository;

import com.example.json.model.dto.CategoryByProductsDTO;
import com.example.json.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT NEW com.example.json.model.dto.CategoryByProductsDTO(c.name,COUNT(p.id),AVG(p.price),SUM(p.price))" +
            " from Product  as p" +
            " Join p.categories as c" +
            " Group By c.id" +
            " order by count(p.id) DESC")

    List<CategoryByProductsDTO> CategoryByProductSummary();

}
