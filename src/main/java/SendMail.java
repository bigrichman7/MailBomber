import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SendMail {
    String login;
    String password;
    String from;
    String to;
    String subject;
    String text;
    Properties props;

    public SendMail(Properties props, String subject, String text) {
        this.props = props;
        this.login = props.getProperty("mail.smtp.login");
        this.password = props.getProperty("mail.smtp.password");
        this.from = props.getProperty("mail.smtp.from");
        this.to = props.getProperty("mail.smtp.to");
        this.subject = subject;
        this.text = text;
        sendMail();
    }

    public void sendMail() {

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
