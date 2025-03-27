import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class InstructionBuilder {
    final static String OPCODES_PATH  = "src/Opcodes.json";

    public static InstructionSet buildInstructions() {
        // System.out.println("Started Building Instructions");
        InstructionSet instructionSet = new InstructionSet();
        JSONObject a = new JSONObject();
        try {
            // read json file
            File f = new File(OPCODES_PATH);
            FileReader fReader = new FileReader(f);
            JSONParser parser =  new JSONParser();
            a = (JSONObject) parser.parse(fReader);
        } catch (Exception e) {
            System.err.println("Could not load Opcodes.json");
        }        
        // initialize instruction hashmaps
        Map<String, Integer> unprefixed = new HashMap<String, Integer>();
        Map<String, Integer> cbprefixed = new HashMap<String, Integer>();
        Set<String> mnemonics = new HashSet<>();

        // iteratre through both sets of Strings
        for (int i = 0; i < 2; i++) {
            // loop setup
            Map<String, Integer> instructions;
            JSONObject o = new JSONObject();

            if (i == 0) {
                o = (JSONObject)a.get("unprefixed");
                unprefixed = new HashMap<String, Integer>();
                instructions = unprefixed; 
            } else {
                o = (JSONObject)a.get("cbprefixed");
                cbprefixed = new HashMap<String, Integer>();
                instructions = cbprefixed; 
            }
            // iter through - works regardless of JSON order
            for (Object j : o.keySet()) {
                String key = j.toString();
                // convert key to corresponding index in array and get JSON
                int code = Integer.parseUnsignedInt(key.substring(2), 16);
                JSONObject jsonInstruction = (JSONObject) o.get(key);
                // get attributes
                String mnemonic = (String) jsonInstruction.get("mnemonic");
                // add mnemonic to list of possible mnemonics if not already included
                mnemonics.add(mnemonic);
                // read dictionary which holds the number of cycles instruciton takes
                JSONArray jsonCycles = (JSONArray)jsonInstruction.get("cycles");
                int[] cycles = new int[jsonCycles.size()];
                for (int k = 0; k < jsonCycles.size(); k++) {
                    cycles[k] = Integer.parseInt(jsonCycles.get(k).toString());
                }
                // init operands array
                JSONArray jsonOperands = (JSONArray) jsonInstruction.get("operands");

                // get operands - sometimes null
                Operand lOperand = null;
                Operand rOperand = null;
                if (jsonOperands.size() > 0) {
                    JSONObject jsonOperand = (JSONObject)jsonOperands.get(0);
                    String name = jsonOperand.get("name").toString();
                    Boolean immediate = Boolean.parseBoolean(jsonOperand.get("immediate").toString());
                    lOperand = new Operand(name, immediate);
                    // if (name.equals("A") && mnemonic.equals("INC")) {
                    //     System.out.println(new Instruction(mnemonic, lOperand, rOperand));
                    // }
                } else if (jsonOperands.size() > 1){
                    JSONObject jsonOperand = (JSONObject)jsonOperands.get(1);
                    String name = jsonOperand.get("name").toString();
                    Boolean immediate = Boolean.parseBoolean(jsonOperand.get("immediate").toString());
                    rOperand = new Operand(name, immediate);
                }

                // initialize new instruction object and add to map
                Instruction instruction = new Instruction(mnemonic, lOperand, rOperand);

                instructions.put(instruction.toString(), code);
            }
        }
        instructionSet.UNPREFIXED = unprefixed;
        instructionSet.CBPREFIXED = cbprefixed;
        instructionSet.MNEMONICS = mnemonics;

        // System.out.println("Finished Building Instructions");

        return instructionSet;
    }

    /**
    //  * @return
    //  */
    // public ArrayList<Op> buildOPs(Instruction i){
    //     NOP op = new NOP();
    // ArrayList<Op> ops = new ArrayList<>();
    // ops.add(op);
    // return ops;
    // }
}
