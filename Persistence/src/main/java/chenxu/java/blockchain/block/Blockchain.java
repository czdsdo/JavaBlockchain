package chenxu.java.blockchain.block;

import chenxu.java.blockchain.util.ByteUtils;
import chenxu.java.blockchain.util.RocksDBUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

;

@Data
@AllArgsConstructor
public class Blockchain {
    private String lastBlockHash;

    public static Blockchain newBlockchain() {
        String lastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
        if (StringUtils.isBlank(lastBlockHash)) {
            Block genesisBlock = Block.newGensisBlock();
            lastBlockHash = genesisBlock.getHash();
            RocksDBUtils.getInstance().putBlock(genesisBlock);
            RocksDBUtils.getInstance().putLastBlockHash(lastBlockHash);
        }
        return new Blockchain(lastBlockHash);
    }

    public void addBlock(Block block) {
        RocksDBUtils.getInstance().putLastBlockHash(block.getHash());
        RocksDBUtils.getInstance().putBlock(block);
        this.lastBlockHash = block.getHash();
    }

    public void addBlock(String data) throws Exception {
        String LastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
        Block lastBlock = RocksDBUtils.getInstance().getBlock(lastBlockHash);
        if (StringUtils.isBlank(lastBlockHash)) {
            throw new Exception("还没有数据库，无法直接添加区块。");
        }
        this.addBlock(Block.newBlock(lastBlockHash, data, lastBlock.getHeight() + 1));
    }

    public class BlockchainIterator {
        private String currentBlockHash;

        private BlockchainIterator(String currentBlockHash) {
            this.currentBlockHash = currentBlockHash;
        }

        public boolean hasNext() {
            if (ByteUtils.ZERO_HASH.equals(currentBlockHash)) {
                return false;
            }
            Block lastBlock = RocksDBUtils.getInstance().getBlock(currentBlockHash);
            if (lastBlock == null)
                return false;
            if (ByteUtils.ZERO_HASH.equals(lastBlock.getPrevBlockHash())) {
                return true;
            }
            return RocksDBUtils.getInstance().getBlock(lastBlock.getPrevBlockHash()) != null;
        }

        public Block next() {
            Block currentBlock = RocksDBUtils.getInstance().getBlock(currentBlockHash);
            if (currentBlock != null) {
                this.currentBlockHash = currentBlock.getPrevBlockHash();
                return currentBlock;
            }
            return null;
        }

        public BlockchainIterator getBlockchainIterator() {
            return new BlockchainIterator(lastBlockHash);
        }
    }
}

