package personal.phuc.expense.security.user.mail_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import personal.phuc.expense.security.user.registration.model.RegistrationContent;

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


    public void send(Mail mail) throws MessagingException {
        try {
            MimeMessage mimeMessage = getMimeMessage(mail);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            throw new MessagingException("Server unexpected response");
        }

    }

    private MimeMessage getMimeMessage(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        setMimeMessageHelper(mimeMessage,mail);
        return mimeMessage;
    }

    private void setMimeMessageHelper(MimeMessage mimeMessage, Mail mail) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom(fromEmail);
        messageHelper.setTo(mail.getTo());
        messageHelper.setSubject(mail.getSubject());
        messageHelper.setText(mail.getContent(), true);
    }


    public String registrationContent(RegistrationContent registrationContent) {
        String userId = "&userId=" + registrationContent.getUserId();
        return "<div> <a target=\"_blank\" href=" +
                clientUrl + registrationContent.getVerifyToken() + userId + ">Verify Here</a></div>" +
                "<div>The token will be expired within " +
                registrationContent.getExpirationTime() +
                " minutes</div>";
    }
    public String passwordResetContent(String random){
        return "<div> Your Verification code : "+random +" </div>";
    }


}
