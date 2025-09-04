	.file	1 "teste.c"

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
	.word	-11
	.globl	globalVar2
	.align	2
globalVar2:
	.word	255
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,32,$31		# vars= 8, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,32
	sw	$31,28($sp)
	sw	$fp,24($sp)
	move	$fp,$sp
	jal	__main
	li	$2,0x00000001		# 1
	sw	$2,16($fp)
	li	$2,0x0000000a		# 10
	sw	$2,20($fp)
	lw	$4,16($fp)
	lw	$5,20($fp)
	jal	soma
	sw	$2,globalVar
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,28($sp)
	lw	$fp,24($sp)
	addu	$sp,$sp,32
	j	$31
	.end	main
	.align	2
	.globl	soma
	.ent	soma
soma:
	.frame	$fp,8,$31		# vars= 0, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,8
	sw	$fp,0($sp)
	move	$fp,$sp
	sw	$4,8($fp)
	sw	$5,12($fp)
	lw	$2,8($fp)
	lw	$3,12($fp)
	addu	$2,$2,$3
	sw	$2,8($fp)
	lw	$2,8($fp)
	j	$L2
$L2:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,0($sp)
	addu	$sp,$sp,8
	j	$31
	.end	soma
