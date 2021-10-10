package personal.phuc.expense.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorUtil {

    public Map<String, String> errors(BindingResult result) {
        Map<String, String> fieldError = new HashMap<>();
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            fieldError.put(error.getField(), error.getDefaultMessage());
        }
        return fieldError;
    }
}
