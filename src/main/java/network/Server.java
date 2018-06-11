package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ArrayList<Socket> list = new ArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        while (true) {
            Socket socket = server.accept();
            list.add(socket);
            new ServerThread(socket, list).start();
        }
    }
}
