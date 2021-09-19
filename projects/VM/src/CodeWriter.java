import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author heqing.ye
 * @ClassName: CodeWriter
 * @Description: TODO
 * @date 8/18/21
 */
public class CodeWriter {

    private List<String> assemblyCodes;
    private int labelCounter;
    private final static String END_LABEL = "GLOBAL_CONST:END";
    private final static String TRUE_LABEL = "GLOBAL_CONST:TRUE";
    private final static String FALSE_LABEL = "GLOBAL_CONST:FALSE";
    private final static String TEMP_LABEL = "TEMPORARY";
    private final static String CALL_LABEL = "GLOBAL_FUNC:CALL";
    private final static String RETURN_LABEL = "GLOBAL_FUNC:RETURN";
    private final static String EQ_LABEL = "GLOBAL_FUNC:EQ";
    private final static String LT_LABEL = "GLOBAL_FUNC:LT";
    private final static String GT_LABEL = "GLOBAL_FUNC:GT";


    public boolean CALL_FLAG, RETURN_FLAG, EQ_FLAG, LT_FLAG, GT_FLAG;

    public final static Map<String, String> ALOperationMapper = Map.ofEntries(
            Map.entry("add", "+"),
            Map.entry("sub", "-"),
            Map.entry("neg", "~"),
            Map.entry("eq", "=="),
            Map.entry("gt", ">"),
            Map.entry("lt", "<"),
            Map.entry("and", "&"),
            Map.entry("or", "|"),
            Map.entry("not", "!")
    );

    public CodeWriter(){
        assemblyCodes = new ArrayList<>();
        labelCounter = 0;
        RETURN_FLAG = false;
        EQ_FLAG = false;
        LT_FLAG = false;
        GT_FLAG = false;
        CALL_FLAG = false;
    }

    public final List<String> getAssemblyCodes() {
        return assemblyCodes;
    }

    public void translate(Command command){
        switch(command.getType()){
            case "push":
                writePush(command.getArg1(), command.getArg2());
                break;
            case "pop":
                writePop(command.getArg1(), command.getArg2());
                break;
            case "add": case "sub": case "neg": case "eq":
            case "gt": case "lt": case "and": case "or": case "not":
                if(command.getType().equals("eq")){
                    EQ_FLAG = true;
                }
                if(command.getType().equals("lt")){
                    LT_FLAG = true;
                }
                if(command.getType().equals("gt")){
                    GT_FLAG = true;
                }
                writeArithmetic(ALOperationMapper.get(command.getType()));
                break;
            case "label": case "goto": case "if-goto":
                writeBranching(command.getType(), command.getArg1());
                break;
            case "function":
                writeFunction(command.getArg1(), command.getArg2());
                break;
            case "call":
                CALL_FLAG = true;
                writeCall(command.getArg1(), command.getArg2());
                break;
            case "return":
                RETURN_FLAG = true;
                writeReturn();
                break;
            default: break;
        }
    }

    public void bootstrap(){
        memory2Memory("CONST", 256, "SP", null);
        writeCall("Sys.init", 0);
        CALL_FLAG = true;
    }

    public void eqSubroutine(){
        assemblyCodes.add("(" + EQ_LABEL + ")");
        assemblyCodes.add("@R14");
        assemblyCodes.add("M=D");

        binaryOperation("-");
        assemblyCodes.add("@" + TRUE_LABEL + "$EQ");
        assemblyCodes.add("D;JEQ");
        assemblyCodes.add("D=0");
        assemblyCodes.add("@" + FALSE_LABEL + "$EQ");
        assemblyCodes.add("0;JMP");
        assemblyCodes.add("(" + TRUE_LABEL + "$EQ" + ")");
        assemblyCodes.add("D=-1");
        assemblyCodes.add("(" + FALSE_LABEL + "$EQ" + ")");
        pushFromDReg();

        assemblyCodes.add("@R14");
        assemblyCodes.add("A=M");
        assemblyCodes.add("0;JMP");
    }

    public void ltSubroutine(){
        assemblyCodes.add("(" + LT_LABEL + ")");
        assemblyCodes.add("@R14");
        assemblyCodes.add("M=D");

        binaryOperation("-");
        assemblyCodes.add("@" + TRUE_LABEL + "$LT");
        assemblyCodes.add("D;JLT");
        assemblyCodes.add("D=0");
        assemblyCodes.add("@" + FALSE_LABEL + "$LT");
        assemblyCodes.add("0;JMP");
        assemblyCodes.add("(" + TRUE_LABEL + "$LT" + ")");
        assemblyCodes.add("D=-1");
        assemblyCodes.add("(" + FALSE_LABEL + "$LT" + ")");
        pushFromDReg();

        assemblyCodes.add("@R14");
        assemblyCodes.add("A=M");
        assemblyCodes.add("0;JMP");
    }

    public void gtSubroutine(){
        assemblyCodes.add("(" + GT_LABEL + ")");
        assemblyCodes.add("@R14");
        assemblyCodes.add("M=D");

        binaryOperation("-");
        assemblyCodes.add("@" + TRUE_LABEL + "$GT");
        assemblyCodes.add("D;JGT");
        assemblyCodes.add("D=0");
        assemblyCodes.add("@" + FALSE_LABEL + "$GT");
        assemblyCodes.add("0;JMP");
        assemblyCodes.add("(" + TRUE_LABEL + "$GT)");
        assemblyCodes.add("D=-1");
        assemblyCodes.add("(" + FALSE_LABEL + "$GT)");
        pushFromDReg();

        assemblyCodes.add("@R14");
        assemblyCodes.add("A=M");
        assemblyCodes.add("0;JMP");
    }

    public void callSubroutine(){
        assemblyCodes.add("(" + CALL_LABEL + ")");
        assemblyCodes.add("@R14");
        assemblyCodes.add("M=D");
        moveValue2Register("LCL", null, "D");
        pushFromDReg();
        moveValue2Register("ARG", null, "D");
        pushFromDReg();
        moveValue2Register("THIS", null, "D");
        pushFromDReg();
        moveValue2Register("THAT", null, "D");
        pushFromDReg();
        memory2Memory("SP", null, "LCL", null);
        assemblyCodes.add("@R14");
        assemblyCodes.add("A=M");
        assemblyCodes.add("0;JMP");
    }

    public void returnSubroutine(){
        assemblyCodes.add("(" + RETURN_LABEL + ")");

        memory2Memory("LCL", -5, "R15", null);

        pop2DReg();
        moveValue2Memory("D", "ARG", 0);

        computeAddressThenStore("ARG", 1, "D");
        moveValue2Memory("D", "SP", null);

        memory2Memory("LCL", -1, "THAT", null);
        memory2Memory("LCL", -2, "THIS", null);
        memory2Memory("LCL", -3, "ARG", null);
        memory2Memory("LCL", -4, "LCL", null);

        assemblyCodes.add("@R15");
        assemblyCodes.add("A=M");
        assemblyCodes.add("0;JMP");
    }

    public void reset(){
        assemblyCodes.clear();
    }

    //============================================================================================================

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: computeAddressThenStore
     * @Description: base address = RAM[src]; dest (some register) = base address + offset
     * @param src, offset, dest    参数
     * @return void    返回类型
     * @throws
     */
    private void computeAddressThenStore(String src, Integer offset, String dest){
        switch(offset){
            case 0:
                assemblyCodes.add("@" + src);
                assemblyCodes.add(dest + "=M");
                break;
            case 1:
                assemblyCodes.add("@" + src);
                assemblyCodes.add(dest + "=M+1");
                break;
            case -1:
                assemblyCodes.add("@" + src);
                assemblyCodes.add(dest + "=M-1");
                break;
            default:
                if(offset.intValue() > 0){
                    assemblyCodes.add("@" + Integer.toString(offset));
                    assemblyCodes.add("D=A");
                    assemblyCodes.add("@" + src);
                    assemblyCodes.add(dest + "=D+M");
                }else{
                    assemblyCodes.add("@" + Integer.toString(Math.abs(offset)));
                    assemblyCodes.add("D=A");
                    assemblyCodes.add("@" + src);
                    assemblyCodes.add(dest + "=M-D");
                }
                break;
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: moveValue2Register
     * @Description: dest是一个register。
     *              如果offset是NULL，那么把RAM[src]中的值移到dest中；
     *              如果offset非NULL，那么计算出 new address = RAM[src] + offset，然后把RAM[new address]中的值移到dest中
     * @param src, offset, dest    参数
     * @return void    返回类型
     * @throws
     */
    private void moveValue2Register(String src, Integer offset, String dest){
        if(offset == null){
            assemblyCodes.add("@" + src);
            assemblyCodes.add(dest + "=M");
        }else{
            switch(src){
                case "CONST":
                    assemblyCodes.add("@" + Integer.toString(offset));
                    assemblyCodes.add(dest + "=A");
                    break;
                default:
                    computeAddressThenStore(src, offset, "A");
                    assemblyCodes.add(dest + "=M");
                    break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: moveValue2Memory
     * @Description: src是一个register。
     *              如果offset是NULL，那么把src中的值移到RAM[dest]中；
     *              如果offset非NULL，那么计算出 new address = RAM[dest] + offset，然后把src中的值移到RAM[new address]中
     * @param src, dest, offset    参数
     * @return void    返回类型
     * @throws
     */
    private void moveValue2Memory(String src, String dest, Integer offset){
        if(offset == null){
            assemblyCodes.add("@" + dest);
        }else{
            computeAddressThenStore(dest, offset, "A");
        }
        assemblyCodes.add("M=" + src);
    }

    private void memory2Memory(String src, Integer offset_src, String dest, Integer offset_dest){
        moveValue2Register(src, offset_src, "D");
        moveValue2Memory("D", dest, offset_dest);
    }

    private void writePush(String src, Integer offset){
        moveValue2Register(src, offset, "D");
        pushFromDReg();
    }

    private void writePop(String dest, Integer offset){
        // 把内存的地址存在RAM[13]里面
        if(offset != null){
            computeAddressThenStore(dest, offset, "D");
            assemblyCodes.add("@R13");
            assemblyCodes.add("M=D");
            dest = "R13";
            offset = 0;
        }
        pop2DReg();
        moveValue2Memory("D", dest, offset);
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: pushFromDReg
     * @Description: 将D-Register中的元素加入栈中
     * @param
     * @return void    返回类型
     * @throws
     */
    private void pushFromDReg(){
        assemblyCodes.add("@SP");
        assemblyCodes.add("A=M");
        assemblyCodes.add("M=D");
        assemblyCodes.add("@SP");
        assemblyCodes.add("M=M+1");
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: writePop
     * @Description: 移除栈顶元素并把结果存到D-Register中
     * @param
     * @return void    返回类型
     * @throws
     */
    private void pop2DReg(){
        assemblyCodes.add("@SP");
        assemblyCodes.add("AM=M-1");
        assemblyCodes.add("D=M");
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: writeEndLabel
     * @Description: 用无限循环结束程序：为了保护内存
     * @param
     * @return void    返回类型
     * @throws
     */
    public void writeEndLabel(){
        assemblyCodes.add("(" + END_LABEL + ")");
        assemblyCodes.add("@" + END_LABEL);
        assemblyCodes.add("0;JMP");
    }

    private String nextLocalLabel(String label){
        return label + ":" + Integer.toString(labelCounter++);
    }

    private String nextReturnAddress(String label) {return label + "$ret." + Integer.toString(labelCounter++);}

    private void binaryOperation(String operator){
        pop2DReg();
        moveValue2Memory("D", "R13", null);
        pop2DReg();
        assemblyCodes.add("@R13");
        assemblyCodes.add("D=D" + operator + "M");
    }

    private void unaryOperation(String operator){
        pop2DReg();
        assemblyCodes.add("M=" + operator + "D");
        assemblyCodes.add("@SP");
        assemblyCodes.add("M=M+1");
    }

    private void writeArithmetic(String operator){
        switch(operator){
            case "+": case "-": case "|": case "&":
                binaryOperation(operator);
                pushFromDReg();
                break;
            case "~":
                unaryOperation("-");
                break;
            case "!":
                unaryOperation(operator);
                break;
            default:
                String localLabel = nextLocalLabel(TEMP_LABEL);
                assemblyCodes.add("@" + localLabel);
                assemblyCodes.add("D=A");
                switch(operator){
                    case "==":
                        assemblyCodes.add("@" + EQ_LABEL);
                        break;
                    case ">":
                        assemblyCodes.add("@" + GT_LABEL);
                        break;
                    case "<":
                        assemblyCodes.add("@" + LT_LABEL);
                        break;
                    default: break;
                }
                assemblyCodes.add("0;JMP");
                assemblyCodes.add("(" + localLabel + ")");
        }
    }

    private void writeBranching(String operator, String label){
        switch(operator){
            case "label":
                assemblyCodes.add("(" + label + ")");
                break;
            case "goto":
                assemblyCodes.add("@" + label);
                assemblyCodes.add("0;JMP");
                break;
            case "if-goto":
                pop2DReg();
                assemblyCodes.add("@" + label);
                assemblyCodes.add("D;JNE");
                break;
            default: break;
        }
    }

    private void writeFunction(String funcName, Integer nVars){
        assemblyCodes.add("(" + funcName + ")");
        for(int i = 0; i < nVars; ++i){
            moveValue2Register("CONST", 0, "D");
            pushFromDReg();
         }
    }

    private void writeCall(String funcName, Integer nVars){
        String returnAddress = nextReturnAddress(funcName);
        String localLabel = nextLocalLabel(TEMP_LABEL);
        assemblyCodes.add("@" + returnAddress);
        assemblyCodes.add("D=A");
        pushFromDReg();

        assemblyCodes.add("@" + localLabel);
        assemblyCodes.add("D=A");
        assemblyCodes.add("@" + CALL_LABEL);
        assemblyCodes.add("0;JMP");
        assemblyCodes.add("(" + localLabel + ")");
        computeAddressThenStore("SP", -(5 + nVars), "D");
        moveValue2Memory("D", "ARG", null);
        writeBranching("goto", funcName);
        assemblyCodes.add("(" + returnAddress + ")");
    }

    private void writeReturn(){
        assemblyCodes.add("@" + RETURN_LABEL);
        assemblyCodes.add("0;JMP");
    }

}
