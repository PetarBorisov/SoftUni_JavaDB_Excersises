package softuni.exam.models.dto;


import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;

public class PartsSeedDto {

    @NotNull
    @Size(min = 2,max = 19)
    @Expose
    private String partName;

    @NotNull
    @DecimalMin("10")
    @DecimalMax("2000")
    @Expose
    private Double price;

    @Positive
    @Expose
    private  Integer quantity;

    public PartsSeedDto() {
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
