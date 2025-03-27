import java.util.ArrayList;

public class ByteData implements Data {
    private byte[] val;

    // fills an array of bytes with 8 bit chunks of integer
    public ByteData(int i) {
        ArrayList<Byte> a = new ArrayList<>();
        // shift i to the right
        while((i & 0xFFFFFFFF) != 0) {
            a.add((byte)i);
            i = i >> 8;
        }
        // allocate array
        val = new byte[a.size()];
        for (int j = 0; j < a.size(); j++) {
            val[j] = (byte)(a.get(j)); // convert to bytes from Byte
        }
    }

    @Override
    public byte[] toBinary() {
        return val;
    }
    
}
