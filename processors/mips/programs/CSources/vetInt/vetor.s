	.file	1 "vetor.c"

 # GNU C 2.7.2.3 [AL 1.1, MM 40] DECstation running ultrix compiled by GNU C

 # Cc1 defaults:

 # Cc1 arguments (-G value = 8, Cpu = 3000, ISA = 1):
 # -quiet -dumpbase -o

gcc2_compiled.:
__gnu_compiled_c:
	.globl	res1
	.sdata
	.align	2
res1:
	.word	-1
	.globl	res2
	.align	2
res2:
	.word	-1
	.globl	res3
	.align	2
res3:
	.word	-1
	.rdata
	.align	2
$LC0:
	.word	11
	.word	12
	.word	13
	.word	14
	.word	15
	.word	16
	.word	17
	.word	18
	.word	19
	.word	10
	.word	9
	.word	8
	.word	7
	.word	6
	.word	5
	.word	4
	.word	3
	.word	2
	.word	1
	.word	0
	.word	-1
	.word	-2
	.word	-3
	.word	-4
	.word	-5
	.word	-6
	.word	-7
	.word	-8
	.word	-9
	.word	-10
	.sdata
	.align	2
$LC1:
	.ascii	"%d\n\000"
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,144,$31		# vars= 120, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,144
	sw	$31,140($sp)
	sw	$fp,136($sp)
	move	$fp,$sp
	jal	__main
	addu	$2,$fp,16
	la	$3,$LC0
	move	$4,$2
	move	$5,$3
	li	$6,0x00000078		# 120
	jal	memcpy
	addu	$4,$fp,16
	li	$5,0x0000001e		# 30
	jal	maiorNoVet
	sw	$2,res1
	addu	$4,$fp,16
	li	$5,0x0000001e		# 30
	jal	menorNoVet
	sw	$2,res2
	addu	$4,$fp,16
	li	$5,0x0000001e		# 30
	jal	somaDoVet
	sw	$2,res3
	la	$4,$LC1
	lw	$5,res1
	jal	printf
	la	$4,$LC1
	lw	$5,res2
	jal	printf
	la	$4,$LC1
	lw	$5,res3
	jal	printf
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,140($sp)
	lw	$fp,136($sp)
	addu	$sp,$sp,144
	j	$31
	.end	main
	.align	2
	.globl	maiorNoVet
	.ent	maiorNoVet
maiorNoVet:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$5,20($fp)
	lw	$2,16($fp)
	lw	$3,0($2)
	sw	$3,0($fp)
	li	$2,0x00000001		# 1
	sw	$2,4($fp)
$L3:
	lw	$2,4($fp)
	lw	$3,20($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L6
	j	$L4
$L6:
	lw	$2,4($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,16($fp)
	addu	$2,$2,$3
	lw	$3,0($2)
	lw	$2,0($fp)
	slt	$3,$2,$3
	beq	$3,$0,$L7
	lw	$2,4($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,16($fp)
	addu	$2,$2,$3
	lw	$3,0($2)
	sw	$3,0($fp)
$L7:
$L5:
	lw	$3,4($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,4($fp)
	j	$L3
$L4:
	lw	$2,0($fp)
	j	$L2
$L2:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	maiorNoVet
	.align	2
	.globl	menorNoVet
	.ent	menorNoVet
menorNoVet:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$5,20($fp)
	lw	$2,16($fp)
	lw	$3,0($2)
	sw	$3,0($fp)
	li	$2,0x00000001		# 1
	sw	$2,4($fp)
$L9:
	lw	$2,4($fp)
	lw	$3,20($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L12
	j	$L10
$L12:
	lw	$2,4($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,16($fp)
	addu	$2,$2,$3
	lw	$3,0($2)
	lw	$2,0($fp)
	slt	$3,$3,$2
	beq	$3,$0,$L13
	lw	$2,4($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,16($fp)
	addu	$2,$2,$3
	lw	$3,0($2)
	sw	$3,0($fp)
$L13:
$L11:
	lw	$3,4($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,4($fp)
	j	$L9
$L10:
	lw	$2,0($fp)
	j	$L8
$L8:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	menorNoVet
	.align	2
	.globl	somaDoVet
	.ent	somaDoVet
somaDoVet:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$5,20($fp)
	sw	$0,0($fp)
	sw	$0,4($fp)
$L15:
	lw	$2,4($fp)
	lw	$3,20($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L18
	j	$L16
$L18:
	lw	$2,4($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,16($fp)
	addu	$2,$2,$3
	lw	$3,0($fp)
	lw	$2,0($2)
	addu	$3,$3,$2
	sw	$3,0($fp)
$L17:
	lw	$3,4($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,4($fp)
	j	$L15
$L16:
	lw	$2,0($fp)
	j	$L14
$L14:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	somaDoVet
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
$L20:
	lw	$2,16($fp)
	lw	$3,40($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L23
	j	$L21
$L23:
	lw	$2,32($fp)
	addu	$3,$2,1
	sw	$3,32($fp)
	lw	$3,36($fp)
	addu	$4,$3,1
	sw	$4,36($fp)
	lbu	$3,0($3)
	sb	$3,0($2)
$L22:
	lw	$3,16($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,16($fp)
	j	$L20
$L21:
$L19:
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
	beq	$2,$0,$L26
	li	$3,0x00000001		# 1
	beq	$2,$3,$L27
	j	$L28
$L26:
	li	$2,0x00000001		# 1
	j	$L24
$L27:
	li	$2,0x00000004		# 4
	j	$L24
$L28:
	li	$2,-1			# 0xffffffff
	j	$L24
$L25:
$L24:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,0($sp)
	addu	$sp,$sp,8
	j	$31
	.end	sizeof2
