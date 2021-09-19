/**
 * @author heqing.ye
 * @ClassName: Push
 * @Description: TODO
 * @date 8/17/21
 */
public class PushPop implements Command{

    private Integer arg2;
    private String type, arg1;

    public PushPop(String _type, String _arg1, Integer _arg2){
        type = _type;
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
