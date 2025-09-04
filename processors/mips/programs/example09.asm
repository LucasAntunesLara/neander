//
//  switch ( k) {
//  case 0: f = i + j; break;
//  case 1: f = g + h; break;
//  case 2: f = g - h; break;
//  case 3: f = i - j; break;
// }
//
.data
// Data address range begin at 0
JumpTable: .word 17, 19, 21, 23
f:    .word 1
g:    .word 100
h:    .word 50
i:    .word 200
j:    .word 25
k:    .word 3
.code
// address of JumpTable
 add $t4, $zero, $zero
// $s0=f, $s1 = g, $2 = h
 lw    $s0,f($zero)
 lw    $s1,g($zero)
   lw    $s2,h($zero)
// $s3=i, $s4 = j, $5 = k
   lw    $s3,i($zero)
   lw    $s4,j($zero)
   lw    $s5,k($zero)
// $t2 = 4
 addi $t2, $zero, 4
// if ( k < 0 || k > 3) then go to Exit
 slt $t3, $s5, $zero
 bne $t3, $zero, Exit
 slt  $t3, $s5, $t2
 beq  $t3, $zero, Exit
// $t1 = 4 * k
 add $t1, $s5, $s5
 add  $t1, $t1, $t1
// $t0 = JumpTable [k]
 add $t1, $t1, $t4
 lw $t0, 0($t1)
// it is actually jr $t0
 jr $t0
//
L0: add $s0, $s3, $s4
  j Exit
L1: add $s0, $s1, $s2
  j Exit
L2: sub $s0, $s1, $s2
  j Exit
L3: sub $s0, $s3, $s4
 j Exit
Exit: add $zero, $zero, $zero
