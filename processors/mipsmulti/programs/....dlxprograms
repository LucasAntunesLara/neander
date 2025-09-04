//
// void strcpy ( char x[], char y[])
// {
// int i;
// i = 0;
// while ((x[i] = y[i]) != 0) i = i + 1;
// }
// data address range begin at address 0
.data
V: .byte 81, 82, 83, 84, 85, 0
// Y is at address 8, since V is completed with zeros to occupy N words
Y: .byte 0 
.code
// stack pointer = 100
 addi  $sp, $zero, 100
// The arguments to be passed to the function are stored in the registers $a0 (x) and $a1 (y)
 add $a1,$zero,$zero
  addi $a0, $zero, 9
// Call strcpy function
 jal strcpy
 nop
 nop
 nop
 nop
 nop
//
// strcpy function
strcpy: addi $sp, $sp, -4
 sw $s0, 4($sp)
 add $s0, $zero, $zero
L1: add $t1, $a1, $s0
 lb $t2, 0($t1)
 add $t3, $a0, $s0
 sb $t2, 0($t3)
 addi $s0, $s0, 1
 bne $t2, $zero, L1
 lw $s0, 4($sp)
 add $sp, $sp, 4
 jr $ra

