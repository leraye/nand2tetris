/**
 * @author heqing.ye
 * @ClassName: Identifier
 * @Description: TODO
 * @date 8/26/21
 */
public class Identifier implements Token{

    private String _varName;
    private String _type = Token.IDENTIFIER;
    private StringBuilder stringBuilder;

    public Identifier(String name){
        _varName = name;
        stringBuilder = new StringBuilder();
    }

    @Override
    public String val() {
        return _varName;
    }

    @Override
    public String type() {
        return _type;
    }

    @Override
    public String toXML() {
        stringBuilder.append("<" + _type + "> ");
        stringBuilder.append(_varName);
        stringBuilder.append(" </" + _type + ">\n");
        return stringBuilder.toString();
    }
}
