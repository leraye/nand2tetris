import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author heqing.ye
 * @ClassName: JackAnalyzer
 * @Description: TODO
 * @date 8/25/21
 */
public class JackCompiler {

    private static boolean checkValidFileName(String filename){
        return filename.matches("^[A-Z][^\\.]*\\.jack$");
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

    private static String getFilename(String jackFilename){
        return jackFilename.replaceAll("([^\\.]+)\\.jack$", "$1");
    }

    public static void main(String[] args){

        if(args.length != 1){
            throw new IllegalArgumentException("One and only one argument is needed.");
        }

        try{
//            File file = new File("src/Main.jack");
            File file = new File(args[0]);
            if(!file.exists()){
                throw new FileNotFoundException("Input file/directory not found.");
            }
            List<File> files = getFiles(file);
            if(files.isEmpty()){
                throw new FileNotFoundException("No valid file has been found.");
            }


            for(File input : files){
                FileReader fileReader = new FileReader(input);
                BufferedReader in = new BufferedReader(fileReader);
                JackTokenizer jackTokenizer = new JackTokenizer();
                while(in.ready()){
                    String currLine = in.readLine();
                    jackTokenizer.trimLine(currLine.trim());
                }
//                printWriter.write("<tokens>\n");
                List<String> lines = jackTokenizer.getText();

                List<Token> allTokens = new ArrayList<>();
                for(String s : lines){
                    List<Token> tokens = jackTokenizer.parseLine(s);
                    allTokens.addAll(tokens);
                }
//                ListIterator<Token> it = allTokens.listIterator();
//                PrintWriter printWriter = new PrintWriter(getFilename(input.getName()) + ".xml");
                CompilationEngine compilationEngine = new CompilationEngine(allTokens, getFilename(input.getName()) + ".vm");
                compilationEngine.compileClass();
/**
                for(String s : compilationEngine.getLines()){
//                    System.out.print(s);
                    printWriter.write(s);
                    printWriter.flush();
                }
 */
//                printWriter.write("</tokens>\n");
//                int c;
//                while((c = in.read()) != -1){
//                    System.out.println((char)c);
//                }
                jackTokenizer.reset();

//                compilationEngine.reset();
                in.close();
                fileReader.close();
//                printWriter.close();
            }
        }catch(FileNotFoundException e) {
            System.err.println("Caught FileNotFoundException: " + e.getMessage());
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }catch(RuntimeException e){
            System.err.println("Caught RuntimeException: " + e.getMessage());
        }
    }
}
