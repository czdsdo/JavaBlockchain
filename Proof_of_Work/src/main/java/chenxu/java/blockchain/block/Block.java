package chenxu.java.blockchain.block;


import chenxu.java.blockchain.pow.PowResult;
import chenxu.java.blockchain.pow.ProofOfWork;
import chenxu.java.blockchain.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.time.Instant;

@Data
@AllArgsConstructor
public class Block {
    private String hash;//当前区块hash
    private String prevBlockHash;//上个区块的hash
    private String data;//当前区块内事务内容
    private long timeStamp;//当前时间戳
    private long height;//当前区块高度
    private long nonce;

    public static Block newBlock(String prevBlockHash, String data, long height) {
        Block block = new Block("", prevBlockHash, data, Instant.now().getEpochSecond(), height,0);
        ProofOfWork pow=ProofOfWork.newProofOfWork(block);
        PowResult powResult=pow.run();
        block.setHash(powResult.getHash());
        block.setNonce(powResult.getNonce());
        return block;
    }

    private static final String ZERO_HASH = Hex.encodeHexString(new byte[32]);

    public static Block newGensisBlock() {
        return Block.newBlock(ZERO_HASH, "Gensis Block", 0);
    }
}
