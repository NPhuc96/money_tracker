package personal.nphuc96.money_tracker.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import personal.nphuc96.money_tracker.security.user.registration.model.MailContent;

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

    public void send(String toEmail, String content) throws MessagingException {
        try {
            MimeMessage mimeMessage = getMimeMessage(toEmail, content);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.info("Error sending confirm email");
            throw new MessagingException("Error sending confirm email");
        }

    }

    private MimeMessage getMimeMessage(String toEmail, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        setMimeMessageHelper(mimeMessage, toEmail, content);
        return mimeMessage;
    }

    private void setMimeMessageHelper(MimeMessage mimeMessage, String toEmail, String content) throws MessagingException {
        String subject = "Please verify your registration";
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom(fromEmail);
        messageHelper.setTo(toEmail);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
    }


    public String buildContent(MailContent mailContent) {
        String to = "&email=" + mailContent.getTo();
        return "<div> <a href=" +
                clientUrl + mailContent.getVerifyToken() + to +
                ">Verify Here</a></div>" +
                "<div>The token will be expired within " +
                mailContent.getExpirationTime() +
                " minutes</div>";
    }

}
