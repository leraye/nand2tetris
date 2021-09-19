/**
 * @author heqing.ye
 * @ClassName: AType
 * @Description: TODO
 * @date 8/14/21
 */
public class AType implements Instruction{

    private String symbol;
    private Integer constant;
    private Integer type;

    public AType(String s){
        String expr = s.substring(1);
        if(expr.matches("[0-9]+")){
            constant = Integer.parseInt(expr);
            symbol = CodeGenerator.int2Binary(constant);
            type = Instruction.CONSTANT;
        }else{
            symbol = expr;
            constant = null;
            type = Instruction.SYMBOL;
        }
    }

    public final String getSymbol() {
        return symbol;
    }

    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public String getDest() {
        return CodeGenerator.A_PREFIX;
    }

    @Override
    public String getValue() {
        return symbol;
    }

    @Override
    public String getJump() {
        return null;
    }

    public void setValue(Integer constant) {
        this.constant = constant;
        this.symbol = CodeGenerator.int2Binary(this.constant);
        type = Instruction.CONSTANT;
    }
}
