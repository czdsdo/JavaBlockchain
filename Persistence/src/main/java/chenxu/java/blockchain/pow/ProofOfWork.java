package chenxu.java.blockchain.pow;

import chenxu.java.blockchain.block.Block;
import chenxu.java.blockchain.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@Slf4j
public class ProofOfWork {
    public static final int TARGET_BITS=16;//256位Hash前有16位0  难度目标
    private Block block;
    private BigInteger target;

    /**
     * 创建新的工作量证明对象
     * 对1左移256-TARGET_BITS 得到难度目标值
     * @param block
     * @return
     */
    public static ProofOfWork newProofOfWork(Block block){
        BigInteger targetValue=BigInteger.ONE.shiftLeft((256-TARGET_BITS));
        return new ProofOfWork(block,targetValue);
    }

    /**
     * 挖矿得到小于难度目标值HASH
     * 准备数据 用sha256哈希 转大整数 比较目标值
     * @return
     */
    public PowResult run(){
        long nonce=0;
        String shaHex="";
        log.info("开始进行挖矿：{}",this.getBlock().getData());
        long  startTime=System.currentTimeMillis();
        while (nonce<Long.MAX_VALUE){
            byte[]data=this.prepareData(nonce);
            shaHex= DigestUtils.sha256Hex(data);
            log.info("{}{}",nonce,shaHex);
            if (new BigInteger(shaHex,16).compareTo(this.target)==-1){
                log.info("\t 耗时Time：{} second \n 当前区块Hash:{}",(float)(System.currentTimeMillis()-startTime)/1000,shaHex);
                break;
            }else {
                nonce++;
            }
        }
        return new PowResult(nonce,shaHex);
    }

    /**
     * 根据区块数据以及nonce生成字节数组
     * @param nonce
     * @return
     */
    private byte[] prepareData(long nonce){
        byte[] prevBlockHashBytes={};
        if (StringUtils.isNoneBlank(this.getBlock().getPrevBlockHash())){
            prevBlockHashBytes=new BigInteger(this.getBlock().getPrevBlockHash(),16).toByteArray();
        }
        return ByteUtils.merge(prevBlockHashBytes,this.getBlock().getData().getBytes(),
                ByteUtils.toBytes(this.getBlock().getTimeStamp()),
                ByteUtils.toBytes(TARGET_BITS),
                ByteUtils.toBytes(nonce));
    }

    /**
     * 验证区块是否有效
     * @return
     */
    public boolean validate(){
        byte[] data=this.prepareData(this.getBlock().getNonce());
        return new BigInteger(DigestUtils.sha256Hex(data),16).compareTo(this.target)==-1;
    }
}
