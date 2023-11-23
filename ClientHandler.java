import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private BufferedReader dis;
    private PrintWriter dos;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCommand(String command) {
        switch (command.toUpperCase()) {
            case "DATE":
                sendDate();
                break;
            case "TIME":
                sendTime();
                break;
            case "EXIT":
                System.out.println("Client is leaving.");
                break;
            default:
                dos.println("Invalid command. Try DATE, TIME, or EXIT.");
        }
    }

    public void sendDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        dos.println("Server Date: " + date);
    }

    public void sendTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(Calendar.getInstance().getTime());
        dos.println("Server Time: " + time);
    }

    @Override
    public void run() {
        try {
            String command;
            while ((command = dis.readLine()) != null) {
                if (command.equalsIgnoreCase("EXIT")) {
                    System.out.println("Client is leaving.");
                    break;
                }
                handleCommand(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
                dos.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
