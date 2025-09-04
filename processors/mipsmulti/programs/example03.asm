//
// A[12] = h + A[8]
//
.data
// Data address range begin at 0
A: .word 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, -1
h: .word 4
.code
// $s3 = address of vector A
add $s3, $zero, $zero
// $s2=h
lw   $s2,h($zero)
// $t0 = A[8]
lw   $t0,32($s3)
// $t0 = h + A[8]
add   $t0, $s2, $t0
// A[12] =  h + A[8]
sw   $t0,48($s3)

