package ru.chernyshev.myeighthtestappspringboot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.chernyshev.myeighthtestappspringboot.model.Location;
import ru.chernyshev.myeighthtestappspringboot.model.Role;
import ru.chernyshev.myeighthtestappspringboot.model.Server;
import ru.chernyshev.myeighthtestappspringboot.repository.LocationRepository;
import ru.chernyshev.myeighthtestappspringboot.repository.ServerRepository;
import ru.chernyshev.myeighthtestappspringboot.repository.UserRepository;
import ru.chernyshev.myeighthtestappspringboot.service.UserService;

import java.math.BigDecimal;

/** Тестовые данные при первом запуске (если в БД ещё нет пользователей). */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;
    private final LocationRepository locationRepository;
    private final ServerRepository serverRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        userService.create("admin", "admin", Role.ADMIN);
        userService.create("user", "user", Role.USER);
        userService.create("reader", "reader", Role.READ_ONLY);

        Location ekb = location("ЦОД Екатеринбург-1", "Екатеринбург", "ул. Мира, 32",
                "5.80", "1.45", "1200.00", "admin");
        Location msk = location("ЦОД Москва-Север", "Москва", "Дмитровское ш., 100",
                "7.20", "1.30", "2500.00", "admin");
        Location spb = location("ЦОД Санкт-Петербург", "Санкт-Петербург", "пр. Энергетиков, 5",
                "6.50", "1.50", "1800.00", "user");

        server("web-01", "10.0.1.11", 16, 64, 1000, 350, 1, "280000.00", 5, "15000.00", ekb, "admin");
        server("db-01", "10.0.1.21", 32, 256, 8000, 750, 2, "750000.00", 5, "60000.00", msk, "admin");
        server("backup-01", "10.0.2.31", 8, 32, 24000, 400, 2, "320000.00", 6, "0.00", ekb, "admin");
        server("app-01", "10.0.3.11", 24, 128, 2000, 500, 1, "410000.00", 5, "25000.00", spb, "user");
        server("cache-01", "10.0.3.12", 16, 128, 500, 300, 1, "260000.00", 4, "0.00", spb, "user");
    }

    private Location location(String name, String city, String address, String price, String pue,
                              String rent, String owner) {
        Location l = new Location();
        l.setName(name);
        l.setCity(city);
        l.setAddress(address);
        l.setElectricityPricePerKwh(new BigDecimal(price));
        l.setPue(new BigDecimal(pue));
        l.setRackUnitRentPerMonth(new BigDecimal(rent));
        l.setCreatedBy(owner);
        return locationRepository.save(l);
    }

    private void server(String name, String ip, int cpu, int ram, int storage, int watts, int units,
                        String price, int years, String license, Location loc, String owner) {
        Server s = new Server();
        s.setName(name);
        s.setIpAddress(ip);
        s.setCpuCores(cpu);
        s.setRamGb(ram);
        s.setStorageGb(storage);
        s.setPowerWatts(watts);
        s.setRackUnits(units);
        s.setPurchasePrice(new BigDecimal(price));
        s.setAmortizationYears(years);
        s.setLicenseCostPerYear(new BigDecimal(license));
        s.setLocation(loc);
        s.setCreatedBy(owner);
        serverRepository.save(s);
    }
}
