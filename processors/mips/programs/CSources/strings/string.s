	.file	1 "string.c"

 # GNU C 2.7.2.3 [AL 1.1, MM 40] DECstation running ultrix compiled by GNU C

 # Cc1 defaults:

 # Cc1 arguments (-G value = 8, Cpu = 3000, ISA = 1):
 # -quiet -dumpbase -o

gcc2_compiled.:
__gnu_compiled_c:
	.globl	varGlobal
	.sdata
	.align	2
varGlobal:
	.word	0
	.rdata
	.align	2
$LC0:
	.ascii	"ab\000"
	.space	57
	.ascii	"b\000"
	.space	58
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bbbbbbbbb\000"
	.space	11
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bbbbbbbb\000"
	.space	12
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bbbbbbb\000"
	.space	13
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bbbbbb\000"
	.space	14
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bbbbb\000"
	.space	15
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bbbb\000"
	.space	16
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bbb\000"
	.space	17
	.ascii	"teste da nossa funcao strlen aaaaaaaaa bb\000"
	.space	18
	.ascii	"teste da nossa funcao strlen aaaaaaaaa b\000"
	.space	19
	.ascii	"teste da nossa funcao strlen aaaaaaaaa \000"
	.space	20
	.ascii	"teste da nossa funcao strlen aaaaaaaaa\000"
	.space	21
	.ascii	"teste da nossa funcao strlen aaaaaaaa\000"
	.space	22
	.ascii	"teste da nossa funcao strlen aaaaaaa\000"
	.space	23
	.ascii	"teste da nossa funcao strlen aaaaaa\000"
	.space	24
	.ascii	"teste da nossa funcao strlen aaaaa\000"
	.space	25
	.ascii	"teste da nossa funcao strlen aaaa\000"
	.space	26
	.ascii	"teste da nossa funcao strlen aaa\000"
	.space	27
	.ascii	"teste da nossa funcao strlen aa\000"
	.space	28
	.ascii	"teste da nossa funcao strlen a\000"
	.space	29
	.ascii	"teste da nossa funcao strlen \000"
	.space	30
	.ascii	"teste da nossa funcao strlen\000"
	.space	31
	.ascii	"teste da nossa funcao strle\000"
	.space	32
	.ascii	"teste da nossa funcao strl\000"
	.space	33
	.ascii	"teste da nossa funcao str\000"
	.space	34
	.ascii	"teste da nossa funcao st\000"
	.space	35
	.sdata
	.align	2
$LC1:
	.ascii	"%d\n\000"
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,1656,$31		# vars= 1632, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,1656
	sw	$31,1652($sp)
	sw	$fp,1648($sp)
	move	$fp,$sp
	jal	__main
	addu	$2,$fp,16
	la	$3,$LC0
	move	$4,$2
	move	$5,$3
	li	$6,0x00000654		# 1620
	jal	memcpy
	sw	$0,1640($fp)
$L2:
	lw	$2,1640($fp)
	slt	$3,$2,27
	bne	$3,$0,$L5
	j	$L3
$L5:
	lw	$2,1640($fp)
	move	$4,$2
	sll	$3,$4,4
	subu	$3,$3,$2
	sll	$2,$3,2
	addu	$3,$fp,16
	addu	$2,$3,$2
	move	$4,$2
	jal	strlength
	sw	$2,varGlobal
	la	$4,$LC1
	lw	$5,varGlobal
	jal	printf
$L4:
	lw	$3,1640($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,1640($fp)
	j	$L2
$L3:
	.set	noreorder
	nop
	.set	reorder
	sw	$0,1640($fp)
$L6:
	lw	$2,1640($fp)
	slt	$3,$2,27
	bne	$3,$0,$L9
	j	$L7
$L9:
	lw	$2,1640($fp)
	move	$4,$2
	sll	$3,$4,4
	subu	$3,$3,$2
	sll	$2,$3,2
	addu	$3,$fp,16
	addu	$2,$3,$2
	move	$4,$2
	jal	strlength
	sw	$2,varGlobal
	la	$4,$LC1
	lw	$5,varGlobal
	jal	printf
$L8:
	lw	$3,1640($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,1640($fp)
	j	$L6
$L7:
	.set	noreorder
	nop
	.set	reorder
	sw	$0,1640($fp)
$L10:
	lw	$2,1640($fp)
	slt	$3,$2,27
	bne	$3,$0,$L13
	j	$L11
$L13:
	lw	$2,1640($fp)
	move	$4,$2
	sll	$3,$4,4
	subu	$3,$3,$2
	sll	$2,$3,2
	addu	$3,$fp,16
	addu	$2,$3,$2
	move	$4,$2
	jal	strlength
	sw	$2,varGlobal
	la	$4,$LC1
	lw	$5,varGlobal
	jal	printf
$L12:
	lw	$3,1640($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,1640($fp)
	j	$L10
$L11:
	.set	noreorder
	nop
	.set	reorder
	sw	$0,1640($fp)
$L14:
	lw	$2,1640($fp)
	slt	$3,$2,27
	bne	$3,$0,$L17
	j	$L15
$L17:
	lw	$2,1640($fp)
	move	$4,$2
	sll	$3,$4,4
	subu	$3,$3,$2
	sll	$2,$3,2
	addu	$3,$fp,16
	addu	$2,$3,$2
	move	$4,$2
	jal	strlength
	sw	$2,varGlobal
	la	$4,$LC1
	lw	$5,varGlobal
	jal	printf
$L16:
	lw	$3,1640($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,1640($fp)
	j	$L14
$L15:
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,1652($sp)
	lw	$fp,1648($sp)
	addu	$sp,$sp,1656
	j	$31
	.end	main
	.align	2
	.globl	strlength
	.ent	strlength
strlength:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	lw	$2,16($fp)
	sw	$2,0($fp)
	sw	$0,4($fp)
$L19:
	lw	$2,0($fp)
	addu	$3,$2,1
	sw	$3,0($fp)
	lb	$2,0($2)
	bne	$2,$0,$L22
	j	$L20
$L22:
	lw	$3,4($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,4($fp)
$L21:
	j	$L19
$L20:
	lw	$2,4($fp)
	j	$L18
$L18:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	strlength
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
