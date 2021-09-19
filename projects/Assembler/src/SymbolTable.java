import java.util.HashMap;
import java.util.Map;

/**
 * @author heqing.ye
 * @ClassName: SymbolTable
 * @Description: SymbolTable maps symbols to their respective addresses
 * @date 8/14/21
 */
public class SymbolTable {

    /**
     * predefined symbols
     */
    public final static Map<String, Integer> PREDEFINED = Map.ofEntries(
            Map.entry("R0", 0),
            Map.entry("R1", 1),
            Map.entry("R2", 2),
            Map.entry("R3", 3),
            Map.entry("R4", 4),
            Map.entry("R5", 5),
            Map.entry("R6", 6),
            Map.entry("R7", 7),
            Map.entry("R8", 8),
            Map.entry("R9", 9),
            Map.entry("R10", 10),
            Map.entry("R11", 11),
            Map.entry("R12", 12),
            Map.entry("R13", 13),
            Map.entry("R14", 14),
            Map.entry("R15", 15),
            Map.entry("SP", 0),
            Map.entry("LCL", 1),
            Map.entry("ARG", 2),
            Map.entry("THIS", 3),
            Map.entry("THAT", 4),
            Map.entry("SCREEN", 16384),
            Map.entry("KBD", 24576)
    );

    private Map<String, Integer> table;

    public SymbolTable(){
        table = new HashMap<>();
    }

    public void addEntry(String symbol, int address){
        table.put(symbol, address);
    }

    public boolean contains(String symbol){
        return table.containsKey(symbol);
    }

    public Integer getAddress(String symbol){
        return table.get(symbol);
    }
}
