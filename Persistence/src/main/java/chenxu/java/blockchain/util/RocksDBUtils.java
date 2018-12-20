package chenxu.java.blockchain.util;

import chenxu.java.blockchain.block.Block;

import java.util.HashMap;
import java.util.Map;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
public class RocksDBUtils {
    private static final String DB_FILE = "blockchain.db";
    private static final String BLOCKS_BUCKET_KEY = "blocks";
    private static final String LAST_BLOCK_KEY = "1";

    public static RocksDBUtils getInstance() {
        return Singleton.INSTANCE.getinstance();
    }

    private enum Singleton {
        INSTANCE;
        private RocksDBUtils singleton;

        Singleton() {
            singleton = new RocksDBUtils();

        }

        public RocksDBUtils getinstance() {
            return singleton;
        }
    }

    private RocksDBUtils() {
        openDB();
        initBlockBucket();
    }

    private RocksDB db;
    private Map<String, byte[]> blockBucket;

    private void openDB() {
        try {
            db = RocksDB.open(DB_FILE);
        } catch (RocksDBException e) {
            throw new RuntimeException("打开数据库失败。", e);
        }
    }

    private void initBlockBucket() {
        try {
            byte[] blockBucketKey = SerializeUtils.serialize(BLOCKS_BUCKET_KEY);
            byte[] blockBucketBytes = db.get(blockBucketKey);
            if (blockBucketBytes != null) {
                blockBucket = (Map) SerializeUtils.deserialize(blockBucketBytes);
            } else {
                blockBucket = new HashMap<>();
                db.put(blockBucketKey, SerializeUtils.serialize(blockBucketBytes));
            }
        } catch (RocksDBException e) {
            throw new RuntimeException("初始化block的bucket失败.!!", e);
        }
    }

    public void putBlock(Block block) {
        try {
            blockBucket.put(block.getHash(), SerializeUtils.serialize(block));
            db.put(SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blockBucket));
        } catch (RocksDBException e) {
            throw new RuntimeException("存储区块失败。", e);
        }
    }

    public Block getBlock(String blockHash) {
        return (Block) SerializeUtils.deserialize(blockBucket.get(blockHash));
    }

    public void putLastBlockHash(String tipBlockHash) {
        try {
            blockBucket.put(LAST_BLOCK_KEY, SerializeUtils.serialize((tipBlockHash)));
            db.put(SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blockBucket));
        } catch (RocksDBException e) {
            throw new RuntimeException("数据库存储最新区块hash失败。", e);
        }
    }

    public String getLastBlockHash() {
        byte[] lastBlockHashBytes = blockBucket.get(LAST_BLOCK_KEY);
        if (lastBlockHashBytes != null) {
            return (String) SerializeUtils.deserialize(lastBlockHashBytes);
        }
        return "";
    }
    public void closeDB(){
        try{
            db.close();
        }catch (Exception e){
            throw new RuntimeException("数据库关闭失败。",e);
        }
    }
}
