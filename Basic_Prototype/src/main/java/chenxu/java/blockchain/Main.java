package chenxu.java.blockchain;

import chenxu.java.blockchain.block.Block;
import chenxu.java.blockchain.block.Blockchain;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Blockchain blockchain= Blockchain.newBlockchain();
        log.info("区块链的信息：区块的长度：{}",blockchain.getBlockList().size());
        blockchain.addBlock("send 1 BTC to 陈序");
        blockchain.addBlock("send 2 more BTC to test1");
        blockchain.addBlock("send 3 more BTc to test2");
        for (int i=0;i<blockchain.getBlockList().size();i++){
            Block block=blockchain.getBlockList().get(i);
            log.info("第{}个区块信息：\tprevBlockHash:{} \tData:{} \tHash:{}",block.getHeight()
            ,block.getPrevBlockHash(),block.getData(),block.getHash());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
            String data2=simpleDateFormat.format(new Date(block.getTimeStamp()*1000L));
            log.info("\ttimeStamp:{}",data2);
        }
    }
}
