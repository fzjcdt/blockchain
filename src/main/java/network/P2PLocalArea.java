package network;

import block.Block;
import block.BlockChain;
import block.BlockTransactions;
import log.LogUtil;
import transaction.Transaction;
import util.SerializeUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.logging.Level;

public class P2PLocalArea {

    private static final String BROADCAST_IP = "230.0.0.1";
    private static final int BROADCAST_PORT = 3000;
    private static final int MAX_LEN = 4096;

    private MulticastSocket multiCastSocket = null;
    private InetAddress broadcastAddress = null;
    byte[] inBuff = new byte[MAX_LEN];
    private DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);

    public Thread receiveThread;

    private volatile static P2PLocalArea instance;

    private P2PLocalArea() {
        init();
    }

    public static P2PLocalArea getInstance() {
        if (instance == null) {
            synchronized (P2PLocalArea.class) {
                if (instance == null) {
                    instance = new P2PLocalArea();
                    LogUtil.Log(Level.INFO, "Init P2P...");
                    instance.start();
                }
            }
        }

        return instance;
    }

    private void init() {
        try {
            multiCastSocket = new MulticastSocket(BROADCAST_PORT);
            broadcastAddress = InetAddress.getByName(BROADCAST_IP);
            multiCastSocket.joinGroup(broadcastAddress);
            multiCastSocket.setLoopbackMode(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveThread = new Thread(new Runnable() {
            public void run() {
                try {
                    listen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            receiveThread.interrupt();
            multiCastSocket.close();
            LogUtil.Log(Level.INFO, "P2P stop listening");
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while (true) {
            try {
                multiCastSocket.receive(inPacket);
                Object obj = SerializeUtil.deserialize(inBuff);
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
                } else {
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
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        datagramPacket.setAddress(broadcastAddress);
        datagramPacket.setPort(BROADCAST_PORT);
        try {
            multiCastSocket.send(datagramPacket);
            LogUtil.Log(Level.INFO, "P2P dispatch to other nodes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}