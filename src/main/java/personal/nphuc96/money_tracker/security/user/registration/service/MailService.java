package personal.nphuc96.money_tracker.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
@Log4j2
public class MailService {

    private final JavaMailSender javaMailSender;
    @Value("${string.mail.address}")
    private String fromEmail;
    @Value("${string.client.url}")
    private String clientUrl;

    public void send(String toEmail, String verifyToken, int expirationTime) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            setMimeMessageHelper(toEmail, verifyToken, expirationTime, mimeMessage);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.info("Error sending confirm email");
            throw new MessagingException("Error sending confirm email");
        }

    }

    private void setMimeMessageHelper(String toEmail, String verifyToken, int expirationTime, MimeMessage mimeMessage) throws MessagingException {
        String subject = "Please verify your registration";
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom(fromEmail);
        messageHelper.setTo(toEmail);
        messageHelper.setSubject(subject);
        String content = buildContent(toEmail, verifyToken, expirationTime);
        messageHelper.setText(content, true);

    }

    private String buildContent(String email, String verifyToken, int expirationTime) {
        String toEmail = "&email=" + email;
        return "<div> <a href=" + clientUrl + verifyToken + toEmail + ">Verify Here</a></div>" +
                "<div>The token will be expired within " + expirationTime + " minutes</div>";
    }

}
