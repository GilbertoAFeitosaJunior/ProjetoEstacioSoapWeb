package mobi.stos.projetoestacio.util;

import java.io.IOException;
import java.util.Properties;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;

/**
 * Código resolvido através de http://code.google.com/p/simple-java-mail/
 *
 * @author Weibson Santos (Google)
 */
public class MailService {

    private String from;
    private String name;
    private String user;
    private String password;
    private String smtp;
    private int port;

    public MailService() throws Exception {
        try {
            Properties props = new Properties();
            props.load(this.getClass().getClassLoader().getResourceAsStream("mail.properties"));

            this.name = props.getProperty("mail.name");
            this.user = props.getProperty("mail.user");
            this.password = props.getProperty("mail.password");
            this.smtp = props.getProperty("mail.smtp");
            this.port = Integer.parseInt(props.getProperty("mail.smtp_port"));

        } catch (IOException e) {
            throw new Exception("O arquivo de configuração do email não foi encontrao!");
        }
    }

    public void enviarEmail(Email email) throws Exception {
        email.setFromAddress(this.name, this.user);
        Mailer mailer = new Mailer(this.smtp, this.port, this.user, this.password, TransportStrategy.SMTP_PLAIN);
        mailer.setDebug(true);
        mailer.sendMail(email);
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
