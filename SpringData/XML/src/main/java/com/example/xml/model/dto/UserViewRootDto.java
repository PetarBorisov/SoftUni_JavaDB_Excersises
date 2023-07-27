package com.example.xml.model.dto;

import com.example.xml.model.entity.User;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserViewRootDto {

@XmlElement(name = "user")
    private List<UserWittProductDto> products;

    public List<UserWittProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<UserWittProductDto> products) {
        this.products = products;
    }
}
