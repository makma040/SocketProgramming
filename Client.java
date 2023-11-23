import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket palinSocket;
    private BufferedReader stdIn;
    private BufferedReader socketIn;
    private PrintWriter socketOut;


    public static void main(String[] args) throws IOException {
        Client dc = new Client("127.0.0.1", 9090);
        dc.communicate();
    }

    public Client(String serverName, int portNumber) {
        try {
            palinSocket = new Socket(serverName, portNumber);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            socketIn = new BufferedReader(new InputStreamReader(palinSocket.getInputStream()));
            socketOut = new PrintWriter(palinSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }
    }

    public void communicate() {
        System.out.println("Enter a command: DATE, TIME, or EXIT");

        String command = "";
        try {
            while (true) {
                command = stdIn.readLine();

                socketOut.println(command);

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Closing client...");
                    break;
                }

                String response = socketIn.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stdIn.close();
                socketIn.close();
                socketOut.close();
                palinSocket.close();
            } catch (IOException e) {
                System.out.println("Closing error: " + e.getMessage());
            }
        }
    }
}
