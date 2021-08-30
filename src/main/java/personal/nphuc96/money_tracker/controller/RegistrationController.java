package personal.nphuc96.money_tracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import personal.nphuc96.money_tracker.security.user.registration.RegistrationRequest;
import personal.nphuc96.money_tracker.security.user.registration.RegistrationServices;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationServices registrationServices;

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(validatedMethod(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            registrationServices.register(request);
        } catch (MessagingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.ok("A confirmation email has been sent to your email");
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token,
                                     @RequestParam("email") String email) {
        registrationServices.confirmToken(token, email);

        return ResponseEntity.ok("confirmed");
    }

    private Map<String, String> validatedMethod(BindingResult result) {
        Map<String, String> fieldError = new HashMap<>();
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            fieldError.put(error.getField(), error.getDefaultMessage());
        }
        return fieldError;
    }
}
