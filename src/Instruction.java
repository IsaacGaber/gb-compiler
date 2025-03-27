public class Instruction implements Data {
    private final String _mnemonic;
    private final Operand _lOperand;
    private final Operand _rOperand;

    // build instruction set -- used to get binary representation
    public static final InstructionSet instructionSet = InstructionBuilder.buildInstructions();


    Instruction(String mnemonic, Operand lOperand, Operand rOperand){
        // _code = code;
        _mnemonic = mnemonic;
        _lOperand = lOperand;
        _rOperand = rOperand;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Instruction) {
            Instruction instruction = (Instruction) obj;
            return (this  == obj ||
                    (this._mnemonic == instruction._mnemonic 
                    && this._lOperand == instruction._lOperand 
                    && this._rOperand == instruction._rOperand));
        } else {
            return false;
        }
    }

    public String toString(){
        return String.format("%s %s %s", _mnemonic, _lOperand, _rOperand);
    }

    @Override
    public byte[] toBinary() {
        try {
            return new byte[]{(byte)(int)(instructionSet.UNPREFIXED.get(this.toString()))}; // extremely ugly, I know
        } catch (Exception e) {
            throw new RuntimeException("Invalid instruction: " + this.toString());
        }
    }
}
