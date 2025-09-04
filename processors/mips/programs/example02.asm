//
// g = h + A[8]
//
.data
// Data address range begin at 0
g:   .word 3
h:   .word 7
A: .word 100, 101, 102, 103, 104, 105, 106, 107, 108, 109
.code
// $s3 = address of vector A
addi $s3, $zero, 8
// $s1=g e $s2=h
lw $s1,g($zero)
lw $s2,h($zero)
// $t0 = A[8]
lw $t0,8($s3)
// g = h + A[8]
add $s1, $s2, $t0

