public interface Data {
    // returns binary representation of data - can be used for instructions, labels, and inline code
    public default byte[] toBinary() {
        return new byte[0];
    };
}
