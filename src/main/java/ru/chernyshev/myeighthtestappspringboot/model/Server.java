package ru.chernyshev.myeighthtestappspringboot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servers")
@Getter
@Setter
@NoArgsConstructor
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 45)
    private String ipAddress;

    private Integer cpuCores;
    private Integer ramGb;
    private Integer storageGb;

    /** Потребляемая мощность, Вт */
    @Column(nullable = false)
    private Integer powerWatts;

    /** Сколько юнитов (U) занимает в стойке */
    @Column(nullable = false)
    private Integer rackUnits;

    /** Стоимость покупки, руб. */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal purchasePrice;

    /** Срок амортизации, лет */
    @Column(nullable = false)
    private Integer amortizationYears;

    /** Лицензии и поддержка, руб. в год */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal licenseCostPerYear;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false, updatable = false, length = 50)
    private String createdBy;
}
