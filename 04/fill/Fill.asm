// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

	// Put your code here.

(LOOP)
	// read keyboard input
	@KBD
	D=M
	// if no key is pressed, paint the screen white
	@WHITE
	D;JEQ
	// else paint the screen black
	
	@256
	D=A
	@i // pointer for the outer loop
	M=D
	@OFFSET // offset from RAM[SCREEN]
	M=0
	// start of outer loop
	(BLACK_OUTER)
	// if all rows are filled
	@i
	D=M
	@STOP_BLACK_OUTER
	D;JEQ
	// start of inner loop
	@32
	D=A
	@j
	M=D
	// inner loop: fill a row
	(BLACK_INNER)
	// if the current row is filled
	@j
	D=M
	@STOP_BLACK_INNER
	D;JEQ

	@OFFSET
	D=M
	@SCREEN
	A=A+D
	M=-1
	@OFFSET
	M=D+1
	@j
	M=M-1
	@BLACK_INNER
	D;JMP
	
	(STOP_BLACK_INNER)
	@i
	M=M-1
	@BLACK_OUTER
	0;JMP

	(STOP_BLACK_OUTER)
	@LOOP
	0;JMP

	(WHITE)
	@256
	D=A
	@i
	M=D
	@OFFSET
	M=0
	(WHITE_OUTER)
	@i
	D=M
	@STOP_WHITE_OUTER
	D;JEQ

	@32
	D=A
	@j
	M=D
	(WHITE_INNER)
	@j
	D=M
	@STOP_WHITE_INNER
	D;JEQ

	@OFFSET
	D=M
	@SCREEN
	A=A+D
	M=0
	@OFFSET
	M=D+1
	@j
	M=M-1
	@WHITE_INNER
	D;JMP

	(STOP_WHITE_INNER)
	@i
	M=M-1
	@WHITE_OUTER
	0;JMP

	(STOP_WHITE_OUTER)
	@LOOP
	0;JMP
	
