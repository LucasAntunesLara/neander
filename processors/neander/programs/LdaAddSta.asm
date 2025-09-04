// Em Java:
//
//	x = <value>;
//	y = <value>;
//	x = x + y;
//
	ORG 10
loop:	LDA X
	ADD Y
	STA X
fim:	HLT
X	DEF BYTE=10
Y	DEF BYTE=5