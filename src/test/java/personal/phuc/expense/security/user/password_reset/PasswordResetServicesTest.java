package personal.phuc.expense.security.user.password_reset;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import personal.phuc.expense.dao.user.AppUserDAO;
import personal.phuc.expense.dao.user.PasswordResetDAO;
import personal.phuc.expense.entity.user.AppUser;
import personal.phuc.expense.entity.user.PasswordResetCode;
import personal.phuc.expense.exception.BadRequestException;
import personal.phuc.expense.exception.ResourceNotFoundException;
import personal.phuc.expense.security.user.mail_service.Mail;
import personal.phuc.expense.security.user.mail_service.MailService;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class PasswordResetServicesTest {
    @Autowired
    private PasswordResetServices services;

    @MockBean
    private AppUserDAO appUserDAO;
    @MockBean
    private PasswordResetDAO passwordResetDAO;
    @MockBean
    private MailService mailService;

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "$argon2id$v=19$m=4096,t=3,p=1$dUhMS1VvalZNUmdkOXhGQw$pFer1fjk/kbQlm9ZdSjgmkpm5iueQBlyic+gDhIRUIk";
/*    private static final String NEW_PASSWORD = "$argon2id$v=19$m=4096,t=3,p=1$dlBnZm5JVEdMMjhjOERqQw$oeMpoCSO/vN08pGPgr7t+Q";*/
    private static final String CODE = "123456";
    private AppUser appUser = AppUser.builder()
            .password(PASSWORD)
            .email(EMAIL)
            .isEnabled(true)
            .isNonLocked(true)
            .build();
    private PasswordResetCode passwordResetCode = PasswordResetCode.builder()
            .code(CODE)
            .email(EMAIL)
            .isConfirmed(false)
            .build();


    @DisplayName("1. Successful Request")
    @Test
    public void successfulRequest() throws MessagingException {
        when(appUserDAO.save(appUser)).thenReturn(appUser);
        when(appUserDAO.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(appUser));
        services.requestReset(EMAIL);
        verify(mailService, times(1)).send(any(Mail.class));
    }

    @DisplayName("2. Failed Request")
    @Test
    public void failedRequest()  {
        when(appUserDAO.findByEmail(EMAIL).isEmpty())
                .thenThrow(ResourceNotFoundException.class);
    }

    @DisplayName("3. Successful Confirmation")
    @Test
    public void successfulConfirmation()  {
        when(appUserDAO.save(appUser)).thenReturn(appUser);
        when(passwordResetDAO.save(passwordResetCode)).thenReturn(passwordResetCode);
        when(passwordResetDAO.findByCodeAndEmail(CODE, EMAIL)).thenReturn(passwordResetCode);
        services.confirm(CODE, EMAIL);
        assertEquals(true, passwordResetCode.getIsConfirmed());
/*        int expected = passwordResetDAO.findByCodeAndEmail(CODE, EMAIL).hashCode();
        int actual = passwordResetCode.hashCode();
        log.info("expected : "+expected+" actual + "+actual);
        assertNotEquals(passwordResetDAO.findByCodeAndEmail(CODE, EMAIL).hashCode(), passwordResetCode.hashCode());*/

    }

    @DisplayName("4. Failed Confirmation")
    @Test
    public void failedConfirmation()  {
        when(passwordResetDAO.findByCodeAndEmail("123", "321"))
                .thenThrow(BadRequestException.class);
    }

}