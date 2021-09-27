package personal.nphuc96.money_tracker.security.user.registration.validation.constraint;

import personal.nphuc96.money_tracker.security.user.registration.validation.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, String> {

    private final String PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) {
            return false;
        }
        return pattern.matcher(value).matches();
    }
}
