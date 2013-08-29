package parse;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Email {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    private static final String SMTP_AUTH_USER = "ruregisteredbot@gmail.com";
    private static final String SMTP_AUTH_PWD  = ""; //Until I implement serverside smtp will not commit with actual pass


    public static void deploy(ArrayList<TrackedCourse> winners) throws Exception{
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        // props.put("mail.smtps.quitwait", "false");

        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("You have new open courses");
        Multipart multipart = new MimeMultipart();
        BodyPart header = new MimeBodyPart();
        header.setContent("Hello, RURegisteredBot here. The following classes have opened: ","text/plain"); 
        multipart.addBodyPart(header);
        InternetAddress test = new InternetAddress("ruregisteredbot@gmail.com"); //Blaaaah
        message.setSender(test);
        for(int i =0;i<winners.size();i++){
        	BodyPart x =new MimeBodyPart();
        	x.setContent("\n"+winners.get(i).course + ": Section " +winners.get(i).section,"text/plain");
        	multipart.addBodyPart(x);
        }
        BodyPart link = new MimeBodyPart();
        link.setContent("\n"+"You can register courses at http://webreg.rutgers.edu\n" +
        		"Questions? Want to tell me my code is trash? Email raltcc@gmail.com","text/plain");
        multipart.addBodyPart(link);

        
        message.setContent(multipart);

        message.addRecipient(Message.RecipientType.TO,
             new InternetAddress("raltcc@gmail.com"));

        transport.connect
          (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}