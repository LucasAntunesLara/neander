//
// g = h + A[i]
//
.data
// Data address range begin at 0
A: .word 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114
g: .word 0
h: .word 0
i: .word 1
.code
// $s3 = address of vector A
add  $s3, $zero, $zero
// $s1=g, $s2=h, $s4 = i
lw    $s1,g($zero)
lw    $s2,h($zero)
lw    $s4,i($zero)
// 4 * i
add    $t1, $s4, $s4
add    $t1, $t1, $t1
// A[i] address
add    $t1, $t1, $s3
// $t0 = A[i]
lw $t0,0,($t1)
// g = h + A[i]
add $s1, $s2, $t0

