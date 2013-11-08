package parse;
import java.util.ArrayList;


public class MandrillBody {
class Message{
	String html;
	String text;
	String subject;
	String from_email;
	String from_name;
	ArrayList<To> to = new ArrayList<To>();

}

public String key;
public Message message = new Message();



}
