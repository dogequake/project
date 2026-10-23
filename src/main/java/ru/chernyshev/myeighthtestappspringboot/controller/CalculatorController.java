package ru.chernyshev.myeighthtestappspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.chernyshev.myeighthtestappspringboot.model.Server;
import ru.chernyshev.myeighthtestappspringboot.service.CostBreakdown;
import ru.chernyshev.myeighthtestappspringboot.service.CostCalculationService;
import ru.chernyshev.myeighthtestappspringboot.service.ServerService;

import java.math.BigDecimal;
import java.util.List;

/** Форма расчёта характеристик: годовая стоимость содержания сервера. */
@Controller
@RequiredArgsConstructor
public class CalculatorController {

    private final ServerService serverService;
    private final CostCalculationService costService;

    @GetMapping("/calculator")
    public String calculator(@RequestParam(required = false) Long serverId,
                             @RequestParam(defaultValue = "1") int years,
                             Model model, Authentication auth) {
        years = Math.max(1, Math.min(years, 50));
        List<Server> servers = serverService.findVisible(auth);

        List<CostBreakdown> all = servers.stream().map(costService::calculate).toList();
        BigDecimal grandTotal = all.stream()
                .map(CostBreakdown::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("servers", servers);
        model.addAttribute("all", all);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("years", years);
        model.addAttribute("selectedId", serverId);

        if (serverId != null) {
            CostBreakdown result = costService.calculate(serverService.getVisible(serverId, auth));
            model.addAttribute("result", result);
            model.addAttribute("resultForYears", result.getTotal().multiply(BigDecimal.valueOf(years)));
        }
        return "calculator";
    }
}
