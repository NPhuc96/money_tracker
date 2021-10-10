package personal.phuc.expense.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import personal.phuc.expense.security.user.password_reset.PasswordResetServices;
import personal.phuc.expense.security.user.password_reset.model.PasswordResetRequest;
import personal.phuc.expense.util.ErrorUtil;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/password")
@AllArgsConstructor
public class PasswordResetController {

    private final PasswordResetServices passwordResetServices;
    private final ErrorUtil errorUtil;

    @PostMapping("/request")
    public ResponseEntity<?> requestReset(@RequestParam("email") String email) {
        try {
            passwordResetServices.requestReset(email);
        } catch (MessagingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.ok("Verification code has been sent");
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("code") String code,
                                     @RequestParam("email") String email) {
        passwordResetServices.confirm(code,email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody @Valid PasswordResetRequest request,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorUtil.errors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        passwordResetServices.reset(request);
        return ResponseEntity.ok("Successful reset");
    }
}
