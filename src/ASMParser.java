import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ASMParser {
    
    private static boolean bracketsBalanced(String s) {
        int cnt = 0;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '[' -> cnt++;
                case ']' -> cnt--;
            }
        }
        // System.out.println(s + " " + cnt);
        return cnt == 0;
    }

    private static String removeBrackets(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '[':
                case ']' :
                    break;
                default :
                    stringBuilder.append(c);
            }
        }
        return new String(stringBuilder);
    }

    // finds next word from i - keeps trailing spaces
    private static String nextWord(String s, int i) {
        String t = "";
        // fill t with char from i until whitespace reached -- does not capitalize
        // includes parenthesis and colons
        char c;
        while (i < s.length() && !Character.isWhitespace(c = s.charAt(i))) {
            i++;
            t += c;
        }

        return t; 
    }
    
    // returns string tokens in a QueueList for ease of processing
    public static QueueList<String> parseLine(String s) {
        QueueList<String> list = new QueueList<>();

        int i = 0;
        while (i < s.length()) {
            String tmp = nextWord(s, i);
            if (!tmp.isEmpty()) {
                if (tmp.charAt(0) == ';') { // stop scanning line on encountering comment
                    break;
                } else {
                    i += tmp.length();
                    System.out.println(tmp);
                    list.enqueue(tmp);    
                }
            } else {
                i++;
            }
        }
        return list;
    }


    // utility function for converting binary, hex and decimal integers in assembly syntax into integers
    private static int parseAsmLiteral(String s) {
        char c = s.charAt(0);
        switch (c) {
            case '$' : //binary
                return Integer.valueOf(s.substring(1), 2);
            case '%' : //hex
                return Integer.valueOf(s.substring(1), 16);
            default:   // decimal
                return Integer.valueOf(s);
        }
    }

    public static QueueList<Data> parseData(Scanner scan) {        
        // parse into list of data blocks
        QueueList<Data> data = new QueueList<>();

        int lineNum = 1; 
        int currentByte = 0; // location of current instruction in final program
        Map<String, Label> labels = new TreeMap<>();

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            QueueList<String> lineTokens = ASMParser.parseLine(line);
            String firstString = lineTokens.dequeue();
            Data lineData;
            Operand lOperand = null;
            Operand rOperand = null;
        
            // determine statement type 
            if (Instruction.instructionSet.MNEMONICS.contains(firstString.toUpperCase())) {  // parse statement as instruction
                // convert strings to operands
                // all needed information to create operands
                String left = lineTokens.dequeue();
                String right = lineTokens.dequeue();
                OperandType leftType = null;
                OperandType rightType = null;
                Data leftData = null;
                Data rightData = null;
                boolean leftImmediate = true;
                boolean rightImmediate = true;
                int instructionBytes = 1; // incremented when operands that take up extra data are added

                // determine which type of data each operand is
                // System.out.println(left);
                // System.out.println(right);
                // left operand first
                if (left != null) {
                    // check for square brackets -- could definitely be streamlined
                    if (left.charAt(0) == '[') {
                        if (bracketsBalanced(left)) {
                            leftImmediate = false;
                            left = removeBrackets(left); // remove brackets for further processing
                        } else {
                            throw new RuntimeException("Unbalanced brackets at line: " + line);
                        }
                    }

                    // determine whether register or data value
                    leftType = OperandType.stringToOperand(left.toUpperCase());
                    
                    // if neither operand is a register, then do further processing
                    if (!OperandType.isRegister(leftType)) {
                        try {
                            int i = parseAsmLiteral(left);
                            leftData = new ByteData(i);
                            leftType = OperandType.intToOperand(i); // determine whether data is 16 or 8 bit
                            
                        } catch (Exception e) { // if exception is thrown, then left must represent a label
                            if (!labels.containsKey(left)) {
                                labels.put(left, new Label());
                            }
                            leftData = labels.get(left);
                        }
                        instructionBytes++;
                    }
                    lOperand = new Operand(leftType, leftData, leftImmediate);
                }

                // now for right operand
                if (right != null) {
                     // check for square brackets on right side
                    if (right.charAt(0) == '[') {
                        if (bracketsBalanced(right)) {
                            rightImmediate = false;
                            right = removeBrackets(right); // remove brackets for further processing
                        } else {
                            throw new RuntimeException("Unbalanced brackets at line: " + line);
                        }
                    }
                    // determine whether register or data value
                    rightType = OperandType.stringToOperand(right.toUpperCase());
 
                    if (!OperandType.isRegister(rightType)) {
                        try {
                            int i = parseAsmLiteral(right);
                            rightData = new ByteData(i);
                            rightType = OperandType.intToOperand(i); // determine whether data is 16 or 8 bit
                            
                        } catch (Exception e) { // if exception is thrown, then left must represent a label
                            if (!labels.containsKey(right)) {
                                labels.put(right, new Label());
                            }
                            rightData = labels.get(right);
                        }
                        instructionBytes++;
                    }
                    rOperand = new Operand(rightType, rightData, rightImmediate);
                }
                
                lineData = new Instruction(firstString.toUpperCase(), lOperand, rOperand);
                // current byte only incremented after processing instruction
                currentByte += instructionBytes;
                data.enqueue(lineData);

            } else if (firstString.charAt(firstString.length()-1) == ':') {    // parse statement as label definition
                firstString = firstString.substring(0, firstString.length()-1);
                // if label does not exist, create new label object - otherwise set memory location pointed to by label
                if (!labels.containsKey(firstString)) {
                    labels.put(firstString, new Label(currentByte));
                } else {
                    Label label = labels.get(firstString);
                    label.set(currentByte);
                }
            } else {

                throw new RuntimeException("Syntax error: " + line + "\nat line: " + lineNum);
            }
            lineNum++;

            // System.out.println(firstString);

        }
        return data;
    }

}
