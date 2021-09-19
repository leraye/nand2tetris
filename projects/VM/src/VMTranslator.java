import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heqing.ye
 * @ClassName: Translator
 * @Description: TODO
 * @date 8/16/21
 */
public class VMTranslator {

    private static boolean checkValidFileName(String filename){
        return filename.matches("^[A-Z][^\\.]*\\.vm$");
    }

    private static List<File> getFiles(File file){
        List<File> files = new ArrayList<>();
        if(Files.isRegularFile(file.toPath())){
            String filename = file.getName();
            if(checkValidFileName(filename)){
                files.add(file);
            }
        }else if(Files.isDirectory(file.toPath())){
            for(File f : file.listFiles()){
                String name = f.getName();
                if(checkValidFileName(name)){
                    files.add(f);
                }
            }
        }
        return files;
    }

    public static void main(String[] args){

        if(args.length != 1){
            throw new IllegalArgumentException("One and only one argument is needed.");
        }

        try{
            File file = new File(args[0]);
            if(!file.exists()){
                throw new FileNotFoundException("Input file/directory not found.");
            }
            List<File> files = getFiles(file);
            if(files.isEmpty()){
                throw new FileNotFoundException("No valid file has been found.");
            }
            String folderName = file.getName();
            String folderPath = file.getPath();
            PrintWriter printWriter = new PrintWriter(folderPath + "/" + folderName + ".asm");
//            System.out.println(folderPath);

            Parser parser = new Parser();
            CodeWriter codeWriter = new CodeWriter();
//            codeWriter.bootstrap();

            for(File input : files){
                FileReader fileReader = new FileReader(input);
                String filename = input.getName().replaceAll("([^\\.]+)\\.vm", "$1");
                parser.currFile = filename;
                BufferedReader in = new BufferedReader(fileReader);
                while(in.ready()){
                    String currLine = in.readLine();
                    Command command = parser.parse(currLine);
                    if(command != null){
                        codeWriter.translate(command);
                    }
                }
                in.close();
                fileReader.close();

                for(String bin : codeWriter.getAssemblyCodes()){
                    printWriter.write(bin);
                    printWriter.write("\n");
                }
                codeWriter.reset();
            }
            codeWriter.writeEndLabel();
            if(codeWriter.CALL_FLAG){
                codeWriter.callSubroutine();
            }
            if(codeWriter.RETURN_FLAG){
                codeWriter.returnSubroutine();
            }
            if(codeWriter.EQ_FLAG){
                codeWriter.eqSubroutine();
            }
            if(codeWriter.LT_FLAG){
                codeWriter.ltSubroutine();
            }
            if(codeWriter.GT_FLAG){
                codeWriter.gtSubroutine();
            }
            for(String bin : codeWriter.getAssemblyCodes()){
                printWriter.write(bin);
                printWriter.write("\n");
            }
            printWriter.close();
        }catch(FileNotFoundException e){
            System.err.println("Caught FileNotFoundException: " + e.getMessage());
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }catch(IllegalArgumentException e){
            System.err.println("Caught Exception: " + e.getMessage());
        }
    }
}
