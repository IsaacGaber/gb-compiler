import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
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

        QueueList<Data> data = ASMParser.parseData(scan);
        write(data, "output");
    }



    static void write(QueueList<Data> data, String filename) {
        // only handle unprefixed instructions
        
        ArrayList<Byte> bytes = new ArrayList<>();
        while (data.hasNext()) {
            Data d = data.dequeue();
            // insert instruction/data into byte array
            byte[] rawData = d.toBinary();
            for (byte dataByte : rawData) {
                bytes.add(dataByte);
            }
            System.out.println("added data");
        }
        
        try (FileOutputStream F = new FileOutputStream(filename)) {
            // write bytes
            for (Byte b : bytes) {
                F.write(b);
            }
        } catch (Exception e) {
            System.err.println("Failed to write compiled result to file");
        }
    }
    
}

