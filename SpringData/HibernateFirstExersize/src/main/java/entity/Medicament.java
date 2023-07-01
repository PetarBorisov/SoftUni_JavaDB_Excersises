package entity;

import entity.BaseEntity;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicaments")
public class Medicament extends BaseEntity {
    private String name;

    public Medicament() {

    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}