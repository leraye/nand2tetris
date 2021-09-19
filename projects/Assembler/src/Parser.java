import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author heqing.ye
 * @ClassName: Parser
 * @Description: 移除comments；记录label和变量；instruction分类
 * @date 8/14/21
 */
public class Parser {

    private SymbolTable symbolTable;
    private List<Instruction> instructions;

    public Parser(){
        symbolTable = new SymbolTable();
        instructions = new ArrayList<>();
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/19/21
     * @Title: parse
     * @Description: 记录label和变量的地址；把instructions分类
     * @param fileReader
     * @return void    返回类型
     * @throws
     */
    public void parse(FileReader fileReader) throws IOException{
        List<String> text = firstPass(new BufferedReader(fileReader));
        int varLocation = 16;
        for(String line : text){
            if(isAInstruction(line)){
                AType aType = new AType(line);
                if(aType.getType().equals(AType.SYMBOL)){
                    String s = aType.getSymbol();
                    if(!symbolTable.contains(s) && !SymbolTable.PREDEFINED.containsKey(s)){
                        symbolTable.addEntry(s, varLocation);
                        aType.setValue(varLocation);
                        ++varLocation;
                    }else {
                        if (SymbolTable.PREDEFINED.containsKey(s)) {
                            aType.setValue(SymbolTable.PREDEFINED.get(s));
                        }else if (symbolTable.contains(s)) {
                            aType.setValue(symbolTable.getAddress(s));
                        }else{
                            throw new RuntimeException("Syntax error in line: " + line + ", undefined symbol");
                        }
                    }
                }
                instructions.add(aType);
            }else if(line.matches("^[A,D,M]+=[^;]+") || line.matches("[^=]+;J..$")){
                instructions.add(new CType(line));
            }else{
                throw new RuntimeException("Syntax error in line: " + line + ", undefined instruction");
            }
        }
    }

    public final List<Instruction> getInstructions() {
        return instructions;
    }

    //=========================================================================================================

    /**
     * @Author: heqing.ye
     * @Date: 9/19/21
     * @Title: trimLine
     * @Description: 移除注解
     * @param line 字符串
     * @return String    返回类型
     * @throws
     */
    private String trimLine(String line){
        line = line.trim();
        String trimmed = line.replaceAll("([^/]*)//.*", "$1");

        return trimmed.trim();
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/19/21
     * @Title: isLabel
     * @Description: 判断字符串s是否是label
     * @param s 字符串
     * @return boolean    返回类型
     * @throws
     */
    private boolean isLabel(String s){
        return Pattern.matches(Instruction.LABEL, s);
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/19/21
     * @Title: isAInstruction
     * @Description: 判断字符串s是否是A类instruction
     * @param s 字符串
     * @return boolean    返回类型
     * @throws
     */
    private boolean isAInstruction(String s){
        return Pattern.matches(Instruction.A_TYPE, s);
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/19/21
     * @Title: firstPass
     * @Description: 逐行移除代码中的comments，并且记录所有label
     * @param in
     * @return List<java.lang.String>    返回类型
     * @throws
     */
    private List<String> firstPass(BufferedReader in) throws IOException{
        List<String> text = new ArrayList<>();
        try{
            int lineNumber = 0;
            while(in.ready()){
                String currLine = in.readLine();
                currLine = trimLine(currLine);
                if(currLine.isEmpty()){
                    continue;
                }
                if(isLabel(currLine)){
                    String label = currLine.substring(1, currLine.length()-1);
                    symbolTable.addEntry(label, lineNumber);
                }else{
                    text.add(currLine);
                    ++lineNumber;
                }
            }
            in.close();
        }catch(IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }
        return text;
    }
}
