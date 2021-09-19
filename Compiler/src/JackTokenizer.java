import java.util.ArrayList;
import java.util.List;

/**
 * @author heqing.ye
 * @ClassName: JackTokenizer
 * @Description: TODO
 * @date 8/25/21
 */
public class JackTokenizer {

    private boolean inBlock;
    private StringBuilder currLine;
    private List<String> text;

    public JackTokenizer(){
        inBlock = false;
        currLine = new StringBuilder();
        text = new ArrayList<>();
    }

    public void trimLine(String line){
        if(line.isEmpty()){
            return;
        }
        for(int i = 0; i < line.length();){
            if(i + 1 < line.length()){
                String lookahead = line.substring(i, i + 2);
                if(lookahead.equals("//")){
                    if(inBlock){
                        ++i;
                    }else{
                        break;
                    }
                }else if(lookahead.equals("/*")){
                    if(inBlock){
                        ++i;
                    }else{
                        inBlock = true;
                        i = i + 2;
                    }
                }else if(lookahead.equals("*/")){
                    if(inBlock){
                        inBlock = false;
                        i = i + 2;
                    }else{
                        currLine.append(line.charAt(i));
                        ++i;
                    }
                }else{
                    if(!inBlock){
                        currLine.append(line.charAt(i));
                    }
                    ++i;
                }
            }else{
                if(!inBlock){
                    currLine.append(line.charAt(i));
                }
                ++i;
            }
        }
        if(!inBlock){
            if(!currLine.isEmpty()){
                text.add(currLine.toString());
            }
            currLine = new StringBuilder();
        }
    }

    public List<Token> parseLine(String line){
        int j;
        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < line.length();){
            switch(line.charAt(i)){
                case '(': case ')': case '{': case '}': case '[': case ']':
                case '.': case ',': case ';': case '+': case '-': case '*': case '/':
                case '&': case '|': case '<': case '>': case '=': case '~':
                    tokens.add(new Symbol(line.charAt(i)));
                    ++i;
                    break;
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    j = i + 1;
                    while(j < line.length() && Character.isDigit(line.charAt(j))){
                        ++j;
                    }
                    tokens.add(new Constant(line.substring(i, j), Token.INT_CONSTANT));
                    i = j;
                    break;
                case '_':
                    j = i + 1;
                    while(j < line.length() && isPartOfIdentifier(line.charAt(j))){
                        ++j;
                    }
                    tokens.add(new Identifier(line.substring(i, j)));
                    i = j;
                    break;
                case '"':
                    j = i + 1;
                    while(j < line.length() && line.charAt(j) != '"'){
                        ++j;
                    }
                    tokens.add(new Constant(line.substring(i + 1, j), Token.STR_CONSTANT));
                    i = j + 1;
                    break;
                default:
                    if(Character.isAlphabetic(line.charAt(i))){
                        j = i + 1;
                        while(j < line.length() && Character.isAlphabetic(line.charAt(j))){
                            ++j;
                        }
                        String word = line.substring(i, j);
                        if(Keyword.KEYWORD_SET.contains(word)){
                            tokens.add(new Keyword(word));
                        }else if(j == line.length() || !isPartOfIdentifier(line.charAt(j))){
                            tokens.add(new Identifier(word));
                        }else{
                            while(j < line.length() && isPartOfIdentifier(line.charAt(j))){
                                ++j;
                            }
                            word = line.substring(i, j);
                            tokens.add(new Identifier(word));
                        }
                        i = j;
                    }else{
                        ++i;
                    }
                    break;
            }
        }
        return tokens;
    }

    public void reset(){
        text.clear();
    }

    public final List<String> getText() {
        return text;
    }

    private boolean isPartOfIdentifier(char c){
        return (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_');
    }
}
