	.file	1 "app1.c"

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
	.rdata
	.align	2
$LC0:
	.word	2
	.word	-113
	.word	-11
	.word	-12
	.word	-13
	.word	-14
	.word	-15
	.word	-16
	.word	-17
	.word	-18
	.word	-19
	.word	-10
	.word	-1
	.word	-3
	.word	-5
	.word	-7
	.word	-9
	.word	-2
	.word	-4
	.word	-6
	.word	-2
	.word	113
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
	.word	1
	.word	3
	.word	5
	.word	7
	.word	9
	.word	2
	.word	4
	.word	6
	.word	21
	.word	22
	.word	31
	.word	32
	.word	23
	.word	24
	.word	33
	.word	34
	.word	25
	.word	26
	.word	35
	.word	36
	.word	27
	.word	28
	.word	37
	.word	38
	.word	29
	.word	30
	.word	39
	.word	40
	.sdata
	.align	2
$LC1:
	.ascii	" %d \000"
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,272,$31		# vars= 248, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,272
	sw	$31,268($sp)
	sw	$fp,264($sp)
	move	$fp,$sp
	jal	__main
	addu	$2,$fp,16
	addu	$3,$fp,16
	la	$2,$LC0
	move	$4,$3
	move	$5,$2
	li	$6,0x000000f0		# 240
	jal	memcpy
	addu	$2,$fp,16
	sw	$2,res1
	addu	$2,$fp,52
	sw	$2,res2
	addu	$4,$fp,16
	li	$5,0x0000000a		# 10
	jal	ordena
	sw	$0,256($fp)
$L2:
	lw	$2,256($fp)
	slt	$3,$2,10
	bne	$3,$0,$L5
	j	$L3
$L5:
	lw	$2,256($fp)
	move	$3,$2
	sll	$2,$3,2
	addu	$3,$fp,16
	addu	$2,$2,$3
	la	$4,$LC1
	lw	$5,0($2)
	jal	printf
$L4:
	lw	$3,256($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,256($fp)
	j	$L2
$L3:
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,268($sp)
	lw	$fp,264($sp)
	addu	$sp,$sp,272
	j	$31
	.end	main
	.align	2
	.globl	ordena
	.ent	ordena
ordena:
	.frame	$fp,24,$31		# vars= 16, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,24
	sw	$fp,16($sp)
	move	$fp,$sp
	sw	$4,24($fp)
	sw	$5,28($fp)
	li	$2,0x00000001		# 1
	sw	$2,8($fp)
$L7:
	lw	$2,8($fp)
	li	$3,0x00000001		# 1
	beq	$2,$3,$L9
	j	$L8
$L9:
	sw	$0,8($fp)
	sw	$0,0($fp)
$L10:
	lw	$3,28($fp)
	addu	$2,$3,-1
	lw	$3,0($fp)
	slt	$2,$3,$2
	bne	$2,$0,$L13
	j	$L11
$L13:
	lw	$2,0($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,24($fp)
	addu	$2,$2,$3
	lw	$3,0($fp)
	move	$4,$3
	sll	$3,$4,2
	lw	$4,24($fp)
	addu	$3,$3,$4
	addu	$4,$3,4
	lw	$2,0($2)
	lw	$3,0($4)
	slt	$2,$3,$2
	beq	$2,$0,$L14
	lw	$2,0($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,24($fp)
	addu	$2,$2,$3
	lw	$3,0($2)
	sw	$3,4($fp)
	lw	$2,0($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,24($fp)
	addu	$2,$2,$3
	lw	$3,0($fp)
	move	$4,$3
	sll	$3,$4,2
	lw	$4,24($fp)
	addu	$3,$3,$4
	addu	$4,$3,4
	lw	$3,0($4)
	sw	$3,0($2)
	lw	$2,0($fp)
	move	$3,$2
	sll	$2,$3,2
	lw	$3,24($fp)
	addu	$2,$2,$3
	addu	$3,$2,4
	lw	$2,4($fp)
	sw	$2,0($3)
	li	$2,0x00000001		# 1
	sw	$2,8($fp)
$L14:
$L12:
	lw	$3,0($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,0($fp)
	j	$L10
$L11:
	j	$L7
$L8:
$L6:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,16($sp)
	addu	$sp,$sp,24
	j	$31
	.end	ordena
	.align	2
	.globl	memcpy2
	.ent	memcpy2
memcpy2:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$5,20($fp)
	sw	$6,24($fp)
	sw	$0,0($fp)
$L16:
	lw	$2,0($fp)
	lw	$3,24($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L19
	j	$L17
$L19:
	lw	$2,16($fp)
	addu	$3,$2,1
	sw	$3,16($fp)
	lw	$3,20($fp)
	addu	$4,$3,1
	sw	$4,20($fp)
	lbu	$3,0($3)
	sb	$3,0($2)
$L18:
	lw	$3,0($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,0($fp)
	j	$L16
$L17:
$L15:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	memcpy2
