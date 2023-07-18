package com.example.json.model.dto;


import com.google.gson.annotations.Expose;

import java.math.BigDecimal;


public class CategoryByProductsDTO {

    @Expose
    private String name;
    @Expose
    private Long count;
    @Expose
    private Double averagePrice;
    @Expose
    private BigDecimal totalSum;

    public CategoryByProductsDTO() {
    }

    public CategoryByProductsDTO(String name, Long count, Double averagePrice, BigDecimal totalSum) {
        this.name = name;
        this.count = count;
        this.averagePrice = averagePrice;
        this.totalSum = totalSum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }
}
