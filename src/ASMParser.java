import java.util.Scanner;

public class ASMParser{

    // finds next word from i
    private static String nextWord(String s, int i) {
        String t = "";
        // fill t with letters until non-alphabetic letter reached (excepting colons -- used for labels)
        while (i < s.length() && (Character.isAlphabetic(s.charAt(i)) || s.charAt(i) == ':')&& s.charAt(i) != ';') {
            t += Character.toLowerCase(s.charAt(i));
            i++;
        }
        t = t.strip();
        return t;
    }

    // returns string tokens in a Queue for ease of processing
    public static Queue<String> parseLine(String s) {
        Queue<String> list = new Queue<>();
        String tmp;
        int i = 0;
        while (i < s.length()) {
            tmp = nextWord(s, i);
            if (!tmp.isEmpty()) {
                i += tmp.length();
                list.enqueue(tmp.toUpperCase());    // uppercase
            } else {
                i++;
            }
        }
        return list;
    }
    
    static Queue<Data> parseData(Scanner scan) {        
        // parse into list of instructions
        Queue<Data> data = new Queue<>();
        
        while (scan.hasNextLine()) {
            Queue<String> lineTokens = ASMParser.parseLine(scan.nextLine());
            String mnemonic = lineTokens.dequeue();
            // only run if label

            String lOperand = lineTokens.dequeue();
            String rOperand = lineTokens.dequeue();
            Instruction instruction = new Instruction(mnemonic, Operand.stringToOperand(lOperand), Operand.stringToOperand(rOperand));
            data.enqueue(instruction);
        }
        return data;
    }

}
