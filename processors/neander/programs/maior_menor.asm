ORG 100
    LDA maior
    SUB menor
    JN troca
HLT
troca: LDA maior
    STA aux
    LDA menor
    STA maior
    LDA aux
    STA menor
    HLT
maior DEF BYTE = 7
menor DEF BYTE = 70
aux DEF BYTE = 0