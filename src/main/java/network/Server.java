package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private static List<Socket> list = new ArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        // 线程安全
        List<Socket> synList = Collections.synchronizedList(list);

        while (true) {
            Socket socket = server.accept();
            list.add(socket);
            new ServerThread(socket, synList).start();
        }
    }
}
