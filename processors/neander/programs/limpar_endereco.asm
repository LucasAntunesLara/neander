//Fazer um programa que limpe
// os endereços de 100 a 110.
//Limpar significa colocar o valor -1 nestes endereços.
ORG 10
loop:	LDA VALUE
    STA FIRST_A
    LDA FIRST_A
    ADD INC
    STA FIRST_A
    LDA LAST_A
    SUB FIRST_A
    JN fim
    JMP loop
fim:	HLT
FIRST_A DEF BYTE=100
LAST_A DEF BYTE=110
VALUE DEF=-1
INC DEF BYTE=1
    
