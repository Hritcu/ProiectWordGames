import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class Login extends JFrame {
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel nameOfGame;
    private JLabel welcomeMessage;
    private JLabel registrationTitle;
    private JLabel loginTitle;
    private JLabel usernameRegistration;
    private JLabel passwordRegistration;
    private JButton registerButton;
    private JLabel usernameLogin;
    private JLabel passwordLogin;
    private JButton loginButton;
    private JPanel mainPanel;
    private JTextField usernameRegistrationField;
    private JTextField usernameLoginField;
    private JPasswordField passwordLoginField;
    private JPasswordField passwordRegistrationField;
    private HashMap<String, String> usersRegistered = new HashMap<>();
    String serverAddress;
    static Scanner in;
    static PrintWriter out;

    public Login(String fazan) {
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("auth");
                out.println(usernameRegistrationField.getText());
                out.println(passwordRegistrationField.getText());
                String word=in.nextLine();
                if (word.toLowerCase().startsWith("ok")) {
                    usersRegistered.put(usernameRegistrationField.getText(), passwordRegistrationField.getText());
                    JOptionPane.showMessageDialog(null, "Contul a fost creat cu succes!");
                } else {
                    JOptionPane.showMessageDialog(null, "Acest user exista deja!");
                }

            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("log");
                out.println(usernameLoginField.getText());
                out.println(passwordLoginField.getText());
                String word=in.nextLine();
                if (word.toLowerCase().startsWith("ok")) { //JOptionPane.showMessageDialog(mainPanel,"Bine ai venit "+usernameLoginField.getText()+"!");
                    dispose();
                    Game game = new Game(usernameLoginField.getText(),in,out);
                    game.setVisible(true);
                } else
                    JOptionPane.showMessageDialog(mainPanel, "Date invalide!");
            }
        });


    }

    public static void main(String[] args) throws Exception {

        try {
            var socket = new Socket("localhost", 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            Login login = new Login("Fazan");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

