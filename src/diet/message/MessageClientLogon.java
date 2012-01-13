package diet.message;
import java.io.Serializable;

public class MessageClientLogon extends Message implements Serializable {


    public MessageClientLogon(String email, String username) {
        super(email, username);

    }

    public String getMessageClass(){
        return "MessageClientLogon";
    }


}
