package chenxu.java.blockchain.util;


import java.util.Arrays;
import java.util.stream.Stream;
import java.nio.ByteBuffer;
import org.apache.commons.lang3.ArrayUtils;

public class ByteUtils {
    public static final String ZERO_HASH="";
    /**
     * 多字节拼接
     * @param bytes
     * @return
     */
    public static byte[]merge(byte[]...bytes){
        Stream<Byte>stream=Stream.of();
        for (byte[] b:bytes){
            stream=Stream.concat(stream, Arrays.stream(ArrayUtils.toObject(b)));
        }
        return ArrayUtils.toPrimitive(stream.toArray(Byte[]::new));
    }

    /**
     * Long转Byte
     * @param val
     * @return
     */
    public static byte[] toBytes(long   val){
        return ByteBuffer.allocate(Long.BYTES).putLong(val).array();
    }
}
