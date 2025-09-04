The C programs must follow these conditions to be able to run on the MIPS simulator:

1. the function main must be the first function in the source code;
2. it is not possible to include standard header files, such as stdio.h and stlib.h, and to call functions from the standard C libraries. Functions such as sizeof, memcpy, strcpy and others must be provided by the programmer.
3. global variables should be declared and may receive values during the program execution with the goal of debugging the program. They will appear in the first addresses of the data memory.
4. the C programs must be compiled using the gcc compiler targeted to produce MIPS code. The compilation must result in a Assembly code to be opened by the simulator (option -S of the gcc). 
