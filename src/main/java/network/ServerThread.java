package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {

    Socket socket;
    ArrayList<Socket> list;
    InputStream is;
    OutputStream os;

    public ServerThread(Socket socket, ArrayList<Socket> list) {
        this.socket = socket;
        this.list = list;
    }

    @Override
    public void run() {
        try {
            while (true) {
                is = socket.getInputStream();
                byte[] b = new byte[40960];
                int len = is.read(b);
                if (len == -1) {
                    list.remove(socket);
                    break;
                }
                for (Socket socket : list) {
                    if (this.socket != socket) {
                        os = socket.getOutputStream();
                        os.write(b);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
