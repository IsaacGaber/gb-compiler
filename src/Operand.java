public enum Operand{
    // 8 bit registers
    A,
    B,
    C,
    D,
    E,
    F,
    H,
    L,
    // 16 bit registers
    AB,
    CD,
    EF,
    HL,
    SP,
    PC,
    // variable types
    N8,  // immediate 8-bit data
    N16, // immediate little-endian 16 bit data
    A8,  // immediate 8-bit unsigned data, sometimes added to 0xFF00 to create a 16 bit HRAM address
    A16, // little-endian 16-bit address
    E8,  // 8-bit unsigned data
    // conditional codes
    CC; // all conditional codes
    // NZ, // execute if result of last operation not zero
    // Z,  // execute if result of last operation is zero
    // C,  // execute if result of last operation not Zero
    // NC;

    public static Operand stringToOperand(String s){
        if (s == null) {
            return null;
        }
        // System.out.println(s);
        switch (s.toLowerCase()) {
            case "a" : return A;
            case "b" : return B;
            case "c" : return C;
            case "d" : return D;
            case "e" : return E;
            case "f" : return F;
            case "h" : return H;
            case "l" : return L;
            case "cc": return CC;
            default: return null;
        }
    }
}