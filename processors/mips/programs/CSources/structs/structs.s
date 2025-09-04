	.file	1 "structs.c"

 # GNU C 2.7.2.3 [AL 1.1, MM 40] DECstation running ultrix compiled by GNU C

 # Cc1 defaults:

 # Cc1 arguments (-G value = 8, Cpu = 3000, ISA = 1):
 # -quiet -dumpbase -o

gcc2_compiled.:
__gnu_compiled_c:
	.globl	globalVar
	.sdata
	.align	2
globalVar:
	.word	-1
	.align	2
$LC0:
	.ascii	"%d\n\000"
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,8224,$31		# vars= 8200, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,8224
	sw	$31,8220($sp)
	sw	$fp,8216($sp)
	move	$fp,$sp
	jal	__main
	sw	$0,20($fp)
$L2:
	lw	$2,20($fp)
	slt	$3,$2,1024
	bne	$3,$0,$L5
	j	$L3
$L5:
	lw	$2,20($fp)
	move	$3,$2
	sll	$2,$3,3
	addu	$3,$fp,24
	addu	$2,$3,$2
	lw	$3,20($fp)
	sw	$3,0($2)
	lw	$2,20($fp)
	move	$3,$2
	sll	$2,$3,3
	addu	$3,$fp,24
	addu	$2,$3,$2
	lw	$3,20($fp)
	move	$4,$3
	sll	$3,$4,1
	sw	$3,4($2)
$L4:
	lw	$3,20($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,20($fp)
	j	$L2
$L3:
	addu	$2,$fp,24
	move	$4,$2
	li	$5,0x00000400		# 1024
	li	$6,0x00000258		# 600
	jal	searchHash
	sw	$2,16($fp)
	lw	$2,16($fp)
	sw	$2,globalVar
	la	$4,$LC0
	lw	$5,16($fp)
	jal	printf
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,8220($sp)
	lw	$fp,8216($sp)
	addu	$sp,$sp,8224
	j	$31
	.end	main
	.align	2
	.globl	searchHash
	.ent	searchHash
searchHash:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$5,20($fp)
	sw	$6,24($fp)
	lw	$2,16($fp)
	sw	$2,4($fp)
	sw	$0,0($fp)
$L7:
	lw	$2,0($fp)
	lw	$3,20($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L10
	j	$L8
$L10:
	lw	$2,4($fp)
	lw	$3,0($2)
	lw	$2,24($fp)
	bne	$3,$2,$L11
	lw	$3,4($fp)
	lw	$2,4($3)
	j	$L6
$L11:
$L9:
	lw	$3,0($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,0($fp)
	lw	$3,4($fp)
	addu	$2,$3,8
	move	$3,$2
	sw	$3,4($fp)
	j	$L7
$L8:
	li	$2,-1			# 0xffffffff
	j	$L6
$L6:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	searchHash
	.rdata
	.align	2
$LC1:
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
	la	$4,$LC1
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
