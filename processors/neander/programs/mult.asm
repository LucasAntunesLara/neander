	ORG 10
loop:	LDA res
	ADD mul1
	STA res
	LDA mul2
	ADD dec
	STA mul2
	JNZ loop
fim:	HLT
mul1	DEF BYTE=10
mul2	DEF BYTE=5
dec	DEF BYTE=255
res	DEF BYTE=0