import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author heqing.ye
 * @ClassName: VMWriter
 * @Description: TODO
 * @date 9/2/21
 */
public class VMWriter {

    private PrintWriter printWriter;

    public VMWriter(String outputFile) {
        try {
            printWriter = new PrintWriter(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        printWriter.close();
    }

    public void writePush(String segment, int index){
        writeCommand("push", segment, String.valueOf(index));
    }

    public void writePop(String segment, int index){
        writeCommand("pop", segment, String.valueOf(index));
    }

    public void writeCall(String funcName, int nArgs){
        writeCommand("call", funcName, String.valueOf(nArgs));
    }

    public void writeArithmetic(String command){
        writeCommand(command,"","");
    }

    public void writeFunction(String funcName, int nArgs){
        writeCommand("function", funcName, String.valueOf(nArgs));
    }

    public void write(String s){
        printWriter.write(s);
        printWriter.flush();
    }

    public void writeLabel(String label){
        writeCommand("label", label, "");
    }

    public void writeIf(String label){
        writeCommand("if-goto", label, "");
    }

    public void writeGoto(String label){
        writeCommand("goto", label, "");
    }

    public void writeReturn(){
        writeCommand("return","","");
    }

    private void writeCommand(String cmd, String arg1, String arg2){
        printWriter.write(cmd + " " + arg1 + " " + arg2 + "\n");
        printWriter.flush();
    }
}
