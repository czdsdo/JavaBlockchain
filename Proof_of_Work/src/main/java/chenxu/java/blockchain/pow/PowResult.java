package chenxu.java.blockchain.pow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PowResult {
    private long nonce;//挖矿随机数
    private String hash;
}
