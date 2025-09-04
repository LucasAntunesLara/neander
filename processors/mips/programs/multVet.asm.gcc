.data
vector1: .word 1,2,3
vector2: .word 10,20,30
vector3: .word 0,0,0
size:  .word 3
.code
begin:  lw  $t0,(size)$zero
loop:  addi  $t0,$t0,-1
  add   $t2,$t0,$zero
  add  $t2,$t2,$t2
  add  $t2,$t2,$t2
  lw  $s0,(vector1)$t2
  lw  $s1,(vector2)$t2
  mult  $t1,$s1,$s0
  sw  $t1,(vector3)$t2
  bne  $t0,$zero,loop
