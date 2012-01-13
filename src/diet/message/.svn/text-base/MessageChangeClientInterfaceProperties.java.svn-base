package diet.message;

import java.io.Serializable;


public class MessageChangeClientInterfaceProperties extends Message implements Serializable {

    private int windowNumber;
   
    
    static public int disableRETURNANDSEND_enableTEXTENTRY = 1;
    static public int enableRETURNANDSEND_enableTEXTENTRY =2;
    static public int clearTextEntryField =3;
    static public int clearMainTextWindow =4;
    static public int disableTextEntry = 5;
    static public int enableTextEntry =6;
    
    private int interfaceproperties;

    public MessageChangeClientInterfaceProperties(int windowNumber, int interfaceproperties) {
        super("server", "server");
        this.interfaceproperties=interfaceproperties;
    }


    
    public int getWindowNumber(){
        return windowNumber;
    }

    public int getInterfaceproperties() {
        return interfaceproperties;
    }

    

}
