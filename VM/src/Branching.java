/**
 * @author heqing.ye
 * @ClassName: Branching
 * @Description: TODO
 * @date 8/20/21
 */
public class Branching implements Command{

    private String operator, label;

    public Branching(String _op, String arg1, String arg2){
        operator = _op;
        label = arg2 + "$" + arg1;
    }

    @Override
    public String getArg1() {
        return label;
    }

    @Override
    public Integer getArg2() {
        return null;
    }

    @Override
    public String getType() {
        return operator;
    }
}
