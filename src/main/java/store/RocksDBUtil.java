package store;

import org.rocksdb.RocksDB;

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
}
