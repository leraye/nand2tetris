/**
 * @author heqing.ye
 * @ClassName: Constant
 * @Description: TODO
 * @date 8/26/21
 */
public class Constant implements Token{

    private String _val, _type;
    private StringBuilder stringBuilder;

    public Constant(String val, String type){
        _val = val;
        _type = type;
        stringBuilder = new StringBuilder();
    }

    @Override
    public String val() {
        return _val;
    }
    @Override
    public String type() {
        return _type;
    }

    @Override
    public String toXML() {
        stringBuilder.append("<" + _type + "> ");
        stringBuilder.append(_val);
        stringBuilder.append(" </" + _type + ">\n");
        return stringBuilder.toString();
    }
}
