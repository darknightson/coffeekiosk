package sample.coffeekiosk.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSendClient {

    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {
        log.info("fromEmail: {}, toEmail: {}, subject: {}, content: {}", fromEmail, toEmail, subject, content);
        return true;
    }
}
