package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarsSeedRootDto {
@XmlElement(name = "car")
    private List<CarsSeedDto> cars;

    public List<CarsSeedDto> getCars() {
        return cars;
    }

    public void setCars(List<CarsSeedDto> cars) {
        this.cars = cars;
    }
}
