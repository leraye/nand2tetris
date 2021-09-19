import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author heqing.ye
 * @ClassName: Assembler
 * @Description: This module contains the main function which drives the entire translation process
 * @date 8/13/21
 */
public class Assembler {

    public static void main(String[] args){

        String input = args[0];
        if(!input.endsWith(".asm")){
            throw new IllegalArgumentException("Invalid input file type");
        }
        // default output file
        String output = input.replaceAll("([^\\.]+)\\.asm$", "$1_yhq.hack");
        if(args.length >= 2){
            output = args[1];
            if(!output.endsWith(".hack")){
                throw new IllegalArgumentException("Invalid output file type");
            }
        }

        try{
            FileReader fileReader = new FileReader(input);
            Parser parser = new Parser();
            parser.parse(fileReader);
            fileReader.close();

            CodeGenerator codeGenerator = new CodeGenerator();
            List<Instruction> instructions = parser.getInstructions();
            List<String> binaries = codeGenerator.translate(instructions);

            PrintWriter printWriter = new PrintWriter(output);
            for(String bin : binaries){
                printWriter.write(bin);
                printWriter.write("\n");
            }
            printWriter.close();

        }catch(FileNotFoundException e){
            System.err.println("Caught FileNotFoundException: " + e.getMessage());
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }

    }
}
