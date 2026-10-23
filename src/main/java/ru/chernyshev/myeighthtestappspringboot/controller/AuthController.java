package ru.chernyshev.myeighthtestappspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.chernyshev.myeighthtestappspringboot.dto.RegisterForm;
import ru.chernyshev.myeighthtestappspringboot.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult br) {
        if (!br.hasErrors() && !form.getPassword().equals(form.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "mismatch", "Пароли не совпадают");
        }
        if (!br.hasErrors() && userService.exists(form.getUsername().trim())) {
            br.rejectValue("username", "exists", "Такой логин уже занят");
        }
        if (br.hasErrors()) {
            return "register";
        }
        userService.register(form.getUsername(), form.getPassword());
        return "redirect:/login?registered";
    }
}
