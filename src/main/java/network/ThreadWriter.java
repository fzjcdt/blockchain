package network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThreadWriter extends Thread {

    Socket socket;

    public ThreadWriter(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String str = sc.next();
                OutputStream os = socket.getOutputStream();
                os.write(str.getBytes());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}