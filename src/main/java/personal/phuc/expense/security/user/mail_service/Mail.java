package personal.phuc.expense.security.user.mail_service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mail {
    private String to;

    private String content;

    private String subject;
}
