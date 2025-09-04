.data
size:		.byte 26
inc:		.byte 1
vector1:	.byte 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0,1,2,3,4,5,6,7,8,9
vector2:	.byte 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0,1,2,3,4,5,6,7,8,9
vector3:	.byte 0
.code
	addi 	$sp, $zero, 124
	jal 	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	jal	copy
	jal	quad
	hlt
reset:	lb  	$t0,(size)$zero
	add	$t1,$zero,$zero
	lb	$t5,(inc)$zero
	lb	$t6,(size)$zero
	jr 	$ra
copy:	addi 	$sp, $sp, -4
	sw 	$ra, 0($sp)
	jal	reset
loopc:	addi  	$t0,$t0,-1
  	lb  	$s0,(vector3)$t1
  	sb  	$s0,(vector1)$t1
	addi  	$t1,$t1,1
  	bne  	$t0,$zero,loopc
	lw 	$ra, 0($sp)
	addi 	$sp, $sp, 4
	jr 	$ra
quad:  	addi 	$sp, $sp, -4
	sw 	$ra, 0($sp)
	jal	reset
loop:	addi  	$t0,$t0,-1
  	lb  	$s0,(vector1)$t1
  	lb  	$s1,(vector2)$t1
  	mult  	$t4,$s1,$s0
  	sb  	$t4,(vector3)$t1
	add  	$t1,$t1,$t5
  	bne  	$t0,$zero,loop
	lw 	$ra, 0($sp)
	addi 	$sp, $sp, 4
	jr 	$ra
