package softuni.exam.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "parts")
public class Part{

    private Long id;
    private String partName;
    private Double price;
    private Integer quantity;

    public Part() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "part_name",nullable = false,unique = true)
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @Column(name = "price",nullable = false)
    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
@Column(name = "quantity",nullable = false)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }}
