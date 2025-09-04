//
// Loop: g = g + A[i];
//  i = i + j;
//  if ( i != h) go to Loop;
//
.data
// Data address range begin at 0
A: .word 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119
g: .word 0
h: .word 20
i: .word 0
j: .word 1
.code
// $s1=g, $s2=h, $s3=i, $s4 = j
 lw    $s1,g($zero)
 lw    $s2,h($zero)
 lw    $s3,i($zero)
 lw    $s4,j($zero)
// $s5 = address of vector A
 add $s5, $zero, $zero
// $t1 = 4 * i
Loop: add $t1, $s3, $s3
 add $t1, $t1, $t1
// $t1 = & A [I]
 add $t1, $t1, $s5
 lw $t0, 0($t1)
// g = g + A[I]
 add $s1, $s1, $t0
// i = i + j
 add $s3, $s3, $s4
// desvia para Loop se i <> j
 bne $s3, $s2, Loop
