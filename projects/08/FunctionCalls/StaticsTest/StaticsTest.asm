@256
D=A
@SP
M=D
@Sys.init$ret.0
D=A
@SP
A=M
M=D
@SP
M=M+1
@TEMPORARY:1
D=A
@GLOBAL_FUNC:CALL
0;JMP
(TEMPORARY:1)
@5
D=A
@SP
D=M-D
@ARG
M=D
@Sys.init
0;JMP
(Sys.init$ret.0)
(Class1.set)
@ARG
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@Class1.0
M=D
@ARG
A=M+1
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@Class1.1
M=D
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@GLOBAL_FUNC:RETURN
0;JMP
(Class1.get)
@Class1.0
D=M
@SP
A=M
M=D
@SP
M=M+1
@Class1.1
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@R13
M=D
@SP
AM=M-1
D=M
@R13
D=D-M
@SP
A=M
M=D
@SP
M=M+1
@GLOBAL_FUNC:RETURN
0;JMP
(Sys.init)
@6
D=A
@SP
A=M
M=D
@SP
M=M+1
@8
D=A
@SP
A=M
M=D
@SP
M=M+1
@Class1.set$ret.2
D=A
@SP
A=M
M=D
@SP
M=M+1
@TEMPORARY:3
D=A
@GLOBAL_FUNC:CALL
0;JMP
(TEMPORARY:3)
@7
D=A
@SP
D=M-D
@ARG
M=D
@Class1.set
0;JMP
(Class1.set$ret.2)
@SP
AM=M-1
D=M
@5
M=D
@23
D=A
@SP
A=M
M=D
@SP
M=M+1
@15
D=A
@SP
A=M
M=D
@SP
M=M+1
@Class2.set$ret.4
D=A
@SP
A=M
M=D
@SP
M=M+1
@TEMPORARY:5
D=A
@GLOBAL_FUNC:CALL
0;JMP
(TEMPORARY:5)
@7
D=A
@SP
D=M-D
@ARG
M=D
@Class2.set
0;JMP
(Class2.set$ret.4)
@SP
AM=M-1
D=M
@5
M=D
@Class1.get$ret.6
D=A
@SP
A=M
M=D
@SP
M=M+1
@TEMPORARY:7
D=A
@GLOBAL_FUNC:CALL
0;JMP
(TEMPORARY:7)
@5
D=A
@SP
D=M-D
@ARG
M=D
@Class1.get
0;JMP
(Class1.get$ret.6)
@Class2.get$ret.8
D=A
@SP
A=M
M=D
@SP
M=M+1
@TEMPORARY:9
D=A
@GLOBAL_FUNC:CALL
0;JMP
(TEMPORARY:9)
@5
D=A
@SP
D=M-D
@ARG
M=D
@Class2.get
0;JMP
(Class2.get$ret.8)
(Sys.init$WHILE)
@Sys.init$WHILE
0;JMP
(Class2.set)
@ARG
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@Class2.0
M=D
@ARG
A=M+1
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@Class2.1
M=D
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@GLOBAL_FUNC:RETURN
0;JMP
(Class2.get)
@Class2.0
D=M
@SP
A=M
M=D
@SP
M=M+1
@Class2.1
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@R13
M=D
@SP
AM=M-1
D=M
@R13
D=D-M
@SP
A=M
M=D
@SP
M=M+1
@GLOBAL_FUNC:RETURN
0;JMP
(GLOBAL_CONST:END)
@GLOBAL_CONST:END
0;JMP
(GLOBAL_FUNC:CALL)
@R14
M=D
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@LCL
M=D
@R14
A=M
0;JMP
(GLOBAL_FUNC:RETURN)
@5
D=A
@LCL
A=M-D
D=M
@R15
M=D
@SP
AM=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M+1
@SP
M=D
@LCL
A=M-1
D=M
@THAT
M=D
@2
D=A
@LCL
A=M-D
D=M
@THIS
M=D
@3
D=A
@LCL
A=M-D
D=M
@ARG
M=D
@4
D=A
@LCL
A=M-D
D=M
@LCL
M=D
@R15
A=M
0;JMP
