	ORG 10

//Carrega res
loop:	LDA res
	//Adiciona mul1 a res
	ADD mul1
	//Armazena isso em res
	STA res
	//Carrega mul2
	LDA mul2
	//Adiciona dec a mul2
	ADD dec
	//Armazena isso na memória
	STA mul2
	//Enquanto o acumulador não for zero, mantém o loop,
	// somando de forma a emular uma multiplicação
	JNZ loop
fim:	HLT

//Variáveis
mul1	DEF BYTE=10
mul2	DEF BYTE=5
dec	DEF BYTE=255
res	DEF BYTE=0