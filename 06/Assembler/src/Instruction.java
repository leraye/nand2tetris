/**
 * @author heqing.ye
 * @ClassName: Instruction
 * @Description: TODO
 * @date 8/14/21
 */
public interface Instruction {

    public static String A_TYPE = "^@[a-zA-Z0-9_:\\.$]+";

    public static String LABEL = "^\\([a-zA-Z_:\\.$][a-zA-Z0-9_:\\.$]*\\)$";

    public final static Integer SYMBOL = 10;
    public final static Integer CONSTANT = 11;

    public final static Integer JUMP = 0;
    public final static Integer ASSIGNMENT = 1;

    Integer getType();

    String getDest();

    String getValue();

    String getJump();
}
