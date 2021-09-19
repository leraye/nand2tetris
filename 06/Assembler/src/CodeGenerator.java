import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author heqing.ye
 * @ClassName: CodeGenerator
 * @Description: TODO
 * @date 8/14/21
 */
public class CodeGenerator {

    public final static int WORD_SIZE = 16;
    public final static String A_PREFIX = "0";
    public final static String C_PREFIX = "111";
    public final static String NO_DEST = "000";
    public final static String NO_JUMP = "000";

    public final static Map<String, String> COMP_CODE = Map.ofEntries(
            Map.entry("0", "0101010"),
            Map.entry("1", "0111111"),
            Map.entry("-1", "0111010"),
            Map.entry("D", "0001100"),
            Map.entry("A", "0110000"),
            Map.entry("M", "1110000"),
            Map.entry("!D", "0001101"),
            Map.entry("!A", "0110001"),
            Map.entry("!M", "1110001"),
            Map.entry("-D", "0001111"),
            Map.entry("-A", "0110011"),
            Map.entry("-M", "1110011"),
            Map.entry("D+1", "0011111"),
            Map.entry("A+1", "0110111"),
            Map.entry("M+1", "1110111"),
            Map.entry("D-1", "0001110"),
            Map.entry("A-1", "0110010"),
            Map.entry("M-1", "1110010"),
            Map.entry("D+A", "0000010"),
            Map.entry("D+M", "1000010"),
            Map.entry("D-A", "0010011"),
            Map.entry("D-M", "1010011"),
            Map.entry("A-D", "0000111"),
            Map.entry("M-D", "1000111"),
            Map.entry("D&A", "0000000"),
            Map.entry("D&M", "1000000"),
            Map.entry("D|A", "0010101"),
            Map.entry("D|M", "1010101")
    );

    public final static Map<String, String> DEST_CODE = Map.ofEntries(
            Map.entry("M", "001"),
            Map.entry("D", "010"),
            Map.entry("DM", "011"),
            Map.entry("A", "100"),
            Map.entry("AM", "101"),
            Map.entry("AD", "110"),
            Map.entry("ADM", "111")
    );

    public final static Map<String, String> JUMP_CODE = Map.ofEntries(
            Map.entry("JGT", "001"),
            Map.entry("JEQ", "010"),
            Map.entry("JGE", "011"),
            Map.entry("JLT", "100"),
            Map.entry("JNE", "101"),
            Map.entry("JLE", "110"),
            Map.entry("JMP", "111")
    );

    public CodeGenerator(){}

    public List<String> translate(List<Instruction> instructions){
        List<String> binaries = new ArrayList<>();
        for(Instruction instruction : instructions){
            if(instruction.getType().equals(AType.CONSTANT)){
                String val = instruction.getValue();
                String dest = instruction.getDest();
                binaries.add(dest + val.substring(dest.length()));
            }else if(instruction.getType().equals(CType.ASSIGNMENT)){
                String dest = CodeGenerator.DEST_CODE.get(instruction.getDest());
                String val = CodeGenerator.COMP_CODE.get(instruction.getValue());
                binaries.add(CodeGenerator.C_PREFIX + val + dest + CodeGenerator.NO_JUMP);
            }else{
                String val = CodeGenerator.COMP_CODE.get(instruction.getValue());
                String jump = CodeGenerator.JUMP_CODE.get(instruction.getJump());
                binaries.add(CodeGenerator.C_PREFIX + val + CodeGenerator.NO_DEST + jump);
            }
        }
        return binaries;
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/19/21
     * @Title: int2Binary
     * @Description: 数字num转换成16位的二进制字符串，不足位补零
     * @param num
     * @return java.lang.String    返回类型
     * @throws
     */
    public static String int2Binary(Integer num){
        String s = Integer.toBinaryString(num);
        StringBuilder stringBuilder = new StringBuilder();
        int currLen = s.length();
        while(currLen < CodeGenerator.WORD_SIZE){
            stringBuilder.append("0");
            ++currLen;
        }
        stringBuilder.append(s);
        return stringBuilder.toString();
    }
}
