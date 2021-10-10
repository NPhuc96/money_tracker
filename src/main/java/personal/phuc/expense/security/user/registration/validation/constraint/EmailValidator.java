package personal.phuc.expense.security.user.registration.validation.constraint;

import personal.phuc.expense.security.user.registration.validation.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean isValid(String emailInput, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(emailInput)) {
            return false;
        }
        return pattern.matcher(emailInput).matches();
    }
}
