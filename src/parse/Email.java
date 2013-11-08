package parse;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;


public class Email {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    private static final String SMTP_AUTH_USER = "ruregisteredbot@gmail.com";
    private static final String SMTP_AUTH_PWD  = "";

   @Deprecated
    public static void deploy(ArrayList<TrackedCourse> winners, String email) throws Exception{
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
        InternetAddress test = new InternetAddress("ruregisteredbot@gmail.com");
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
             new InternetAddress(email));

        transport.connect
          (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
public static void MandrillDeploy(ArrayList<TrackedCourse> winners, String email) throws IOException {
		
		String baseurl = "https://mandrillapp.com/api/1.0/messages/send.json";
		URL obj = new URL(baseurl);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		String header = "Hello, RURegisteredBot here. The following classes have opened: ";
    	String text =new String();
    	String footer = "\n"+"You can register courses at http://webreg.rutgers.edu\n" +
        		"Questions? Want to tell me my code is trash? Email raltcc@gmail.com";
		for(TrackedCourse i:winners){
        	text = text.concat("\n"+i.course + ": Section " +i.section);
        }
		MandrillBody body = new MandrillBody();
		String api = "GHYOkzuWZK66ius-AKAxGg";
	
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		body.key=api;
		body.message.from_email="ruregisteredbot@gmail.com";
		body.message.from_name="RURegistered Bot";
		body.message.subject="You have new open courses!";
		body.message.text=header+text+footer;
		To emailbody = new To();
		emailbody.email= email;
		body.message.to.add(emailbody);
		Gson gson = new Gson();
		String json = gson.toJson(body);
		String urlParameters = json;
		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
	}
}