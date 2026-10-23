package ru.chernyshev.myeighthtestappspringboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.chernyshev.myeighthtestappspringboot.model.Server;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ServerForm {

    @NotBlank(message = "Введите название сервера")
    @Size(max = 100, message = "Не более 100 символов")
    private String name;

    @NotBlank(message = "Введите IP-адрес")
    @Size(max = 45, message = "Не более 45 символов")
    private String ipAddress;

    @Min(value = 1, message = "Минимум 1 ядро")
    private Integer cpuCores;

    @Min(value = 1, message = "Минимум 1 ГБ")
    private Integer ramGb;

    @Min(value = 1, message = "Минимум 1 ГБ")
    private Integer storageGb;

    @NotNull(message = "Введите мощность")
    @Min(value = 1, message = "Мощность должна быть больше 0")
    private Integer powerWatts;

    @NotNull(message = "Введите количество юнитов")
    @Min(value = 1, message = "Минимум 1U")
    private Integer rackUnits;

    @NotNull(message = "Введите стоимость покупки")
    @DecimalMin(value = "0.0", message = "Не может быть отрицательной")
    private BigDecimal purchasePrice;

    @NotNull(message = "Введите срок амортизации")
    @Min(value = 1, message = "Минимум 1 год")
    private Integer amortizationYears;

    @NotNull(message = "Введите стоимость лицензий")
    @DecimalMin(value = "0.0", message = "Не может быть отрицательной")
    private BigDecimal licenseCostPerYear;

    @NotNull(message = "Выберите площадку")
    private Long locationId;

    public static ServerForm from(Server s) {
        ServerForm f = new ServerForm();
        f.name = s.getName();
        f.ipAddress = s.getIpAddress();
        f.cpuCores = s.getCpuCores();
        f.ramGb = s.getRamGb();
        f.storageGb = s.getStorageGb();
        f.powerWatts = s.getPowerWatts();
        f.rackUnits = s.getRackUnits();
        f.purchasePrice = s.getPurchasePrice();
        f.amortizationYears = s.getAmortizationYears();
        f.licenseCostPerYear = s.getLicenseCostPerYear();
        f.locationId = s.getLocation().getId();
        return f;
    }

    /** Копирует поля формы в сущность (площадку выставляет сервис). */
    public void applyTo(Server s) {
        s.setName(name.trim());
        s.setIpAddress(ipAddress.trim());
        s.setCpuCores(cpuCores);
        s.setRamGb(ramGb);
        s.setStorageGb(storageGb);
        s.setPowerWatts(powerWatts);
        s.setRackUnits(rackUnits);
        s.setPurchasePrice(purchasePrice);
        s.setAmortizationYears(amortizationYears);
        s.setLicenseCostPerYear(licenseCostPerYear);
    }
}
