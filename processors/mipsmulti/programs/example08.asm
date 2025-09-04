//
// while ( save [i] == k)
//  i = i + j;
//
.data
// Data address range begin at 0
save:  .word 5, 5, 5, 5, 5, 5, 5, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119
i:  .word 0
j:  .word 1
k:  .word 5
.code
// $s3=i, $s4 = j, $5 = k
  lw    $s3,i($zero)
  lw    $s4,j($zero)
  lw    $s5,k($zero)
// $s6 = address of vector save
  add $s6, $zero, $zero
// $t1 = 4 * i
Loop:  add $t1, $s3, $s3
  add $t1, $t1, $t1
// $t1 = & A [I]
  add $t1, $t1, $s6
  lw $t0, 0($t1)
// goto Exit if save[i] <> k
 bne $t0, $s5, Exit
// i = i + j
  add $s3, $s3, $s4
// go to Loop
 j Loop
Exit: add $zero, $zero, $zero