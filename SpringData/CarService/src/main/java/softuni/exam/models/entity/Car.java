package softuni.exam.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    private Long id;
    private CarType carType;
    private String carMake;
    private String carModel;
    private Double engine;
    private Integer kilometers;
    private String plateNumber;
    private Integer year;

    public Car() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @Column(name = "car_type",nullable = false)
@Enumerated(EnumType.STRING)
    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
@Column(name = "car_make",nullable = false)
    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }
@Column(name = "car_model",nullable = false)
    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

@Column(name = "plate_number",unique = true,nullable = false)
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
@Column(name = "year",nullable = false)
    public int getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
@Column(name = "engine",nullable = false)
    public Double getEngine() {
        return engine;
    }

    public void setEngine(Double engine) {
        this.engine = engine;
    }
@Column(name = "kilometers",nullable = false)
    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }
}
