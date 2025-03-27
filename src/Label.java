public class Label implements Data{
    // used to represent abstract memory locations
    private int _address;
    // private String _name;
    private boolean _set;

    public byte[] toBinary(){
        if (!_set) {
            throw new Error("label was never defined");
        } else {
            return new byte[]{(byte)(_address >> 8), (byte)_address}; // convert 16-bit memory address into two bytes
        }
    }

    public void set(int address) {
        if (!_set) {
            _address = address;
            _set = true;    
        } else { // label redefinition probably isn't enforced like this in actual compiler - done for my own sanity
            throw new Error("cannot define label location twice"); 
        }
    }

    public boolean isSet() {
        return _set;
    }

    public Label(int address) {
        _address = address;
        _set = true;
        // _name = name;
    }

    public Label(){
        _set = false;
    }

    // public Label(String name, int address){
    //     this(name);
    //     _address = address;
    //     set = true;
    // }


    // public String getName(){
    //     return _name;
    // }
}
