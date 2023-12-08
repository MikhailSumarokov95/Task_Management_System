package ru.sumarokov.task_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.sumarokov.task_management_system.entity.User;

public class UserDto {

    private Long id;
    @NotEmpty(message = "Поле \"Электронный адрес\" должно быть заполнено")
    @Email(message = "Неверный формат электронной почты")
    @Size(min = 0, max = 64, message = "Поле \"Почта\" должно быть длиной от 0 до 64 символов")
    private String email;
    @NotEmpty(message = "Поле \"Пароль\" должно быть заполнено")
    @Size(min = 0, max = 64, message = "Поле \"Пароль\" должно быть длиной от 0 до 64 символов")
    private String password;

    public UserDto(Long id,
                   String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public static UserDto toDto(User entity) {
        return new UserDto(entity.getId(),
                entity.getEmail());
    }

    public User toEntity() {
        return new User(id, password, email);
    }
}
