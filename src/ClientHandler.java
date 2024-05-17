import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            username = reader.readLine();
            Problem2_Server.addClient(username, this);

            sendMessage("Welcome to the chat, " + username + "!");

            String message;
            while ((message = reader.readLine()) != null) {
            	Problem2_Server.broadcastMessage(username + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	Problem2_Server.removeClient(username);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(String message) {
        writer.println(message);
    }
}
