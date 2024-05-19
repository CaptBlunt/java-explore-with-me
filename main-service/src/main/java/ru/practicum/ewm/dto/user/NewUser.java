package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NewUser {

    @NotBlank(message = "Поле 'name' должно быть заполнено")
    @Length(message = "Длина name должэна быть от 2 до 250", min = 2, max = 250)
    private String name;

    @NotBlank(message = "Поле 'email' должно быть заполнено")
    @Length(message = "Длина name должэна быть от 6 до 254", min = 6, max = 254)
    @Email
    private String email;

}
