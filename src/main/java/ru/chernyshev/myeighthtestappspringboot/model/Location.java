package ru.chernyshev.myeighthtestappspringboot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/** Площадка / ЦОД */
@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 255)
    private String address;

    /** Цена электроэнергии, руб. за кВт·ч */
    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal electricityPricePerKwh;

    /** PUE ЦОДа (коэффициент энергоэффективности, учитывает охлаждение), обычно 1.2-2.0 */
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal pue;

    /** Аренда одного юнита (1U) стойки, руб. в месяц */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal rackUnitRentPerMonth;

    /** Логин пользователя, создавшего запись */
    @Column(nullable = false, updatable = false, length = 50)
    private String createdBy;
}
