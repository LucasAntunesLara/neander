//
// f = (g+h) - (i+j)
//
.data
g:  .word 3
h:  .word 3
i:  .word 1
j:   .word 1
.code
// $s1=g, $s2=h, $s3=i e $s4=j
lw   $s1,(g)$zero
lw   $s2,(h)$zero
lw   $s3,(i)$zero
lw   $s4,(j)$zero
// $t0 = g + h
add  $t0, $s1, $s2
// $t1 = i + j
add   $t1, $s3, $s4
// $s0 (ou f) = $t0 - $t1 
sub   $s0, $t0, $t1 

