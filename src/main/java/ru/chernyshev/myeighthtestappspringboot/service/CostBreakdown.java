package ru.chernyshev.myeighthtestappspringboot.service;

import lombok.Value;

import java.math.BigDecimal;

/** Результат расчёта годовой стоимости содержания сервера (руб. в год). */
@Value
public class CostBreakdown {
    Long serverId;
    String serverName;
    String locationName;
    /** Амортизация оборудования */
    BigDecimal amortization;
    /** Электроэнергия (с учётом PUE) */
    BigDecimal electricity;
    /** Аренда места в стойке */
    BigDecimal rent;
    /** Лицензии и поддержка */
    BigDecimal licenses;
    /** Итого за год */
    BigDecimal total;
}
