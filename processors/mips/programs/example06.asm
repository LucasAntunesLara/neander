//
// if ( i == j) f = g + h;
// else f = g - h;
//
.data
// Data address range begin at 0
f: .word -1
g: .word 15
h: .word 5
i: .word 1
j: .word 2
.code
// $s0=f, $s1=g, $s2=h, $s3=i, $s4 = j
 lw     $s0,f($zero)
 lw     $s1,g($zero)
 lw    $s2,h($zero)
 lw    $s3,i($zero)
 lw    $s4,j($zero)
//
  bne $s3, $s4, Else
  add $s0, $s1, $s2
 j Exit
Else:  sub $s0, $s1, $s2
Exit:  add $zero, $zero, $zero
