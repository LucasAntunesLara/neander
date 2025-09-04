	.file	1 "TDbitcnts.c"

 # GNU C 2.7.2.3 [AL 1.1, MM 40] DECstation running ultrix compiled by GNU C

 # Cc1 defaults:

 # Cc1 arguments (-G value = 8, Cpu = 3000, ISA = 1):
 # -quiet -dumpbase -o

gcc2_compiled.:
__gnu_compiled_c:
	.globl	results
	.data
	.align	2
results:
	.word	-1
	.word	-1
	.word	-1
	.word	-1
	.word	-1
	.word	-1
	.word	-1
	.word	-1
	.align	2
bits:
	.byte	0
	.byte	1
	.byte	1
	.byte	2
	.byte	1
	.byte	2
	.byte	2
	.byte	3
	.byte	1
	.byte	2
	.byte	2
	.byte	3
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	1
	.byte	2
	.byte	2
	.byte	3
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	1
	.byte	2
	.byte	2
	.byte	3
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	1
	.byte	2
	.byte	2
	.byte	3
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	5
	.byte	6
	.byte	6
	.byte	7
	.byte	1
	.byte	2
	.byte	2
	.byte	3
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	5
	.byte	6
	.byte	6
	.byte	7
	.byte	2
	.byte	3
	.byte	3
	.byte	4
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	5
	.byte	6
	.byte	6
	.byte	7
	.byte	3
	.byte	4
	.byte	4
	.byte	5
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	5
	.byte	6
	.byte	6
	.byte	7
	.byte	4
	.byte	5
	.byte	5
	.byte	6
	.byte	5
	.byte	6
	.byte	6
	.byte	7
	.byte	5
	.byte	6
	.byte	6
	.byte	7
	.byte	6
	.byte	7
	.byte	7
	.byte	8
	.text
	.align	2
	.globl	main
	.ent	main
main:
	.frame	$fp,40,$31		# vars= 16, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,40
	sw	$31,36($sp)
	sw	$fp,32($sp)
	move	$fp,$sp
	sw	$4,40($fp)
	sw	$5,44($fp)
	jal	__main
	sw	$0,28($fp)
	sw	$0,24($fp)
	sw	$0,20($fp)
$L2:
	lw	$2,20($fp)
	slt	$3,$2,1500
	bne	$3,$0,$L5
	j	$L3
$L5:
	lw	$2,28($fp)
	sw	$2,results
	lw	$4,28($fp)
	jal	bit_shifter
	sw	$2,results+4
	lw	$4,28($fp)
	jal	bit_count
	sw	$2,results+8
	lw	$4,28($fp)
	jal	bitcount
	sw	$2,results+12
	lw	$4,28($fp)
	jal	ntbl_bitcnt
	sw	$2,results+16
	lw	$4,28($fp)
	jal	ntbl_bitcount
	sw	$2,results+20
	lw	$4,28($fp)
	jal	BW_btbl_bitcount
	sw	$2,results+24
	lw	$4,28($fp)
	jal	AR_btbl_bitcount
	sw	$2,results+28
$L4:
	lw	$3,20($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,20($fp)
	lw	$3,28($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,28($fp)
	j	$L2
$L3:
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,36($sp)
	lw	$fp,32($sp)
	addu	$sp,$sp,40
	j	$31
	.end	main
	.align	2
	.globl	bit_shifter
	.ent	bit_shifter
bit_shifter:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
.risabegin 22
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$0,4($fp)
	sw	$0,0($fp)
$L7:
	lw	$2,16($fp)
	beq	$2,$0,$L11
	lw	$2,0($fp)
	sltu	$3,$2,32
	bne	$3,$0,$L10
.risaend
	j	$L11
$L11:
	j	$L8
$L10:
.risabegin 21
	lw	$3,16($fp)
	andi	$2,$3,0x0001
	lw	$3,4($fp)
	addu	$2,$3,$2
	sw	$2,4($fp)
$L9:
	lw	$3,0($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,0($fp)
	lw	$2,16($fp)
	sra	$3,$2,1
	sw	$3,16($fp)
	j	$L7
$L8:
	lw	$2,4($fp)
	j	$L6
$L6:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
.risaend
	j	$31
	.end	bit_shifter
	.align	2
	.globl	bit_count
	.ent	bit_count
bit_count:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	sw	$0,0($fp)
	lw	$2,16($fp)
	beq	$2,$0,$L13
$L14:
	lw	$3,0($fp)
	addu	$2,$3,1
	move	$3,$2
	sw	$3,0($fp)
$L16:
	lw	$3,16($fp)
	addu	$2,$3,-1
	lw	$3,16($fp)
	and	$2,$2,$3
	move	$3,$2
	sw	$3,16($fp)
	bne	$3,$0,$L14
$L13:
	lw	$2,0($fp)
	j	$L12
$L12:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	bit_count
	.align	2
	.globl	bitcount
	.ent	bitcount
bitcount:
	.frame	$fp,8,$31		# vars= 0, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,8
	sw	$fp,0($sp)
	move	$fp,$sp
	sw	$4,8($fp)
	lw	$2,8($fp)
	li	$4,-1431655766			# 0xaaaaaaaa
	and	$3,$2,$4
	srl	$2,$3,1
	lw	$3,8($fp)
	li	$4,0x55555555		# 1431655765
	and	$3,$3,$4
	addu	$2,$2,$3
	sw	$2,8($fp)
	lw	$2,8($fp)
	li	$4,-858993460			# 0xcccccccc
	and	$3,$2,$4
	srl	$2,$3,2
	lw	$3,8($fp)
	li	$4,0x33333333		# 858993459
	and	$3,$3,$4
	addu	$2,$2,$3
	sw	$2,8($fp)
	lw	$2,8($fp)
	li	$4,-252645136			# 0xf0f0f0f0
	and	$3,$2,$4
	srl	$2,$3,4
	lw	$3,8($fp)
	li	$4,0x0f0f0f0f		# 252645135
	and	$3,$3,$4
	addu	$2,$2,$3
	sw	$2,8($fp)
	lw	$2,8($fp)
	li	$4,-16711936			# 0xff00ff00
	and	$3,$2,$4
	srl	$2,$3,8
	lw	$3,8($fp)
	li	$4,0x00ff00ff		# 16711935
	and	$3,$3,$4
	addu	$2,$2,$3
	sw	$2,8($fp)
	lw	$2,8($fp)
	li	$4,-65536			# 0xffff0000
	and	$3,$2,$4
	srl	$2,$3,16
	lw	$4,8($fp)
	andi	$3,$4,0xffff
	addu	$2,$2,$3
	sw	$2,8($fp)
	lw	$2,8($fp)
	j	$L18
$L18:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,0($sp)
	addu	$sp,$sp,8
	j	$31
	.end	bitcount
	.align	2
	.globl	ntbl_bitcnt
	.ent	ntbl_bitcnt
ntbl_bitcnt:
	.frame	$fp,32,$31		# vars= 8, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,32
	sw	$31,28($sp)
	sw	$fp,24($sp)
	move	$fp,$sp
	sw	$4,32($fp)
	lw	$3,32($fp)
	andi	$2,$3,0x000f
	lb	$3,bits($2)
	sw	$3,16($fp)
	lw	$3,32($fp)
	sra	$2,$3,4
	move	$3,$2
	sw	$3,32($fp)
	beq	$3,$0,$L20
	lw	$4,32($fp)
	jal	ntbl_bitcnt
	lw	$3,16($fp)
	addu	$2,$3,$2
	sw	$2,16($fp)
$L20:
	lw	$2,16($fp)
	j	$L19
$L19:
	move	$sp,$fp			# sp not trusted here
	lw	$31,28($sp)
	lw	$fp,24($sp)
	addu	$sp,$sp,32
	j	$31
	.end	ntbl_bitcnt
	.align	2
	.globl	ntbl_bitcount
	.ent	ntbl_bitcount
ntbl_bitcount:
	.frame	$fp,8,$31		# vars= 0, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,8
	sw	$fp,0($sp)
	move	$fp,$sp
	sw	$4,8($fp)
	lw	$2,8($fp)
	andi	$3,$2,0x000f
	lb	$2,bits($3)
	lw	$4,8($fp)
	andi	$3,$4,0x00f0
	srl	$4,$3,4
	lb	$3,bits($4)
	addu	$2,$2,$3
	lw	$4,8($fp)
	andi	$3,$4,0x0f00
	srl	$4,$3,8
	lb	$3,bits($4)
	addu	$2,$2,$3
	lw	$4,8($fp)
	andi	$3,$4,0xf000
	srl	$4,$3,12
	lb	$3,bits($4)
	addu	$2,$2,$3
	lw	$3,8($fp)
	li	$4,0x000f0000		# 983040
	and	$3,$3,$4
	srl	$4,$3,16
	lb	$3,bits($4)
	addu	$2,$2,$3
	lw	$3,8($fp)
	li	$4,0x00f00000		# 15728640
	and	$3,$3,$4
	srl	$4,$3,20
	lb	$3,bits($4)
	addu	$2,$2,$3
	lw	$3,8($fp)
	li	$4,0x0f000000		# 251658240
	and	$3,$3,$4
	srl	$4,$3,24
	lb	$3,bits($4)
	addu	$2,$2,$3
	lw	$3,8($fp)
	li	$4,-268435456			# 0xf0000000
	and	$3,$3,$4
	srl	$4,$3,28
	lb	$3,bits($4)
	addu	$4,$2,$3
	move	$2,$4
	j	$L21
$L21:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,0($sp)
	addu	$sp,$sp,8
	j	$31
	.end	ntbl_bitcount
	.align	2
	.globl	BW_btbl_bitcount
	.ent	BW_btbl_bitcount
BW_btbl_bitcount:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	lw	$2,16($fp)
	sw	$2,0($fp)
	lbu	$3,0($fp)
	lb	$2,bits($3)
	lbu	$3,1($fp)
	lb	$4,bits($3)
	addu	$2,$2,$4
	lbu	$3,3($fp)
	lb	$4,bits($3)
	addu	$2,$2,$4
	lbu	$3,2($fp)
	lb	$4,bits($3)
	addu	$3,$2,$4
	move	$2,$3
	j	$L22
$L22:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	BW_btbl_bitcount
	.align	2
	.globl	AR_btbl_bitcount
	.ent	AR_btbl_bitcount
AR_btbl_bitcount:
	.frame	$fp,16,$31		# vars= 8, regs= 1/0, args= 0, extra= 0
	.mask	0x40000000,-8
	.fmask	0x00000000,0
	subu	$sp,$sp,16
	sw	$fp,8($sp)
	move	$fp,$sp
	sw	$4,16($fp)
	addu	$5,$fp,16
	sw	$5,0($fp)
	lw	$2,0($fp)
	addu	$3,$2,1
	sw	$3,0($fp)
	lbu	$2,0($2)
	lb	$3,bits($2)
	sw	$3,4($fp)
	lw	$2,0($fp)
	addu	$3,$2,1
	sw	$3,0($fp)
	lbu	$2,0($2)
	lb	$3,bits($2)
	lw	$2,4($fp)
	addu	$3,$2,$3
	sw	$3,4($fp)
	lw	$2,0($fp)
	addu	$3,$2,1
	sw	$3,0($fp)
	lbu	$2,0($2)
	lb	$3,bits($2)
	lw	$2,4($fp)
	addu	$3,$2,$3
	sw	$3,4($fp)
	lw	$2,0($fp)
	lbu	$3,0($2)
	lb	$2,bits($3)
	lw	$3,4($fp)
	addu	$2,$3,$2
	sw	$2,4($fp)
	lw	$2,4($fp)
	j	$L23
$L23:
	move	$sp,$fp			# sp not trusted here
	lw	$fp,8($sp)
	addu	$sp,$sp,16
	j	$31
	.end	AR_btbl_bitcount
