package ru.practicum.shareit.validation.validator;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.validation.annotation.EndBeforeStart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndBeforeStartValidator implements ConstraintValidator<EndBeforeStart, BookingCreationDto> {
    @Override
    public boolean isValid(BookingCreationDto bookingCreationDto,
            ConstraintValidatorContext constraintValidatorContext) {
        if (bookingCreationDto.getEnd() == null || bookingCreationDto.getStart() == null) {
            return false;
        }
        if (bookingCreationDto.getEnd().isBefore(bookingCreationDto.getStart())) {
            return false;
        }
        return !bookingCreationDto.getEnd().equals(bookingCreationDto.getStart());
    }
}