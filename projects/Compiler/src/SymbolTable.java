import java.util.*;

/**
 * @author heqing.ye
 * @ClassName: SymbolTable
 * @Description: TODO
 * @date 9/2/21
 */
public class SymbolTable {

    public final static Map<String, String> kindMapper = Map.of(
            "static", "static",
            "field", "this",
            "var", "local",
            "arg", "argument"
    );

    private int STATIC, LOCAL, ARG, THIS;
    private Map<String, Variable> indexMapper;

    public SymbolTable(){
        STATIC = 0;
        LOCAL = 0;
        ARG = 0;
        THIS = 0;
        indexMapper = new HashMap<>();
    }

    public void add(String name, String type, String kind){
        switch(kind){
            case "field":
                Variable fVar = new Variable(name, type, kind, THIS++);
                fVar._segment = kindMapper.get(kind);
                indexMapper.put(name, fVar);
                break;
            case "static":
                Variable sVar = new Variable(name, type, kind, STATIC++);
                sVar._segment = kindMapper.get(kind);
                indexMapper.put(name, sVar);
                break;
            case "var":
                Variable lVar = new Variable(name, type, kind, LOCAL++);
                lVar._segment = kindMapper.get(kind);
                indexMapper.put(name, lVar);
                break;
            case "arg":
                Variable aVar = new Variable(name, type, kind, ARG++);
                aVar._segment = kindMapper.get(kind);
                indexMapper.put(name, aVar);
                break;
            default:
                break;
        }
    }

    public Variable find(String name){
        if(indexMapper.containsKey(name)){
            return indexMapper.get(name);
        }else{
            return null;
        }
    }

    public int getLocal(){
        return LOCAL;
    }

    public int getField(){
        return THIS;
    }

    public void reset(){
        STATIC = 0;
        LOCAL = 0;
        ARG = 0;
        THIS = 0;
        indexMapper.clear();
    }

}
