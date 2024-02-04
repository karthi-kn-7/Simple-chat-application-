import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

class ClientChat extends JFrame implements ActionListener{

    JLabel title,status;
    JTextArea ta;
    JTextField tf;
    JButton send;
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket s;
    ClientChat(){
        setTitle("Client");
        setLayout(null);
        setSize(500, 700);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        title=new JLabel("Client message");
        status=new JLabel();
        ta=new JTextArea();
        tf=new JTextField();
        send=new JButton("send");

        title.setBounds(10, 0,190,25);
        status.setBounds(10,title.getHeight()+5, 190, 25);
        ta.setBounds(10, status.getHeight()+10+title.getHeight(), 480, 500);
        tf.setBounds(10,ta.getHeight()+5+status.getHeight()+10+title.getHeight(), 350, 25);
        send.setBounds(355, ta.getHeight()+5+status.getHeight()+10+title.getHeight(), 90, 25);
        status.setText("Attempting to connect...");
        add(title);
        add(ta);
        add(tf);
        add(send);
        add(status);

        send.addActionListener(this);

    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==send){
            try{
                String msg="client : " + tf.getText()+'\n';
                ta.append("client : " + tf.getText()+'\n');
                out.writeObject(msg);
                out.flush();
                tf.setText("");

            }catch(Exception ee){
                ta.append("unable to send...\n");
            }
        }
    }
    void startchat(){
        try{
            s=new Socket("localhost",5678);
            status.setText("Connected to : " + s.getInetAddress());
            out=new ObjectOutputStream(s.getOutputStream());
            in=new ObjectInputStream(s.getInputStream());
            while(true){
                String msg=(String)in.readObject(); 
                ta.append(msg);
            }
        }
        catch(Exception e){
            status.setText("Attempting to connect...");
            System.out.println("occured : " + e);
        }
        finally{
            try{
                s.close();
                in.close();
                out.close();
            }
            catch(Exception eee){
                System.out.println("closing : " + eee);
            }
        }

    }

}

public class Client {
    public static void main(String[] args) {
        ClientChat c=new ClientChat();
        c.startchat();
    }
}
