/**
 * @author heqing.ye
 * @ClassName: Function
 * @Description: TODO
 * @date 8/22/21
 */
public class Function implements Command{

    private String type, arg1;
    private Integer arg2;

    public Function(String _op, String funcName, Integer nVars){
        type = _op;
        arg1 = funcName;
        arg2 = nVars;
    }

    @Override
    public String getArg1() {
        return arg1;
    }

    @Override
    public Integer getArg2() {
        return arg2;
    }

    @Override
    public String getType() {
        return type;
    }

}
