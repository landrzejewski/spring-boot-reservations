package pl.training.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {

    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext context) {
        if (dateRange.startDate() == null || dateRange.endDate() == null) {
            return true;
        }
        return dateRange.endDate().isAfter(dateRange.startDate());
    }

}
