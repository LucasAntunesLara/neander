	.file	1 "shifts.c"

 # GNU C 2.7.2.3 [AL 1.1, MM 40] DECstation running ultrix compiled by GNU C

 # Cc1 defaults:

 # Cc1 arguments (-G value = 8, Cpu = 3000, ISA = 1):
 # -quiet -dumpbase -o

gcc2_compiled.:
__gnu_compiled_c:
	.sdata
	.align	2
$LC0:
	.ascii	"%x\n\000"
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,48,$31		# vars= 16, regs= 2/0, args= 24, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,48
	sw	$31,44($sp)
	sw	$fp,40($sp)
	move	$fp,$sp
	jal	__main
	sw	$0,28($fp)
$L2:
	lw	$2,28($fp)
	slt	$3,$2,2
	bne	$3,$0,$L5
	j	$L3
$L5:
	.set	noreorder
	nop
	.set	reorder
	sw	$0,32($fp)
$L6:
	lw	$2,32($fp)
	slt	$3,$2,64
	bne	$3,$0,$L9
	j	$L7
$L9:
	sw	$0,16($sp)
	lw	$2,32($fp)
	sw	$2,20($sp)
	lw	$4,28($fp)
	move	$5,$0
	move	$6,$0
	move	$7,$0
	jal	assembleRType
	sw	$2,24($fp)
$L8:
	lw	$3,32($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,32($fp)
	j	$L6
$L7:
$L4:
	lw	$3,28($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,28($fp)
	j	$L2
$L3:
	lw	$2,24($fp)
	sw	$2,globalVar
	la	$4,$LC0
	lw	$5,24($fp)
	jal	printf
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,44($sp)
	lw	$fp,40($sp)
	addu	$sp,$sp,48
	j	$31
	.end	main
	.align	2
	.globl	assembleRType
	.ent	assembleRType
assembleRType:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$5,20($fp)
	sw	$6,24($fp)
	sw	$7,28($fp)
	sw	$0,0($fp)
	lw	$2,16($fp)
	slt	$3,$2,64
	bne	$3,$0,$L11
	li	$2,-1			# 0xffffffff
	j	$L10
	j	$L12
$L11:
	lw	$2,16($fp)
	sll	$3,$2,26
	sw	$3,4($fp)
	lw	$2,0($fp)
	lw	$3,4($fp)
	or	$2,$2,$3
	sw	$2,0($fp)
	lw	$2,0($fp)
	sw	$2,globalVar
$L12:
	lw	$2,20($fp)
	slt	$3,$2,32
	bne	$3,$0,$L13
	li	$2,-1			# 0xffffffff
	j	$L10
	j	$L14
$L13:
	lw	$2,20($fp)
	sll	$3,$2,21
	sw	$3,4($fp)
	lw	$2,0($fp)
	lw	$3,4($fp)
	or	$2,$2,$3
	sw	$2,0($fp)
	lw	$2,0($fp)
	sw	$2,globalVar
$L14:
	lw	$2,24($fp)
	slt	$3,$2,32
	bne	$3,$0,$L15
	li	$2,-1			# 0xffffffff
	j	$L10
	j	$L16
$L15:
	lw	$2,24($fp)
	sll	$3,$2,16
	sw	$3,4($fp)
	lw	$2,0($fp)
	lw	$3,4($fp)
	or	$2,$2,$3
	sw	$2,0($fp)
	lw	$2,0($fp)
	sw	$2,globalVar
$L16:
	lw	$2,28($fp)
	slt	$3,$2,32
	bne	$3,$0,$L17
	li	$2,-1			# 0xffffffff
	j	$L10
	j	$L18
$L17:
	lw	$2,28($fp)
	sll	$3,$2,11
	sw	$3,4($fp)
	lw	$2,0($fp)
	lw	$3,4($fp)
	or	$2,$2,$3
	sw	$2,0($fp)
	lw	$2,0($fp)
	sw	$2,globalVar
$L18:
	lw	$2,32($fp)
	slt	$3,$2,32
	bne	$3,$0,$L19
	li	$2,-1			# 0xffffffff
	j	$L10
	j	$L20
$L19:
	lw	$2,32($fp)
	sll	$3,$2,6
	sw	$3,4($fp)
	lw	$2,0($fp)
	lw	$3,4($fp)
	or	$2,$2,$3
	sw	$2,0($fp)
	lw	$2,0($fp)
	sw	$2,globalVar
$L20:
	lw	$2,36($fp)
	slt	$3,$2,64
	bne	$3,$0,$L21
	li	$2,-1			# 0xffffffff
	j	$L10
	j	$L22
$L21:
	lw	$2,0($fp)
	lw	$3,36($fp)
	or	$2,$2,$3
	sw	$2,0($fp)
	lw	$2,0($fp)
	sw	$2,globalVar
$L22:
	lw	$2,0($fp)
	j	$L10
$L10:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	assembleRType
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
$L24:
	lw	$2,16($fp)
	lw	$3,40($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L27
	j	$L25
$L27:
	lw	$2,32($fp)
	addu	$3,$2,1
	sw	$3,32($fp)
	lw	$3,36($fp)
	addu	$4,$3,1
	sw	$4,36($fp)
	lbu	$3,0($3)
	sb	$3,0($2)
$L26:
	lw	$3,16($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,16($fp)
	j	$L24
$L25:
$L23:
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
	beq	$2,$0,$L30
	li	$3,0x00000001		# 1
	beq	$2,$3,$L31
	j	$L32
$L30:
	li	$2,0x00000001		# 1
	j	$L28
$L31:
	li	$2,0x00000004		# 4
	j	$L28
$L32:
	li	$2,-1			# 0xffffffff
	j	$L28
$L29:
$L28:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,0($sp)
	addu	$sp,$sp,8
	j	$31
	.end	sizeof2

	.comm	globalVar,4
