package ru.practicum.ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCategory {

    @NotBlank(message = "Поле 'name' должно быть заполнено")
    @Length(message = "Длина name должна составлять до 50",max = 50)
    private String name;
}
