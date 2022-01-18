package socketProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client extends JFrame implements ActionListener {

    JPanel topPanel;
    JTextField typingField;
    JButton sendButton;
    static JPanel msgDisplayArea;
    static JFrame mainFrame = new JFrame();

    static Box vertical = Box.createVerticalBox();
    static JPanel right;
    static JLabel l2;
    static JPanel p2;

    static JPanel left;
    static JPanel receivePanel;


    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;

    Boolean typing;

    Client(){
        topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(new Color(7, 94, 84));
        topPanel.setBounds(0, 0, 450, 70);
        mainFrame.add(topPanel);

        ImageIcon cross_mark = new ImageIcon(ClassLoader.getSystemResource("com/socket/img/cross_mark.png"));
        Image cross_mark2 = cross_mark.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon cross_mark3 = new ImageIcon(cross_mark2);
        JLabel cross_markLabel = new JLabel(cross_mark3);
        cross_markLabel.setBounds(10, 25, 20, 20);
        topPanel.add(cross_markLabel);

        cross_markLabel.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });

        ImageIcon profileImg = new ImageIcon(ClassLoader.getSystemResource(randomIcon()));
        Image profileImg2 = profileImg.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        ImageIcon profileImg3 = new ImageIcon(profileImg2);
        JLabel profileImgLabel = new JLabel(profileImg3);
        profileImgLabel.setBounds(40, 5, 60, 60);
        topPanel.add(profileImgLabel);


        ImageIcon sunImg1 = new ImageIcon(ClassLoader.getSystemResource("com/socket/img/sun.png"));
        Image sunImg2 = sunImg1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon sunImg3 = new ImageIcon(sunImg2);
        JLabel sunLabel = new JLabel(sunImg3);
        sunLabel.setBounds(410, 20, 25, 25);
        topPanel.add(sunLabel);

        sunLabel.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                msgDisplayArea.setBackground(new Color(238, 238, 238));
                mainFrame.getContentPane().setBackground(new Color(238, 238, 238));
                right.setBackground(new Color(238, 238, 238));
                l2.setBackground(new Color(238, 238, 238));
                p2.setBackground(new Color(238, 238, 238));


                left.setBackground(new Color(238, 238, 238));
                receivePanel.setBackground(new Color(238, 238, 238));
            }
        });


        ImageIcon moonImg = new ImageIcon(ClassLoader.getSystemResource("com/socket/img/moon.png"));
        Image moonImg2 = moonImg.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon moonImg3 = new ImageIcon(moonImg2);
        JLabel moonLabel = new JLabel(moonImg3);
        moonLabel.setBounds(380, 20, 25, 25);
        topPanel.add(moonLabel);

        moonLabel.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){

                msgDisplayArea.setBackground(new Color(60, 63, 65));
                mainFrame.getContentPane().setBackground(new Color(43, 43, 43));
                right.setBackground(new Color(60, 63, 65));
                l2.setBackground(new Color(60, 63, 65));
                p2.setBackground(new Color(60, 63, 65));


                left.setBackground(new Color(60, 63, 65));
                receivePanel.setBackground(new Color(60, 63, 65));

            }
        });


        JLabel nameLabel = new JLabel("Server");
        nameLabel.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(110, 15, 100, 18);
        topPanel.add(nameLabel);


        JLabel activityLabel = new JLabel("Active Now");
        activityLabel.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        activityLabel.setForeground(Color.WHITE);
        activityLabel.setBounds(110, 35, 100, 20);
        topPanel.add(activityLabel);

        Timer t = new Timer(1, new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(!typing){
                    activityLabel.setText("Active Now");
                }
            }
        });

        t.setInitialDelay(2000);

        msgDisplayArea = new JPanel();
        msgDisplayArea.setBounds(5, 75, 440, 570);
        msgDisplayArea.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        mainFrame.add(msgDisplayArea);



        JScrollPane sp = new JScrollPane(msgDisplayArea);
        sp.setBounds(5, 75, 440, 570);
        mainFrame.add(sp);


        typingField = new JTextField();
        typingField.setBounds(5, 655, 310, 40);
        typingField.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        mainFrame.add(typingField);

        typingField.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
                activityLabel.setText("typing...");

                t.stop();

                typing = true;
            }

            public void keyReleased(KeyEvent ke){
                typing = false;

                if(!t.isRunning()){
                    t.start();
                }
            }
        });

        sendButton = new JButton("Send");
        sendButton.setBounds(320, 655, 123, 40);
        sendButton.setBackground(new Color(7, 94, 84));
        sendButton.setForeground(Color.BLACK);
        sendButton.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        sendButton.addActionListener(this);
        mainFrame.add(sendButton);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setBackground(Color.WHITE);
        mainFrame.setLayout(null);
        mainFrame.setSize(450, 730);
        mainFrame.setLocation(2300, 300);
        mainFrame.setUndecorated(true);
        mainFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent ae){

        try{
            String out = typingField.getText();

            p2 = formatLabel(out);

            msgDisplayArea.setLayout(new BorderLayout());

            right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            msgDisplayArea.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);
            typingField.setText("");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static JPanel formatLabel(String out){
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));

        JLabel l1 = new JLabel("<html><p style = \"width : 150px\">"+out+"</p></html>");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        l1.setBackground(new Color(37, 211, 102));
        l1.setOpaque(true);
        l1.setBorder(new EmptyBorder(15,15,15,50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        l2 = new JLabel();
        l2.setText(sdf.format(cal.getTime()));

        p3.add(l1);
        p3.add(l2);
        return p3;
    }

    public static void main(String[] args){
        new Client().mainFrame.setVisible(true);

        try{

            s = new Socket("LocalHost", 1219);
            din  = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            String msginput = "";

            while(true){
                msgDisplayArea.setLayout(new BorderLayout());
                msginput = din.readUTF();
                receivePanel = formatLabel(msginput);
                left = new JPanel(new BorderLayout());
                left.add(receivePanel, BorderLayout.LINE_START);

                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                msgDisplayArea.add(vertical, BorderLayout.PAGE_START);
                mainFrame.validate();
            }

        }catch(Exception e){}
    }

    public static String randomIcon(){

        int num = (int) Math.ceil((Math.random()*8));

        switch (num){
            case 1:
                return "com/socket/img/cat.png";

            case 2:
                return "com/socket/img/dog.png";

            case 3:
                return "com/socket/img/monkey.png";

            case 4:
                return "com/socket/img/pig.png";

            case 5:
                return "com/socket/img/frog.png";
            case 6:
                return "com/socket/img/koala.png";
            case 7:
                return "com/socket/img/fox.png";
            case 8:
                return "com/socket/img/panda.png";


        }

        return null;
    }
}
