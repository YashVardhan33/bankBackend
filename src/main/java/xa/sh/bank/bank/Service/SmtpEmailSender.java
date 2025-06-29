package xa.sh.bank.bank.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class SmtpEmailSender implements EmailSender{

    private final JavaMailSender mailSender;

    @Autowired
    public SmtpEmailSender(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }



    @Override
    public void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,"UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody,true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to send mail",e);

        }
    }

}
