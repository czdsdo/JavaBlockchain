package chenxu.java.blockchain.block;




import java.math.BigInteger;
import java.time.Instant;

import chenxu.java.blockchain.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
@Data
@AllArgsConstructor
public class Block {
    private String hash;//当前区块hash
    private String prevBlockHash;//上个区块的hash
    private String data;//当前区块内事务内容
    private long timeStamp;//当前时间戳
    private long height;//当前区块高度


    public static Block newBlock(String prevBlockHash, String data, long height){
        Block block=new Block("",prevBlockHash,data, Instant.now().getEpochSecond(),height);
        block.setHash();
        return block;
    }

    private void setHash() {
        byte[] prevBlockHashBytes={};
        if (StringUtils.isNoneBlank(this.getPrevBlockHash())){
            prevBlockHashBytes=new BigInteger(this.getPrevBlockHash(),16).toByteArray();
        }
        byte[] headers= ByteUtils.merge(prevBlockHashBytes,this.getData().getBytes(),
                ByteUtils.toBytes(this.getTimeStamp()));
        this.setHash(DigestUtils.sha256Hex(headers));

    }
    private static final String ZERO_HASH=Hex.encodeHexString(new byte[32]);
    public static Block newGensisBlock(){
        return  Block.newBlock(ZERO_HASH,"Gensis Block",0);
    }
}
