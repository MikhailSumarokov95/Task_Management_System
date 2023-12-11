package ru.sumarokov.task_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserInfoDto {

    @NotEmpty(message = "Поле \"Электронный адрес\" должно быть заполнено")
    @Email(message = "Неверный формат электронной почты")
    @Size(min = 0, max = 64, message = "Поле \"Почта\" должно быть длиной от 0 до 64 символов")
    private String email;
    @NotEmpty(message = "Поле \"Пароль\" должно быть заполнено")
    @Size(min = 0, max = 64, message = "Поле \"Пароль\" должно быть длиной от 0 до 64 символов")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
