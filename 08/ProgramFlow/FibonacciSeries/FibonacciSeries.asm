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
@THAT
M=D
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M+1
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
@2
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
(FibonacciSeries$MAIN_LOOP_START)
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
@FibonacciSeries$COMPUTE_ELEMENT
D;JNE
@FibonacciSeries$END_PROGRAM
0;JMP
(FibonacciSeries$COMPUTE_ELEMENT)
@THAT
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
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
@2
D=A
@THAT
D=D+M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
@THAT
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
D=D+M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@THAT
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
@FibonacciSeries$MAIN_LOOP_START
0;JMP
(FibonacciSeries$END_PROGRAM)
(GLOBAL_CONST:END)
@GLOBAL_CONST:END
0;JMP