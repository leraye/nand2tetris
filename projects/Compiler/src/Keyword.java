import java.util.Set;

/**
 * @author heqing.ye
 * @ClassName: Keyword
 * @Description: TODO
 * @date 8/26/21
 */
public class Keyword implements Token{

    public final static Set<String> KEYWORD_SET =
            Set.of("class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean",
                    "void", "true", "false", "null", "this", "let", "do", "if", "else", "while", "return");

    private String _keyword;
    private String _type = Token.KEYWORD;
    private StringBuilder stringBuilder;

    Keyword(String keyword){
        _keyword = keyword;
        stringBuilder = new StringBuilder();
    }

    @Override
    public String val() {
        return _keyword;
    }

    @Override
    public String type() {
        return _type;
    }

    @Override
    public String toXML() {
        stringBuilder.append("<" + _type + "> ");
        stringBuilder.append(_keyword);
        stringBuilder.append(" </" + _type + ">\n");
        return stringBuilder.toString();
    }
}
