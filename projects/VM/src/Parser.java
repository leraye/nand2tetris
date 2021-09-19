/**
 * @author heqing.ye
 * @ClassName: Parser
 * @Description: TODO
 * @date 8/16/21
 */
public class Parser {

    private static String[] pointers = {"THIS", "THAT"};
    public String currFile, currFunction;

    public Parser(){
        currFile = null;
        currFunction = null;
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/26/21
     * @Title: parse
     * @Description:
     * @param currLine    参数
     * @return Command    返回类型
     * @throws
     */
    public Command parse(String currLine) throws IllegalArgumentException{
        currLine = trimLine(currLine);
        if(currLine.isEmpty()){
            return null;
        }
        String[] segments = splitBySpaces(currLine);
        // 操作分类
        String command = segments[0];
        switch(command){
            case "push": case "pop":
                if(segments.length != 3){
                    throw new IllegalArgumentException("Invalid syntax: " + currLine + ", two arguments expected.");
                }
                String arg1 = segments[1];
                Integer arg2 = Integer.parseInt(segments[2]);
                return parsePushPop(command, arg1, arg2);
            case "add": case "sub": case "neg": case "eq":
            case "gt": case "lt": case "and": case "or": case "not":
                if(segments.length != 1){
                    throw new IllegalArgumentException("Invalid syntax: " + currLine + ", no arguments needed.");
                }
                return new Arithmetic(command, null, null);
            case "label": case "goto": case "if-goto":
                if(segments.length != 2){
                    throw new IllegalArgumentException("Invalid syntax: " + currLine + ", one argument expected.");
                }
                if(currFunction != null){
                    return new Branching(command, segments[1], currFunction);
                }else{
                    return new Branching(command, segments[1], currFile);
                }
            case "call": case "function":
                if(segments.length != 3){
                    throw new IllegalArgumentException("Invalid syntax: " + currLine + ", two arguments expected.");
                }
                if(command.equals("function")){
                    currFunction = segments[1];
                }
                return new Function(command, segments[1], Integer.parseInt(segments[2]));
            case "return":
                if(segments.length != 1){
                    throw new IllegalArgumentException("Invalid syntax: " + currLine + ", no arguments needed.");
                }
                return new Function(command, null, null);
            default:
                throw new IllegalArgumentException("Invalid syntax: " + currLine + ", unrecognizable command type.");
        }
    }

    //=================================================================================

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: trimLine
     * @Description: 去掉左右空格以及注释
     * @param line    参数
     * @return java.lang.String    返回类型
     * @throws
     */
    private String trimLine(String line){
        line = line.trim();
        String trimmed = line.replaceAll("([^/]*)//.*", "$1");

        return trimmed.trim();
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: splitBySpaces
     * @Description: 字符串按照空格分开
     * @param s    参数
     * @return java.lang.String[]    返回类型
     * @throws
     */
    private String[] splitBySpaces(String s){
        return s.split("\\s+");
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/19/21
     * @Title: parsePushPop
     * @Description: 创建PushPop类
     * @param opCode, arg1, arg2    参数
     * @return Command    返回类型
     * @throws
     */
    private Command parsePushPop(String opCode, String arg1, Integer arg2){
        Command command = null;
        switch(arg1){
            case "local": command = new PushPop(opCode, "LCL", arg2); break;
            case "argument": command = new PushPop(opCode, "ARG", arg2); break;
            case "this": command = new PushPop(opCode, "THIS", arg2); break;
            case "that": command = new PushPop(opCode, "THAT", arg2); break;
            case "pointer": command = new PushPop(opCode, pointers[arg2], null); break;
            case "temp": command = new PushPop(opCode, Integer.toString(5 + arg2), null); break;
            case "constant": command = new PushPop(opCode, "CONST", arg2); break;
            case "static": arg1 = currFile + "." + Integer.toString(arg2); arg2 = null;
            default: command = new PushPop(opCode, arg1, arg2); break;
        }
        return command;
    }
}
