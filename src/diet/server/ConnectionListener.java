package diet.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * There is a single ConnectionListener object per server. It listens on a specified port
 * for connections from the clients. On receiving a connection request, it instantiates a 
 * new {@link ParticipantConnection} object.
 * @author user
 */
public class ConnectionListener extends Thread {

    private ExperimentManager expM;
    ServerSocket serverSocket;
    int portNumber;
    boolean listening;



    public ConnectionListener(ExperimentManager experimentM, int portNumber) {
        expM = experimentM;
        serverSocket = null;
        listening = true;
        this.portNumber = portNumber;
    }

/**
 * Loop which listens for new connections on the specified port number. On establishing a new connection
 * it instantiates a new ObjectInputStream and ObjectOutputStream which it then associates with a 
 * ParticipantConnection object.
 * @author user
 */
    public void run(){
        try{
           serverSocket = new ServerSocket(portNumber);
        }
        catch (Exception e){
            System.err.println("Could not listen on port: "+portNumber);
            System.exit(-123);
        }
        while (listening) {
            try{
                Socket s2 =serverSocket.accept();
                ObjectInputStream in;
                ObjectOutputStream out;
                System.out.println("Server has received connection request");
                out = new ObjectOutputStream(s2.getOutputStream());
                in = new ObjectInputStream(s2.getInputStream());
                System.out.println("Server has created input and output streams");
                ParticipantConnection participconnection= new ParticipantConnection(in,out,expM);
                participconnection.start();

           }catch (Exception e){
                System.err.println("ConnectionListener: socket problem");
                System.err.println(e.getMessage());
                System.err.println(e.getStackTrace());
                //System.exit(-1);
           }
        }
    }


}
