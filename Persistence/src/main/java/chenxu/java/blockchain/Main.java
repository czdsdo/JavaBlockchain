package chenxu.java.blockchain;

import chenxu.java.blockchain.block.Block;
import chenxu.java.blockchain.block.Blockchain;
import chenxu.java.blockchain.pow.ProofOfWork;
import chenxu.java.blockchain.util.RocksDBUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Blockchain blockchain= Blockchain.newBlockchain();
        try {
            blockchain.addBlock("send 1 BTC to 陈序");
            blockchain.addBlock("send 2 more BTC to test1");
            blockchain.addBlock("send 3 more BTc to test2");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            RocksDBUtils.getInstance().closeDB();
        }
        Blockchain.BlockchainIterator iterator=blockchain.getBlockchainItertor();
        for (int i=0;i<blockchain.getBlockList().size();i++){
            Block block=blockchain.getBlockList().get(i);
            log.info("第{}个区块信息：\tprevBlockHash:{} \tData:{} \tHash:{}",block.getHeight()
            ,block.getPrevBlockHash(),block.getData(),block.getHash());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
            String data2=simpleDateFormat.format(new Date(block.getTimeStamp()*1000L));
            log.info("\ttimeStamp:{}",data2);
            ProofOfWork pow=ProofOfWork.newProofOfWork(block);
            log.info("是否有效：{} \n",pow.validate());
        }
    }
}
