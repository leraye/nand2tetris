import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author heqing.ye
 * @ClassName: CType
 * @Description: TODO
 * @date 8/14/21
 */
public class CType implements Instruction{

    private String dest;
    private String comp;
    private String jump;
    private Integer type;

    public CType(String s){
        Pattern p = Pattern.compile("[=;]");
        String[] symbols = p.split(s);

        if(s.matches("^[A,D,M]+=[^;]+")){
            dest = destNormalize(symbols[0]);
            comp = compNormalize(symbols[1]);
            jump = null;
            type = CType.ASSIGNMENT;
        }
        if(s.matches("[^=]+;J..$")){
            dest = null;
            comp = compNormalize(symbols[0]);
            jump = symbols[1];
            type = CType.JUMP;
        }
    }

    @Override
    public String getDest() {
        return dest;
    }

    @Override
    public String getValue() {
        return comp;
    }

    @Override
    public String getJump() {
        return jump;
    }

    @Override
    public Integer getType() {
        return type;
    }

    private String destNormalize(String s){
        StringBuilder stringBuilder = new StringBuilder();
        if(s.contains("A")){
            stringBuilder.append('A');
        }
        if(s.contains("D")){
            stringBuilder.append('D');
        }
        if(s.contains("M")){
            stringBuilder.append('M');
        }
        return stringBuilder.toString();
    }

    private String compNormalize(String s){
        if(Pattern.matches(s, "1+[ADM]") || Pattern.matches(s, "[AM][+&|]D")){
            return s.replaceAll("([AM0-9])([+&|])([ADM])", "$3$2$1");
        }
        return s;
    }
}
