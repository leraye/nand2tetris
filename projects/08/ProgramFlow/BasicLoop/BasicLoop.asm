@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
(BasicLoop$LOOP_START)
@ARG
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
@LCL
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
@R13
M=D
@SP
AM=M-1
D=M
@R13
D=D+M
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
@ARG
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
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
@ARG
D=M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
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
@BasicLoop$LOOP_START
D;JNE
@LCL
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
(GLOBAL_CONST:END)
@GLOBAL_CONST:END
0;JMP