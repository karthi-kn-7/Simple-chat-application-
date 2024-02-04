//package CHAT;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

class ServerChat extends JFrame implements ActionListener{
    JLabel title ,status;
    JButton send;
    JTextArea ta;
    JTextField tf;
    ObjectInputStream in;
    ObjectOutputStream out;
    ServerSocket ss;
    Socket s;

    ServerChat(){
        setSize(500, 700);
        setTitle("SERVER");
        setLayout(null);
        setVisible(true);

        title=new JLabel("server message");
        status=new JLabel();
        ta=new JTextArea();
        tf=new JTextField();
        send=new JButton("send");

        title.setBounds(10, 0,190,25);
        status.setBounds(10,title.getHeight()+5, 190, 25);
        ta.setBounds(10, status.getHeight()+10+title.getHeight(), 480, 500);
        tf.setBounds(10,ta.getHeight()+5+status.getHeight()+10+title.getHeight(), 350, 25);
        send.setBounds(355, ta.getHeight()+5+status.getHeight()+10+title.getHeight(), 90, 25);
        status.setText("waiting to connect...");
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
                String msg="server : "+tf.getText()+'\n';
                ta.append(msg);
                out.writeObject(msg);
                out.flush();
                tf.setText("");
            }
            catch(Exception ee){
                ta.append("unable to send ...\n");
            }
        }
    }
    void startchat(){
        try{
            ss=new ServerSocket(5678);
            while(true){
                s=ss.accept();
                status.setText("client connected : " + s.getInetAddress());
                try{
                    in=new ObjectInputStream(s.getInputStream());
                    out=new ObjectOutputStream(s.getOutputStream());
                    while(true){
                        String msg=(String)in.readObject();
                        ta.append(msg);
                    }
                }catch(Exception ss){
                    status.setText("waiting to connect...");
                    System.out.println("instream outstream : " + ss);
                }
            }
        }catch(Exception ee){
            System.out.println("server startchat : " + ee);
        }finally{
           try{
            s.close();
            ss.close();
            in.close();
            out.close();
           }
           catch(Exception eee){
            System.out.println("closing : " + eee);
           }
        }
    }
}

public class Server {
    public static void main(String[] args) {
        ServerChat s=new ServerChat();
        s.startchat();
    }
}
