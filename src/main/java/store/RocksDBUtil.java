package store;

import block.Block;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import util.SerializeUtil;

import java.util.HashMap;
import java.util.Map;

public class RocksDBUtil {
    private static final String DB_FILE = "blockchain.db";
    private static final String BLOCKS_BUCKET_KEY = "blocks";
    private static final String LAST_BLOCK_KEY = "1";

    private volatile static RocksDBUtil instance;

    public static RocksDBUtil getInstance() {
        if (instance == null) {
            synchronized (RocksDBUtil.class) {
                if (instance == null) {
                    instance = new RocksDBUtil();
                }
            }
        }
        return instance;
    }

    private RocksDB db;
    private Map<String, byte[]> blocksBucket;

    private RocksDBUtil() {
        openDB();
        initBlockBucket();
    }

    private void openDB() {
        try {
            db = RocksDB.open(DB_FILE);
        } catch (RocksDBException e) {
            throw new RuntimeException("Fail to open rocks db", e);
        }
    }

    private void initBlockBucket() {
        try {
            byte[] blockBucketKey = SerializeUtil.serialize(BLOCKS_BUCKET_KEY);
            byte[] blockBucketBytes = db.get(blockBucketKey);
            if (blockBucketBytes != null) {
                blocksBucket = (Map) SerializeUtil.deserialize(blockBucketBytes);
            } else {
                blocksBucket = new HashMap();
                db.put(blockBucketKey, SerializeUtil.serialize(blocksBucket));
            }
        } catch (RocksDBException e) {
            throw new RuntimeException("Fail to init block bucket", e);
        }
    }

    public void putLastBlockHash(String tipBlockHash) {
        try {
            blocksBucket.put(LAST_BLOCK_KEY, SerializeUtil.serialize(tipBlockHash));
            db.put(SerializeUtil.serialize(BLOCKS_BUCKET_KEY), SerializeUtil.serialize(blocksBucket));
        } catch (Exception e) {
            throw new RuntimeException("Fail to put last block hash", e);
        }
    }

    public String getLastBlockHash() {
        byte[] lastBlockHashBytes = blocksBucket.get(LAST_BLOCK_KEY);
        return lastBlockHashBytes == null ? "" : (String) SerializeUtil.deserialize(lastBlockHashBytes);
    }

    public void putBlock(Block block) {
        try {
            blocksBucket.put(block.getHash(), SerializeUtil.serialize(block));
            db.put(SerializeUtil.serialize(BLOCKS_BUCKET_KEY), SerializeUtil.serialize(blocksBucket));
        } catch (RocksDBException e) {
            throw new RuntimeException("Fail to put block", e);
        }
    }

    public Block getBlock(String blockHash) {
        return (Block) SerializeUtil.deserialize(blocksBucket.get(blockHash));
    }

    public void closeDB() {
        try {
            db.close();
        } catch (Exception e) {
            throw new RuntimeException("Fail to close db", e);
        }
    }
}
