package ru.chernyshev.myeighthtestappspringboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegisterForm {

    @NotBlank(message = "Введите логин")
    @Size(min = 3, max = 50, message = "Логин: от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Введите пароль")
    @Size(min = 4, max = 100, message = "Пароль: от 4 до 100 символов")
    private String password;

    @NotBlank(message = "Повторите пароль")
    private String confirmPassword;
}
