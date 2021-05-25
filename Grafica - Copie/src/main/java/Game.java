import javax.swing.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Game extends JFrame{
    private JPanel mainGamePanel;
    private JPanel topGamePanel;
    private JLabel nameOfGame;
    private JLabel welcomeMessage;
    private JPanel bodyGamePanel;
    private JLabel randomLetterLabel;
    private JTextField recievedWord;
    private JTextField filterField;
    private JButton sendWordButton;
    private JComboBox resultFilter;
    private JButton filterWords;
    private JPanel list;
    private JTextField sendWord;
    private JLabel messageNumberOfUse;
    private JList list1;
    private static int number=5;
    public Game(String name, Scanner in, PrintWriter out){
        super("Fazan");
        this.setContentPane(mainGamePanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        welcomeMessage.setText("Bine ai venit "+name+" !");
        Random r = new Random();
        char c = (char)(r.nextInt(26) + 'a');
        String text="";
        text+=c;
        messageNumberOfUse.setText("Mai ai "+number+" încercări!");

        filterWords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(number>0) {
//
                    out.println("ajutor");
                    out.println(filterField.getText());
                    resultFilter.removeAllItems();
                    String word =in.nextLine();
                    resultFilter.addItem(word);
                    int nr=0;
                    while(word!=null)
                    {
                        nr++;
                        if(nr>9)
                            break;
                        System.out.println(word);
                        word=in.nextLine();
                        resultFilter.addItem(word);

                    }
                    System.out.println("gata");
                    number--;
                    messageNumberOfUse.setText("Mai ai "+number+" încercări!");

                }
                else
                    JOptionPane.showMessageDialog(null,"Numar de incercari epuizat");



            }
        });

        sendWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("incercare");
                String primit=sendWord.getText();
                out.println(sendWord.getText());
                String word=in.nextLine();
                if(word.startsWith("gresit"))
                {number=number-2;
                    messageNumberOfUse.setText("Mai ai "+number+" încercări!");
                    recievedWord.setText("nu este corect");
                }
                else
                {recievedWord.setText(word);
                    randomLetterLabel.setText("Cuvantul trebuie sa inceapa cu: "+word.substring(word.length()-2,word.length()));}
                if(number<0)
                    JOptionPane.showMessageDialog(null,"Numar de incercari epuizat");


            }
        });


        resultFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(resultFilter.getSelectedItem()!=null)
                {sendWord.setText(resultFilter.getSelectedItem().toString());
                }
                System.out.println(e.getActionCommand());

            }
        });
    }

}
