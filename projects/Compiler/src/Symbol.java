import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author heqing.ye
 * @ClassName: Symbol
 * @Description: TODO
 * @date 8/26/21
 */
public class Symbol implements Token{

    private String _symbol;
    private String _type = Token.SYMBOL;
    private StringBuilder stringBuilder;

    public Symbol(char c){
        _symbol = Character.toString(c);
        stringBuilder = new StringBuilder();
    }

    @Override
    public String val() {
        return _symbol;
    }

    @Override
    public String type() {
        return _type;
    }

    @Override
    public String toXML() {
        stringBuilder.append("<" + _type + "> ");
        stringBuilder.append(_symbol);
        stringBuilder.append(" </" + _type + ">\n");
        return stringBuilder.toString();
    }
}
