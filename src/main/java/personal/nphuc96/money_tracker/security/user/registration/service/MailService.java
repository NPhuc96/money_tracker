package personal.nphuc96.money_tracker.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import personal.nphuc96.money_tracker.security.user.registration.model.Sender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
@Log4j2
public class MailService {

    private final Sender sender;
    @Value("${string.mail.address}")
    private String emailAddress;
    @Value("${string.client.url}")
    private String clientUrl;

    public void send(String to, String verifyUrl) throws MessagingException {
        String subject = "Please verify your registration";
        try {
            JavaMailSender mailSender = sender.getJavaMailSender();
            log.info("Registration Username :" + sender.getUsername() + "  Password : " + sender.getPassword());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(emailAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            String content = buildContent(verifyUrl, emailAddress);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            log.info("Error sending confirm email");
            throw new MessagingException("Error sending confirm email");
        }

    }

    private String buildContent(String verifyUrl, String email) {
        String emailParam = "&email=" + email;
        return "<div>Please click the link to confirm your register :" + clientUrl + verifyUrl + emailParam + "</div>" +
                "<div>The token will be expired within 15 minutes</div>";
    }
}
