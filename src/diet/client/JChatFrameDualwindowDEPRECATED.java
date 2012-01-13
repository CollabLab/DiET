package diet.client;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class JChatFrameDualwindowDEPRECATED extends JFrame {


ConnectionToServer serverConnection;
    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea jTextArea1 = new JTextArea();

    public JChatFrameDualwindowDEPRECATED() throws HeadlessException {
            super();
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JChatFrameDualwindowDEPRECATED(ConnectionToServer serverConnection) throws HeadlessException {
        super();
        this.serverConnection=serverConnection;
    }


    public JChatFrameDualwindowDEPRECATED(GraphicsConfiguration gc) {
        super(gc);
    }

    public JChatFrameDualwindowDEPRECATED(String title) throws HeadlessException {
        super(title);
    }

    public JChatFrameDualwindowDEPRECATED(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }

    public static void main(String[] args) {
        JChatFrameDualwindowDEPRECATED jchatframedualwindow = new JChatFrameDualwindowDEPRECATED();
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        jScrollPane1.setBounds(new Rectangle(11, 15, 454, 181));
        this.getContentPane().add(jScrollPane1);
        jScrollPane1.getViewport().add(jTextArea1);
        jTextArea1.setText("jTextArea1");
    }
}
