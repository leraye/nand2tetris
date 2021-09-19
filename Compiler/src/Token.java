import java.util.List;

/**
 * @author heqing.ye
 * @ClassName: Token
 * @Description: TODO
 * @date 8/26/21
 */
public interface Token {
    public final static String KEYWORD = "keyword";
    public final static String SYMBOL = "symbol";
    public final static String INT_CONSTANT = "integerConstant";
    public final static String STR_CONSTANT = "stringConstant";
    public final static String IDENTIFIER = "identifier";

    String val();

    String type();

    String toXML();
}
