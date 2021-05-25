import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Main {

    private Lock gameLock; // to lock game for synchronization
    private Condition otherPlayerConnected; // to wait for other player
    private Condition otherPlayerTurn; // to wait for other player's turn
    private static Set<String> names = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();
    private static List<String> ultim = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        var pool = Executors.newFixedThreadPool(500);
        try (var listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    private static class Handler extends Functii implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        private String parola;
        private String comanda;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            Connection con = DbConnection.getCon();
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    comanda = in.nextLine();
                    if (comanda.toLowerCase().startsWith("auth")) {
                        name = in.nextLine();
                        parola = in.nextLine();
                        PreparedStatement statement;
                        String getGenre = "SELECT * FROM conturi WHERE cont= ?";
                        try {
                            statement = con.prepareStatement(getGenre);
                            statement.setString(1, name);
                            ResultSet rs = statement.executeQuery();
                            int i = 0;
                            while (rs.next())
                                i++;

                            if (i == 1) {
                                out.println("nu");
                            } else {
                                String insert = "INSERT INTO conturi (cont,parola)" +
                                        " values (?,?)";
                                try {

                                    statement = con.prepareStatement(insert);
                                    statement.setString(1, name);
                                    statement.setString(2, parola);

                                    statement.executeUpdate();
                                    out.println("ok");

                                } catch (SQLException throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }
                    }
                    if (comanda.toLowerCase().startsWith("log")) {
                        name = in.nextLine();
                        parola = in.nextLine();
                        PreparedStatement statement;
                        String getGenre = "SELECT * FROM conturi WHERE cont= ? and parola =?";
                        try {
                            statement = con.prepareStatement(getGenre);
                            statement.setString(1, name);
                            statement.setString(2, parola);
                            ResultSet rs = statement.executeQuery();
                            int i = 0;
                            while (rs.next()) {
                                i++;
                            }
                            if (i != 0) {
                                out.println("ok");

                                break;
                            }

                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }

                while (true) {

                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("quit")) {
                        return;
                    }

                    if (input.toLowerCase().startsWith("ajutor")) {
                        input = in.nextLine();
                        List<String> primit = cautare(input, con);
                        int i = 0;
                        if (primit != null) {
                            for (String word : primit) {
                                System.out.println(word);
                                if (i < 10)
                                    out.println(word);
                                i++;
                            }
                        } else
                            out.println("nu");


                    } else {
                        input = in.nextLine();
                        System.out.println(input.substring(0, 2));
                        //System.out.println(ultim[nr].substring(ultim[nr].length() - 2, ultim[nr].length()));
                        if ((ultim.size() != 0)) {
                            System.out.println(ultim.get(ultim.size() - 1).substring(ultim.get(ultim.size() - 1).length() - 2, ultim.get(ultim.size() - 1).length()));
                            if (input.substring(0, 2).startsWith(ultim.get(ultim.size() - 1).substring(ultim.get(ultim.size() - 1).length() - 2, ultim.get(ultim.size() - 1).length()))) {
                                int i = validare(input, con);
                                String primit = new String();
                                if (i == 0)
                                    primit = "gresit";
                                else {
                                    primit = input;
                                    ultim.add(input);
                                }

                                out.println(primit);

                            } else {
                                out.println("gresit");

                                }

                            } else{
                                int i = validare(input, con);
                                String primit = new String();
                                if (i == 0)
                                    primit = "gresit";
                                else {
                                    primit = input;
                                    ultim.add(input);
                                }
                                out.println(primit);
                            }


                        }
//                    if (validare(input, con) == 0 || input.toLowerCase().startsWith("pass")) {
//                        System.out.println("mai incearca");
//                        System.out.println(ultim);
//                        String gasit = cautare( ultim.substring(ultim.length() - 2, ultim.length()), con);
//                        System.out.println(gasit);
//                        for (PrintWriter writer : writers) {
//                            writer.println("MESSAGE " + "trebuia:" + ": " + gasit);
//                        }
//                        ultim = ultim + gasit.substring(gasit.length() - 2, gasit.length());
//                    } else {
//                        System.out.println("bravo");
//                        ultim = ultim + input.substring(input.length() - 2, input.length());
//                    }

                    }

                } catch(Exception e){
                    System.out.println(e);
                } finally{
                    if (out != null) {
                        writers.remove(out);
                    }
                    if (name != null) {
                        System.out.println(name + " is leaving");
                        names.remove(name);
                        for (PrintWriter writer : writers) {
                            writer.println("MESSAGE " + name + " has left");
                        }
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }