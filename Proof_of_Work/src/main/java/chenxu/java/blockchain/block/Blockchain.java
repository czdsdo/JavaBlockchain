package chenxu.java.blockchain.block;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
public class Blockchain {
    private List<Block>blockList;
    public static Blockchain newBlockchain(){
        List<Block>blocks=new LinkedList<>();
        blocks.add(Block.newGensisBlock());
        return new Blockchain(blocks);
    }
    public void addBlock(Block block){
        this.blockList.add(block);
    }
    public void addBlock(String data){
        Block previousBlock=blockList.get(blockList.size()-1);
        this.addBlock(Block.newBlock(previousBlock.getHash(),data,previousBlock.getHeight()+1));
    }
}
