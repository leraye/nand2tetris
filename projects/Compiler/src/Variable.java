import java.util.Map;

/**
 * @author heqing.ye
 * @ClassName: Variable
 * @Description: TODO
 * @date 9/2/21
 */
public class Variable {

    public String _name, _type, _kind, _segment;
    public Integer _index;

    public Variable(String name, String type, String kind, Integer counter){
        _name = name;
        _type = type;
        _kind = kind;
        _index = counter;
    }
}
