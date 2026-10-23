package ru.chernyshev.myeighthtestappspringboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.chernyshev.myeighthtestappspringboot.model.Location;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class LocationForm {

    @NotBlank(message = "Введите название площадки")
    @Size(max = 100, message = "Не более 100 символов")
    private String name;

    @NotBlank(message = "Введите город")
    @Size(max = 100, message = "Не более 100 символов")
    private String city;

    @Size(max = 255, message = "Не более 255 символов")
    private String address;

    @NotNull(message = "Введите цену электроэнергии")
    @DecimalMin(value = "0.0", message = "Не может быть отрицательной")
    private BigDecimal electricityPricePerKwh;

    @NotNull(message = "Введите PUE")
    @DecimalMin(value = "1.0", message = "PUE не может быть меньше 1.0")
    private BigDecimal pue;

    @NotNull(message = "Введите стоимость аренды 1U")
    @DecimalMin(value = "0.0", message = "Не может быть отрицательной")
    private BigDecimal rackUnitRentPerMonth;

    public static LocationForm from(Location l) {
        LocationForm f = new LocationForm();
        f.name = l.getName();
        f.city = l.getCity();
        f.address = l.getAddress();
        f.electricityPricePerKwh = l.getElectricityPricePerKwh();
        f.pue = l.getPue();
        f.rackUnitRentPerMonth = l.getRackUnitRentPerMonth();
        return f;
    }

    public void applyTo(Location l) {
        l.setName(name.trim());
        l.setCity(city.trim());
        l.setAddress(address == null || address.isBlank() ? null : address.trim());
        l.setElectricityPricePerKwh(electricityPricePerKwh);
        l.setPue(pue);
        l.setRackUnitRentPerMonth(rackUnitRentPerMonth);
    }
}
