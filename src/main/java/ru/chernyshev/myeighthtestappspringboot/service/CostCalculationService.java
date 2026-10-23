package ru.chernyshev.myeighthtestappspringboot.service;

import org.springframework.stereotype.Service;
import ru.chernyshev.myeighthtestappspringboot.model.Location;
import ru.chernyshev.myeighthtestappspringboot.model.Server;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CostCalculationService {

    private static final BigDecimal HOURS_PER_YEAR = BigDecimal.valueOf(8760);
    private static final BigDecimal MONTHS = BigDecimal.valueOf(12);
    private static final BigDecimal WATTS_IN_KW = BigDecimal.valueOf(1000);

    /**
     * Годовая стоимость = амортизация + электричество + аренда стойки + лицензии
     *
     *  амортизация  = цена покупки / срок амортизации (лет)
     *  электричество = мощность(кВт) * 8760 ч * PUE * цена кВт·ч
     *  аренда       = юниты * цена 1U в месяц * 12
     *  лицензии     = лицензии и поддержка в год
     */
    public CostBreakdown calculate(Server s) {
        Location l = s.getLocation();

        BigDecimal amortization = s.getPurchasePrice()
                .divide(BigDecimal.valueOf(s.getAmortizationYears()), 2, RoundingMode.HALF_UP);

        BigDecimal kw = BigDecimal.valueOf(s.getPowerWatts())
                .divide(WATTS_IN_KW, 6, RoundingMode.HALF_UP);
        BigDecimal electricity = kw
                .multiply(HOURS_PER_YEAR)
                .multiply(l.getPue())
                .multiply(l.getElectricityPricePerKwh())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal rent = l.getRackUnitRentPerMonth()
                .multiply(BigDecimal.valueOf(s.getRackUnits()))
                .multiply(MONTHS)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal licenses = s.getLicenseCostPerYear().setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = amortization.add(electricity).add(rent).add(licenses);

        return new CostBreakdown(s.getId(), s.getName(), l.getName(),
                amortization, electricity, rent, licenses, total);
    }
}
