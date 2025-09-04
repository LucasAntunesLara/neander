	.file	1 "matriz.c"

 # GNU C 2.7.2.3 [AL 1.1, MM 40] DECstation running ultrix compiled by GNU C

 # Cc1 defaults:

 # Cc1 arguments (-G value = 8, Cpu = 3000, ISA = 1):
 # -quiet -dumpbase -o

gcc2_compiled.:
__gnu_compiled_c:
	.rdata
	.align	2
$LC0:
	.word	121
	.word	25
	.word	123
	.word	21
	.word	22
	.word	23
	.word	24
	.word	25
	.word	26
	.word	27
	.word	134
	.word	333
	.word	316
	.word	37
	.word	36
	.word	35
	.word	34
	.word	33
	.word	32
	.word	31
	.word	117
	.word	118
	.word	119
	.word	11
	.word	12
	.word	13
	.word	14
	.word	15
	.word	16
	.word	17
	.word	141
	.word	45
	.word	143
	.word	41
	.word	42
	.word	43
	.word	44
	.word	45
	.word	46
	.word	47
	.word	154
	.word	353
	.word	156
	.word	57
	.word	56
	.word	55
	.word	54
	.word	53
	.word	52
	.word	51
	.word	167
	.word	618
	.word	169
	.word	61
	.word	62
	.word	63
	.word	64
	.word	65
	.word	66
	.word	67
	.word	181
	.word	85
	.word	183
	.word	81
	.word	82
	.word	83
	.word	84
	.word	85
	.word	86
	.word	87
	.word	194
	.word	339
	.word	196
	.word	97
	.word	96
	.word	95
	.word	94
	.word	93
	.word	92
	.word	91
	.word	17
	.word	18
	.word	19
	.word	1
	.word	2
	.word	3
	.word	4
	.word	5
	.word	6
	.word	7
	.word	177
	.word	178
	.word	179
	.word	71
	.word	72
	.word	73
	.word	74
	.word	75
	.word	76
	.word	77
	.sdata
	.align	2
$LC1:
	.ascii	"%d\n\000"
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,424,$31		# vars= 400, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,424
	sw	$31,420($sp)
	sw	$fp,416($sp)
	move	$fp,$sp
	jal	__main
	addu	$2,$fp,16
	la	$3,$LC0
	move	$4,$2
	move	$5,$3
	li	$6,0x00000190		# 400
	jal	memcpy
	addu	$4,$fp,16
	li	$5,0x0000000a		# 10
	li	$6,0x0000000a		# 10
	jal	maiorNaMatriz
	sw	$2,res1
	la	$4,$LC1
	lw	$5,res1
	jal	printf
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,420($sp)
	lw	$fp,416($sp)
	addu	$sp,$sp,424
	j	$31
	.end	main
	.align	2
	.globl	maiorNaMatriz
	.ent	maiorNaMatriz
maiorNaMatriz:
	.frame	$fp,24,$31		# vars= 16, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,24
	sw	$fp,16($sp)
	move	$fp,$sp
	sw	$4,24($fp)
	sw	$5,28($fp)
	sw	$6,32($fp)
	lw	$2,24($fp)
	sw	$2,12($fp)
	lw	$2,12($fp)
	lw	$3,0($2)
	sw	$3,8($fp)
	sw	$0,0($fp)
$L3:
	lw	$2,0($fp)
	lw	$3,28($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L6
	j	$L4
$L6:
	sw	$0,4($fp)
$L7:
	lw	$2,4($fp)
	lw	$3,32($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L10
	j	$L8
$L10:
	lw	$2,12($fp)
	lw	$3,0($2)
	lw	$2,8($fp)
	slt	$3,$2,$3
	beq	$3,$0,$L11
	lw	$2,12($fp)
	lw	$3,0($2)
	sw	$3,8($fp)
$L11:
	lw	$3,12($fp)
	addu	$2,$3,4
	move	$3,$2
	sw	$3,12($fp)
$L9:
	lw	$3,4($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,4($fp)
	j	$L7
$L8:
$L5:
	lw	$3,0($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,0($fp)
	j	$L3
$L4:
	lw	$2,8($fp)
	j	$L2
$L2:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,16($sp)
	addu	$sp,$sp,24
	j	$31
	.end	maiorNaMatriz
	.rdata
	.align	2
$LC2:
	.ascii	"size = %d\n\000"
	.text
	.align	2
	.globl	memcpy2
	.ent	memcpy2
memcpy2:
	.frame	$fp,32,$31		# vars= 8, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,32
	sw	$31,28($sp)
	sw	$fp,24($sp)
	move	$fp,$sp
	sw	$4,32($fp)
	sw	$5,36($fp)
	sw	$6,40($fp)
	la	$4,$LC2
	lw	$5,40($fp)
	jal	printf
	sw	$0,16($fp)
$L13:
	lw	$2,16($fp)
	lw	$3,40($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L16
	j	$L14
$L16:
	lw	$2,32($fp)
	addu	$3,$2,1
	sw	$3,32($fp)
	lw	$3,36($fp)
	addu	$4,$3,1
	sw	$4,36($fp)
	lbu	$3,0($3)
	sb	$3,0($2)
$L15:
	lw	$3,16($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,16($fp)
	j	$L13
$L14:
$L12:
	move	$sp,$fp			# sp not trusted here
	lw	$31,28($sp)
	lw	$fp,24($sp)
	addu	$sp,$sp,32
	j	$31
	.end	memcpy2
	.align	2
	.globl	sizeof2
	.ent	sizeof2
sizeof2:
	.frame	$fp,8,$31		# vars= 0, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,8
	sw	$fp,0($sp)
	move	$fp,$sp
	sw	$4,8($fp)
	lw	$2,8($fp)
	beq	$2,$0,$L19
	li	$3,0x00000001		# 1
	beq	$2,$3,$L20
	j	$L21
$L19:
	li	$2,0x00000001		# 1
	j	$L17
$L20:
	li	$2,0x00000004		# 4
	j	$L17
$L21:
	li	$2,-1			# 0xffffffff
	j	$L17
$L18:
$L17:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,0($sp)
	addu	$sp,$sp,8
	j	$31
	.end	sizeof2

	.comm	res1,4
