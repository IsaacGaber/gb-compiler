import java.util.Map;

public class Operand implements Data{
    // operand types
    private OperandType _operandType;
    private Data _data;
    private boolean _immediate;

    // init operand from string - used when building instruction set
    Operand(String s, boolean immediate) {
        _operandType = OperandType.stringToOperand(s);
        _immediate = immediate;
        _data = null;
    }

    Operand(String s, Map<String, Label> l) {
        throw new UnsupportedOperationException("Unimplemented constructor");
    }

    Operand(OperandType o) {
        _operandType = o;
        _data = null;    
    }

    Operand(OperandType o, Data d, boolean immediate) {
        _operandType = o;
        _data = d;
        _immediate = immediate;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Operand other = (Operand)obj;
            return other._operandType == _operandType && other._immediate == _immediate;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public byte[] toBinary() {
        return _data.toBinary();
    }

    public String toString() {
        if (_operandType == null) {
            return "";
        } else if (_immediate) {
            return _operandType.name();
        } else {
            return '[' + _operandType.name() + ']';
        }
    }
}
