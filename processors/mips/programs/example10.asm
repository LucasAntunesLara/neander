//
// int leaf_example ( int g, int h, int i, int j)
// {
//  int f;
//  f = ( g + h) - ( i + j);
//  return f;
// }
//
.data
// Data address range begin at 0
g:    .word 100
h:    .word 100
i:    .word 75
j:    .word 75
.code
// stack pointer = 100
  addi $sp, $zero, 100
// $a0=g, $a1 = h, $a2 = i, $a3 = j
  lw    $a0,g($zero)
  lw    $a1,h($zero)
  lw    $a2,i($zero)
  lw    $a3,j($zero)
// save the registers to be used onto the stack
leaf_example: addi  $sp, $sp, -12
  sw $t1, 8($sp)
  sw $t0, 4($sp)
  sw $s0, 0($sp)
// f ($s0) = ( g + h) - ( i + j);
  add $t0, $a0, $a1
  add $t1, $a2, $a3
  sub $s0, $t0, $t1
  add $v0, $s0, $zero
// restore the registers
    lw $s0, 0($sp)
    lw $t0, 4($sp)
    lw $t1, 8($sp)
    addi $sp, $sp, 12
// return to the caller
  jr $ra
