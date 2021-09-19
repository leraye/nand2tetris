import java.util.Map;

/**
 * @author heqing.ye
 * @ClassName: Arithmetic
 * @Description: TODO
 * @date 8/16/21
 */
public class Arithmetic implements Command{

    private Integer arg2;
    private String arg1, type;

    public Arithmetic(String _op, String _arg1, Integer _arg2){
        type = _op;
        arg1 = _arg1;
        arg2 = _arg2;
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
