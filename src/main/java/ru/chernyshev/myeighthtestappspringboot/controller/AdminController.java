package ru.chernyshev.myeighthtestappspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.chernyshev.myeighthtestappspringboot.model.Role;
import ru.chernyshev.myeighthtestappspringboot.service.UserService;

/** Форма добавления ролей. Доступна только ADMIN (см. SecurityConfig). */
@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String roles(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        return "admin/roles";
    }

    @PostMapping
    public String changeRole(@RequestParam String username, @RequestParam Role role,
                             Authentication auth, RedirectAttributes ra) {
        try {
            userService.changeRole(username, role, auth.getName());
            ra.addFlashAttribute("message", "Роль пользователя " + username + " изменена на " + role
                    + ". Изменения вступят в силу после следующего входа этого пользователя.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/roles";
    }
}
