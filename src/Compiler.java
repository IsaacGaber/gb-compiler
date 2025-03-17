import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Scanner;

public class Compiler {


    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in);
        Scanner scan;
        // load file to compile 
        while (true) {
            try {
                System.out.print("Input the full path of the file you would like to compile: ");
                scan = new Scanner(new FileReader(in.nextLine()));
                in.close();
                break;
            } catch (Exception e) {
                System.out.println("Invalid file path");
            }

        }

        Queue<Data> data = ASMParser.parseData(scan);
        write(data, "output");
    }



    static void write(Queue<Data> data, String filename) {
        // only handle unprefixed instructions
        
        byte bytes[] = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            Data d = data.dequeue();
            // insert instruction/data into byte array
            bytes[i] = (byte)(d.toBinary());
            i++;
        }
        
        try (FileOutputStream F = new FileOutputStream(filename)) {
            F.write(bytes);
        } catch (Exception e) {
            System.err.println("Failed to write compiled result to file");
        }
    }
    
}

