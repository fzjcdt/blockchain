package network;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ThreadReader extends Thread {

    Socket socket;

    public ThreadReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {

                InputStream is = socket.getInputStream();
                byte[] b = new byte[1024];
                int len = is.read(b);
                String str = new String(b, 0, len);
                System.out.println(str);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}