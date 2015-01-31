package org.asuki.bv;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PastValidationTest {

    private ValueHolder valueHolder;

    @BeforeMethod
    public void setup() {
        valueHolder = new ValueHolder();
    }

    @Test(dataProvider = "data")
    public void testValidate(LocalDate date, LocalDateTime dateTime,
            int expected) {

        valueHolder.setDate(date);
        valueHolder.setDateTime(dateTime);

        Set<ConstraintViolation<ValueHolder>> violations = validate(valueHolder);

        assertThat(violations.size(), is(expected));

        for (ConstraintViolation<ValueHolder> constraintViolation : violations) {
            assertThat(constraintViolation.getMessage(),
                    is("Date must be the past"));
        }
    }

    @DataProvider
    public Object[][] data() {
        // @formatter:off
        return new Object[][] {
            { null, null, 0 },
            { LocalDate.now().minusDays(1), LocalDateTime.now().minusDays(1), 0 },
            { LocalDate.now(),              LocalDateTime.now(),              2 },
            { LocalDate.now().plusDays(1),  LocalDateTime.now().plusDays(1),  2 }, 
        };
        // @formatter:on
    }

    private Set<ConstraintViolation<ValueHolder>> validate(ValueHolder target) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ValueHolder>> violations = validator
                .validate(target);
        return violations;
    }

}
