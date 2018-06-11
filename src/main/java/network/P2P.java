package network;

import block.Block;
import block.BlockChain;
import block.BlockTransactions;
import log.LogUtil;
import transaction.Transaction;
import util.SerializeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;

public class P2P {

    private static final int MAX_LEN = 40960;
    // private static final String HOST = "127.0.0.1";
    private static final String HOST = "119.29.17.101";
    private static final int PORT = 9999;

    byte[] inBuff = new byte[MAX_LEN];

    public Thread receiveThread;

    private volatile static P2P instance;

    private Socket socket;

    private P2P() {
        init();
    }

    public static P2P getInstance() {
        if (instance == null) {
            synchronized (P2P.class) {
                if (instance == null) {
                    instance = new P2P();
                    LogUtil.Log(Level.INFO, "Init P2P...");
                    instance.start();
                }
            }
        }

        return instance;
    }

    private void init() {
        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveThread = new Thread(new Runnable() {
            public void run() {
                listen();
            }
        });
    }

    public void start() {
        if (receiveThread.isAlive()) {
            return;
        }
        LogUtil.Log(Level.INFO, "P2P start listening");
        receiveThread.start();
    }

    public void stop() throws IOException {
        try {
            socket.close();
            LogUtil.Log(Level.INFO, "P2P stop listening");
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    public void listen() {
        while (true) {
            try {
                InputStream is = socket.getInputStream();
                byte[] b = new byte[MAX_LEN];
                int len = is.read(b);

                Object obj = SerializeUtil.deserialize(b);
                if (obj == null)
                    continue;
                System.out.println(obj.getClass());
                if (obj instanceof Block) {
                    // 别的节点挖出了新节点
                    LogUtil.Log(Level.INFO, "Receive a block");
                    BlockChain.addBlock((Block) obj);
                } else if (obj instanceof List) {
                    // 别的节点的全链回复
                    LogUtil.Log(Level.INFO, "Receive a blockchain check");
                    BlockChain.receiveBlockChainHandle((List<Block>) obj);
                } else if (obj instanceof Transaction) {
                    BlockTransactions.addTransaction((Transaction) obj);
                } else if (obj instanceof String && "give me the blockchain".equals(obj)) {
                    // 别的节点的整条链请求
                    // 实际上是能拿到发过来的ip的, 用socket就好, 不用多播, 但用ip是否意味着不是匿名了?
                    // 这样做会稍微加重网络负担, 之后再改进
                    LogUtil.Log(Level.INFO, "Receive blockchain download request");
                    dispatchToALL(BlockChain.blockChain);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dispatchToALL(Object obj) {
        byte[] buf = SerializeUtil.serialize(obj);

        try {
            OutputStream os = socket.getOutputStream();
            os.write(buf);
            LogUtil.Log(Level.INFO, "P2P dispatch to other nodes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}