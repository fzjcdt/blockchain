package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class ServerThread extends Thread {

    Socket socket;
    List<Socket> list;
    InputStream is;
    OutputStream os;

    public ServerThread(Socket socket, List<Socket> list) {
        this.socket = socket;
        this.list = list;
    }

    @Override
    public void run() {
        try {
            while (true) {
                is = this.socket.getInputStream();
                byte[] b = new byte[409600];
                int len = is.read(b);
                if (len == -1) {
                    list.remove(socket);
                    break;
                }

                Iterator<Socket> it = list.iterator();
                while (it.hasNext()) {
                    Socket s = it.next();
                    try {
                        if (!(s.equals(this.socket))) {
                            os = s.getOutputStream();
                            os.write(b);
                        }
                    } catch (Exception e) {
                        list.remove(s);
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            list.remove(socket);
            e.printStackTrace();
        }
    }

}
