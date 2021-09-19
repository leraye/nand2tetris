import java.util.*;

/**
 * @author heqing.ye
 * @ClassName: CompilationEngine
 * @Description: TODO
 * @date 8/25/21
 */
public class CompilationEngine {

    private SymbolTable classTable, localTable;
    private String _className;
    private VMWriter vmWriter;
    private Integer _counter;
    private ListIterator<Token> _it;
    private List<Token> _tokens;
    private final static String WHILE = "WHILE_LABEL";
    private final static String IF = "IF_LABEL";
    private final static String ELSE = "ELSE_LABEL";

    public CompilationEngine(List<Token> tokens, String outputFile){
        _tokens = tokens;
        _it = _tokens.listIterator();
        classTable = new SymbolTable();
        localTable = new SymbolTable();
        vmWriter = new VMWriter(outputFile);
        _counter = 0;
    }

    private String nextLabel(String label){
        return label + "$" + Integer.toString(_counter++);
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/6/21
     * @Title: compileClass
     * @Description: 'class' className '{' classVarDec* subroutineDec* '}'
     * @param
     * @return void    返回类型
     * @throws
     */
    public void compileClass() throws RuntimeException{
        boolean endOfClass = false;
        while(_it.hasNext()){
            Token token = _it.next();
            switch(token.type()){
                case "keyword":
                    switch(token.val()){
                        case "field": case "static":
                            compileClassVarDec(token.val());
                            break;
                        case "constructor": case "function": case "method":
                            compileSubroutineDec(token.val());
                            break;
                        case "class":
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileClass: invalid keyword " + token.val());
                    }
                    break;
                case "identifier":
                    _className = token.val();
                    break;
                case "symbol":
                    switch(token.val()){
                        case "{":
                            break;
                        case "}":
                            endOfClass = true;
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileClass: invalid symbol " + token.val());
                    }
                    break;
                default:
                    throw new RuntimeException("Syntax error in compileClass: invalid token " + token.toXML());
            }
            if(endOfClass){
                break;
            }
        }
        vmWriter.close();
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/6/21
     * @Title: compileClassVarDec
     * @Description: ('static' | 'field' | 'var') type varName (',' varName)* ';'
     * @param kind    参数
     * @return void    返回类型
     * @throws
     */
    private void compileClassVarDec(String kind) throws RuntimeException{
        boolean endOfDec = false;
        String type = null;
        while(_it.hasNext()){
            Token curr = _it.next();
            if(type == null){
                switch(curr.type()){
                    case "keyword":
                        switch(curr.val()){
                            case "int": case "char": case "boolean":
                                type = curr.val();
                                break;
                            default:
                                throw new RuntimeException("Syntax error in compileClassVarDec: unsupported type " + curr.val());
                        }
                        break;
                    case "identifier":
                        type = curr.val();
                        break;
                    default:
                        throw new RuntimeException("Syntax error in compileClassVarDec: invalid token " + curr.toXML());
                }
            }else{
                switch(curr.type()){
                    case "identifier":
                        if(kind.equals("var")){
                            localTable.add(curr.val(), type, kind);
                        }else{
                            classTable.add(curr.val(), type, kind);
                        }
                        break;
                    case "symbol":
                        switch(curr.val()){
                            case ";":
                                endOfDec = true;
                                break;
                            case ",":
                                break;
                            default:
                                throw new RuntimeException("Syntax error in compileClassVarDec: invalid symbol " + curr.val());
                        }
                        break;
                    default:
                        throw new RuntimeException("Syntax error in compileClassVarDec: invalid token " + curr.toXML());
                }
            }
            if(endOfDec){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/6/21
     * @Title: compileSubroutineDec
     * @Description: ('function' | 'method' | 'constructor') ('void' | type) funcName '(' param list ')' funcBody
     * @param kind    参数
     * @return void    返回类型
     * @throws
     */
    private void compileSubroutineDec(String kind) throws RuntimeException{
        String returnType = null;
        String funcName = null;
        boolean endOfDec = false;
        while(_it.hasNext()){
            Token curr = _it.next();
            if(returnType == null){
                switch(curr.type()){
                    case "keyword":
                        switch(curr.val()){
                            case "void": case "int": case "char": case "boolean":
                                returnType = curr.val();
                                break;
                            default:
                                throw new RuntimeException("Syntax error in compileSubroutineDec: invalid return type " + curr.val());
                        }
                        break;
                    case "identifier":
                        returnType = curr.val();
                        break;
                    default:
                        throw new RuntimeException("Syntax error in compileSubroutineDec: invalid token " + curr.toXML());
                }
            }else{
                switch(curr.type()){
                    case "identifier":
                        funcName = curr.val();
                        break;
                    case "symbol":
                        switch(curr.val()){
                            case "(":
                                if(kind.equals("method")){
                                    localTable.add("this", _className, "arg");
                                }
                                compileParameterList();
                                break;
                            case "{":
                                compileSubroutineBody(funcName, kind);
                                localTable.reset();
                                endOfDec = true;
                                break;
                            case ")":
                                break;
                            default:
                                throw new RuntimeException("Syntax error in compileSubroutineDec: invalid symbol " + curr.val());
                        }
                        break;
                    default:
                        throw new RuntimeException("Syntax error in compileSubroutineDec: invalid token " + curr.toXML());
                }
            }
            if(endOfDec){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/5/21
     * @Title: compileExpressionList
     * @Description: (expr(',' expr)*)?
     * @param
     * @return int    返回类型
     * @throws
     */
    private int compileExpressionList() throws RuntimeException{
        int numParam = 0;
        while(_it.hasNext()){
            Token curr = _it.next();
            if(curr.type().equals("symbol")){
                if(curr.val().equals(")")){
                    _it.previous();
                    break;
                }
                if(curr.val().equals(",")){
                    continue;
                }
            }
            _it.previous();
            compileExpression(Set.of(",", ")"));
            ++numParam;
        }
        return numParam;
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileExpression
     * @Description: term (op term)*
     * @param endSymbols    参数
     * @return void    返回类型
     * @throws RuntimeException
     */
    private void compileExpression(Set<String> endSymbols) throws RuntimeException{
        String operator = null;

        while(_it.hasNext()){
            compileTerm(_it);
            if(operator != null){
                switch(operator){
                    case "+":
                        vmWriter.writeArithmetic("add");
                        break;
                    case "-":
                        vmWriter.writeArithmetic("sub");
                        break;
                    case "*":
                        vmWriter.writeCall("Math.multiply", 2);
                        break;
                    case "/":
                        vmWriter.writeCall("Math.divide", 2);
                        break;
                    case "|":
                        vmWriter.writeArithmetic("or");
                        break;
                    case "&":
                        vmWriter.writeArithmetic("and");
                        break;
                    case "<":
                        vmWriter.writeArithmetic("lt");
                        break;
                    case ">":
                        vmWriter.writeArithmetic("gt");
                        break;
                    case "=":
                        vmWriter.writeArithmetic("eq");
                        break;
                    default:
                        throw new RuntimeException("Syntax error in compileExpression: invalid operator " + operator);
                }
            }
            Token next = _it.next();
            if(next.type().equals("symbol")){
                if(endSymbols.contains(next.val())){
                    _it.previous();
                    break;
                }
                operator = next.val();
            }else{
                throw new RuntimeException("Syntax error in compileExpression: invalid token " + next.toXML());
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileTerm
     * @Description: constant | varName | varName[expr] | (expr) | (-~) term | funcName(Expr List) | varName.funcName(Expr List)
     * @param it    参数
     * @return void    返回类型
     * @throws RuntimeException
     */
    private void compileTerm(ListIterator<Token> it) throws RuntimeException{
        boolean endOfTerm = false;
        String funcCall = "";
        while(it.hasNext()){
            Token curr = it.next();
            switch(curr.type()){
                case "integerConstant":
                    vmWriter.writePush("constant", Integer.parseInt(curr.val()));
                    endOfTerm = true;
                    break;
                case "stringConstant":
                    String str = curr.val();
                    vmWriter.writePush("constant", str.length());
                    vmWriter.writeCall("String.new", 1);
                    for(int i = 0; i < str.length(); ++i){
                        vmWriter.writePush("constant", (int)str.charAt(i));
                        vmWriter.writeCall("String.appendChar", 2);
                    }
                    endOfTerm = true;
                    break;
                case "keyword":
                    switch(curr.val()){
                        case "false": case "null":
                            vmWriter.writePush("constant", 0);
                            endOfTerm = true;
                            break;
                        case "true":
                            vmWriter.writePush("constant", 1);
                            vmWriter.writeArithmetic("neg");
                            endOfTerm = true;
                            break;
                        case "this":
                            vmWriter.writePush("pointer", 0);
                            endOfTerm = true;
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileTerm: invalid keyword " + curr.val());
                    }
                    break;
                case "symbol":
                    switch(curr.val()){
                        case "-":
                            compileTerm(it);
                            vmWriter.writeArithmetic("neg");
                            endOfTerm = true;
                            break;
                        case "~":
                            compileTerm(it);
                            vmWriter.writeArithmetic("not");
                            endOfTerm = true;
                            break;
                        case "(":
                            compileExpression(Set.of(")"));
                            break;
                        case "]":
                            vmWriter.writeArithmetic("add");
                            vmWriter.writePop("pointer", 1);
                            vmWriter.writePush("that", 0);
                            endOfTerm = true;
                            break;
                        case ")":
                            endOfTerm = true;
                            break;
                        default:
                            it.previous();
                            endOfTerm = true;
                            break;
                    }
                    break;
                case "identifier":
                    Token next = it.next();
                    switch(next.type()){
                        case "symbol":
                            switch(next.val()){
                                case "[":
                                    Variable arr = localTable.find(curr.val());
                                    if(arr == null){
                                        arr = classTable.find(curr.val());
                                    }
                                    if(arr == null){
                                        throw new RuntimeException("Syntax error in compileTerm: undefined variable " + curr.val());
                                    }
                                    vmWriter.writePush(arr._segment, arr._index);
                                    compileExpression(Set.of("]"));
                                    break;
                                case "(":
                                    funcCall = funcCall + curr.val();
                                    compileSubroutineCall(funcCall);
                                    break;
                                case ".":
                                    funcCall = curr.val() + ".";
                                    break;
                                default:
                                    Variable var = localTable.find(curr.val());
                                    if(var == null){
                                        var = classTable.find(curr.val());
                                    }
                                    if(var == null){
                                        throw new RuntimeException("Syntax error in compileTerm: undeclared variable " + curr.val());
                                    }
                                    vmWriter.writePush(var._segment, var._index);
                                    it.previous();
                                    endOfTerm = true;
                                    break;
                            }
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileTerm: undeclared token " + next.val());
                    }
                    break;
                default:
                    throw new RuntimeException("Syntax error in compileTerm: invalid token " + curr.toXML());
            }
            if(endOfTerm){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/5/21
     * @Title: compileSubroutineCall
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param func    参数
     * @return void    返回类型
     * @throws
     */
    private void compileSubroutineCall(String func) throws RuntimeException{
        int arg = 0;
        if(func.contains(".")){
            String[] names = func.split("\\.");
            Variable var = localTable.find(names[0]);
            if(var == null){
                var = classTable.find(names[0]);
            }
            if(var != null){
                names[0] = var._type;
                arg = 1;
                vmWriter.writePush(var._segment, var._index);
            }
            func = names[0] + "." + names[1];
        }else{
            vmWriter.writePush("pointer", 0);
            arg = 1;
            func = _className + "." + func;
        }
        int num = compileExpressionList();
        vmWriter.writeCall(func, num + arg);
        return;
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileParameterList
     * @Description: (type varName(, type varName)*)?
     * @param
     * @return void    返回类型
     * @throws
     */
    private void compileParameterList() throws RuntimeException{
        boolean endOfParamList = false;
        while(_it.hasNext()){
            Token curr = _it.next();
            switch(curr.type()){
                case "symbol":
                    switch(curr.val()){
                        case ")":
                            _it.previous();
                            endOfParamList = true;
                            break;
                        case ",":
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileParameterList: invalid symbol " + curr.val());
                    }
                    break;
                case "keyword":
                    switch(curr.val()){
                        case "int": case "char": case "boolean":
                            Token next = _it.next();
                            if(!next.type().equals("identifier")){
                                throw new RuntimeException("Syntax error in compileParameterList: invalid token " + next.toXML());
                            }
                            localTable.add(next.val(), curr.val(), "arg");
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileParameterList: invalid keyword " + curr.val());
                    }
                    break;
                case "identifier":
                    Token next = _it.next();
                    if(!next.type().equals("identifier")){
                        throw new RuntimeException("Syntax error in compileParameterList: invalid token " + next.toXML());
                    }
                    localTable.add(next.val(), curr.val(), "arg");
                    break;
                default:
                    throw new RuntimeException("Syntax error in compileParameterList: invalid token " + curr.toXML());
            }
            if(endOfParamList){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 9/6/21
     * @Title: compileSubroutineBody
     * @Description: {varDec* statements}
     * @param funcName    参数
     * @return void    返回类型
     * @throws
     */
    private void compileSubroutineBody(String funcName, String kind) throws RuntimeException{
        boolean endOfBody = false;
        int startingPoint = _it.nextIndex();
        while(_it.hasNext()){
            Token curr = _it.next();
            if(curr.type().equals("keyword") && curr.val().equals("var")){
                compileClassVarDec(curr.val());
            }
            if(curr.type().equals("symbol") && curr.val().equals("}")){
                break;
            }
        }
        _it = _tokens.listIterator(startingPoint);
        vmWriter.writeFunction(_className + "." + funcName, localTable.getLocal());
        if(kind.equals("method")){
            vmWriter.writePush("argument", 0);
            vmWriter.writePop("pointer", 0);
        }
        if(kind.equals("constructor")){
            vmWriter.writePush("constant", classTable.getField());
            vmWriter.writeCall("Memory.alloc", 1);
            vmWriter.writePop("pointer", 0);
        }
        while(_it.hasNext()){
            Token curr = _it.next();
            switch(curr.type()){
                case "keyword":
                    switch(curr.val()){
                        case "var": case "int": case "char": case "boolean":
                            break;
                        case "let":
                            compileLet();
                            break;
                        case "do":
                            compileDo();
                            break;
                        case "return":
                            compileReturn();
                            break;
                        case "if":
                            compileIf();
                            break;
                        case "while":
                            compileWhile();
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileSubroutineBody: invalid keyword " + curr.val());
                    }
                    break;
                case "symbol":
                    switch(curr.val()){
                        case "}":
                            endOfBody = true;
                            break;
                        case ";": case ",":
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileSubroutineBody: invalid symbol " + curr.val());
                    }
                    break;
                case "identifier":
                    break;
                default:
                    throw new RuntimeException("Syntax error in compileSubroutineBody: invalid token " + curr.toXML());
            }
            if(endOfBody){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileLet
     * @Description: let varName ([expr])? = expr ;
     * @param
     * @return void    返回类型
     * @throws
     */
    private void compileLet() throws RuntimeException{
        boolean endOfStatement = false;
        boolean isArray = false;
        Variable var = null;
        while(_it.hasNext()){
            Token curr = _it.next();

            switch(curr.type()){
                case "symbol":
                    switch(curr.val()){
                        case "=":
                            compileExpression(Set.of(";"));
                            break;
                        case "[":
                            isArray = true;
                            vmWriter.writePush(var._segment, var._index);
                            compileExpression(Set.of("]"));
                            break;
                        case "]":
                            vmWriter.writeArithmetic("add");
                            break;
                        case ";":
                            if(isArray){
                                vmWriter.writePop("temp", 0);
                                vmWriter.writePop("pointer", 1);
                                vmWriter.writePush("temp", 0);
                                vmWriter.writePop("that", 0);
                            }else{
                                vmWriter.writePop(var._segment, var._index);
                            }
                            endOfStatement = true;
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileLet: invalid symbol " + curr.val());
                    }
                    break;
                case "identifier":
                    var = localTable.find(curr.val());
                    if(var == null){
                        var = classTable.find(curr.val());
                    }
                    if(var == null){
                        throw new RuntimeException("Syntax error in compileLet: undefined variable " + curr.val());
                    }
                default:
                    break;
            }
            if(endOfStatement){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileDo
     * @Description: do subroutineCall;
     * @param
     * @return void    返回类型
     * @throws
     */
    private void compileDo() throws RuntimeException{
        boolean endOfStatement = false;
        String func = "";
        while(_it.hasNext()){
            Token curr = _it.next();
            switch(curr.type()){
                case "symbol":
                    switch(curr.val()){
                        case "(":
                            compileSubroutineCall(func);
                            break;
                        case ";":
                            vmWriter.writePop("temp", 0);
                            endOfStatement = true;
                            break;
                        case ".":
                            func += curr.val();
                        default:
                            break;
                    }
                    break;
                case "identifier":
                    func += curr.val();
                    break;
                default:
                    break;
            }
            if(endOfStatement){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileReturn
     * @Description: return expr? ;
     * @param
     * @return void    返回类型
     * @throws
     */
    private void compileReturn() throws RuntimeException{
        boolean returnVoid = true;
        while(_it.hasNext()){
            Token curr = _it.next();
            if(curr.type().equals("symbol") && curr.val().equals(";")){
                if(returnVoid){
                    vmWriter.writePush("constant", 0);
                }
                break;
            }
            _it.previous();
            compileExpression(Set.of(";"));
            returnVoid = false;
        }
        vmWriter.writeReturn();
//        vmWriter.write("return\n");
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileWhile
     * @Description: while (expr) {statements}
     * @param
     * @return void    返回类型
     * @throws
     */
    private void compileWhile() throws RuntimeException{
        boolean endOfStatement = false;
        String l1 = nextLabel(WHILE);
        String l2 = nextLabel(WHILE);
        vmWriter.writeLabel(l1);
        while(_it.hasNext()){
            Token curr = _it.next();
            switch(curr.type()){
                case "symbol":
                    switch(curr.val()){
                        case "(":
                            compileExpression(Set.of(")"));
                            break;
                        case "{":
                            vmWriter.writeIf(l2);
                            compileStatements();
                            break;
                        case ")":
                            vmWriter.writeArithmetic("not");
                            break;
                        case "}":
                            vmWriter.writeGoto(l1);
                            vmWriter.writeLabel(l2);
                            endOfStatement = true;
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileWhile: invalid symbol " + curr.val());
                    }
                    break;
                default:
                    throw new RuntimeException("Syntax error in compileWhile: invalid token " + curr.toXML());
            }
            if(endOfStatement){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileIf
     * @Description: if (expr) {statements} (else {statements})?
     * @param
     * @return void    返回类型
     * @throws
     */
    private void compileIf() throws RuntimeException{
        boolean endOfStatement = false;
        String l1 = nextLabel(IF);
        String l2 = null;
        boolean if_block = true;
        while(_it.hasNext()){
            Token curr = _it.next();
            switch(curr.type()){
                case "symbol":
                    switch(curr.val()){
                        case "(":
                            compileExpression(Set.of(")"));
                            break;
                        case "{":
                            if(if_block){
                                vmWriter.writeIf(l1);
                            }
                            compileStatements();
                            break;
                        case ")":
                            vmWriter.writeArithmetic("not");
                            break;
                        case "}":
                            Token next = _it.next();
                            if(next.type().equals("keyword") && next.val().equals("else")){
                                l2 = nextLabel(ELSE);
                                vmWriter.writeGoto(l2);
                            }else{
                                _it.previous();
                                endOfStatement = true;
                            }
                            if(if_block){
                                vmWriter.writeLabel(l1);
                                if_block = false;
                            }else{
                                vmWriter.writeLabel(l2);
                            }
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileIf: invalid symbol " + curr.val());
                    }
                    break;
                default:
                    throw new RuntimeException("Syntax error in compileIf: invalid token " + curr.toXML());
            }
            if(endOfStatement){
                break;
            }
        }
    }

    /**
     * @Author: heqing.ye
     * @Date: 8/30/21
     * @Title: compileStatements
     * @Description: statement*
     * @param
     * @return void    返回类型
     * @throws
     */
    private void compileStatements() throws RuntimeException{
        boolean endOfStatements = false;
        while(_it.hasNext()){
            Token curr = _it.next();
            switch(curr.type()){
                case "keyword":
                    switch(curr.val()){
                        case "let":
                            compileLet();
                            break;
                        case "if":
                            compileIf();
                            break;
                        case "while":
                            compileWhile();
                            break;
                        case "return":
                            compileReturn();
                            break;
                        case "do":
                            compileDo();
                            break;
                        default:
                            throw new RuntimeException("Syntax error in compileStatements: invalid keyword " + curr.val());
                    }
                    break;
                case "symbol":
                    if(curr.val().equals("}")){
                        _it.previous();
                        endOfStatements = true;
                    }else{
                        throw new RuntimeException("Syntax error in compileStatements: invalid symbol " + curr.val());
                    }
                    break;
                default:
                    throw new RuntimeException("Syntax error in compileStatements: invalid token " + curr.toXML());
            }
            if(endOfStatements){
                break;
            }
        }
    }
}
