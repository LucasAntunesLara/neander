//
// int fact ( int n)
// {
//  if ( n < 1) return ( 1)
//  else return ( n * fact ( n - 1));
// }
//
.data
n: .word 12
.code
// stack pointer = 100
  addi $sp, $zero, 100
// Call fact function
 lw $a0,n($zero)
 jal fact
 nop
 nop
 nop
 nop
 nop
//
// Fatorial function
// save the return address and n onto the stack
//
fact: addi $sp, $sp, -8
 sw $ra, 4($sp)
 sw $a0, 0($sp)
// if ( n >= 1) then go to L1
 slti $t0, $a0, 1
 beq $t0, $zero, L1
// return (1)
 addi $v0, $zero, 1
 addi $sp, $sp, 8
 jr $ra
// call fact function with n-1
L1: addi $a0, $a0, -1
 jal fact
// restore values from the stack
 lw $a0, 0($sp)
 lw $ra, 4($sp)
 addi $sp, $sp, 8
// n * fact ( n - 1)
 mult $v0, $a0, $v0
// return to the caller
 jr $ra
