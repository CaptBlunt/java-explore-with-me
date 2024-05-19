package ru.practicum.ewm.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateNowPlusTwoHoursValidator implements ConstraintValidator<DateNowPlusTwoHours, LocalDateTime> {

    @Override
    public void initialize(DateNowPlusTwoHours constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value != null) {
            LocalDateTime date = LocalDateTime.now().plusHours(2);
            return value.isAfter(date) || value.isEqual(date);
        }
        return true;
    }
}
