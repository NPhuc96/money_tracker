package personal.phuc.expense.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import personal.phuc.expense.security.user.registration.RegistrationServices;
import personal.phuc.expense.security.user.registration.model.RegistrationRequest;
import personal.phuc.expense.util.ErrorUtil;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationServices registrationServices;
    private final ErrorUtil errorUtil;

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorUtil.errors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            registrationServices.register(request);
        } catch (MessagingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.ok("A confirmation email has been sent to your email");
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token,
                                     @RequestParam("userId") Integer userId) {
        registrationServices.confirmToken(token, userId);
        return ResponseEntity.ok("confirmed");
    }

}
