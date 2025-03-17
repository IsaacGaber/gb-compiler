public class Label implements Data{
    // used to match 
    public int address;
    public String name;
    boolean set = false;

    public int toBinary(){
        return address;
    }
}
