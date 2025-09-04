//
// swap ( int v[], int n)
// {
// int i, j;
// for ( i = 0; i < n; i = i + 1) {
//  for ( j = i - 1; j >= 0 && v[j] > v[j+1]; j = j - 1) swap ( v, j); 
// }
// data address range begin at address 0
.data
v:  .word 100,98,96,94,92,90,88,86,84,82,80,78,76,74,72,70,68,66,64,62,60,58,56,54,52, 50
n: .word 26
.code
// stack pointer = 124
 addi $sp, $zero, 124
// The arguments to be passed to the function are stored in the registers $a0 (vetO and $a1 (k)
  add $a0,$zero,$zero
 lw $a1,n($zero)
// Call sort function
  jal sort
  hlt
////////////////////////////////////////////////////////////////////////////////////////////
// swap function
////////////////////////////////////////////////////////////////////////////////////////////
// $t1 = vet [k]
swap: add $t1,$a1,$a1
 add $t1,$t1,$t1
 add $t1,$a0,$t1
// $t0 = vet [k] and $t2 = vet [k+1]
 lw $t0,0($t1)
 lw $t2,4($t1)
// swap the values of the elements vet [k] and vet [k+1]
 sw $t2,0($t1)
 sw $t0,4($t1)
//
 jr $ra
////////////////////////////////////////////////////////////////////////////////////////////
// sort function
////////////////////////////////////////////////////////////////////////////////////////////
sort: addi $sp, $sp, -20
 sw $ra, 16($sp)
 sw $s3, 12($sp)
 sw $s2, 8($sp)
 sw $s1, 4($sp)
 sw $s0, 0($sp) 
 move $s2, $a0
 move $s3, $a1
 move $s0, $zero
for1tst: slt $t0, $s0, $s3
 beq $t0, $zero, exit1
 addi $s1, $s0, -1
for2tst: slti $t0, $s1, 0
 bne $t0, $zero, exit2
 add $t1, $s1, $s1
 add $t1, $t1, $t1
 add $t2, $s2, $t1
 lw $t3, 0($t2)
 lw $t4, 4($t2)
 slt $t0, $t4, $t3
 beq $t0, $zero, exit2
 move $a0, $s2
 move $a1, $s1
 jal swap
 addi $s1, $s1, -1
 j for2tst
exit2: addi $s0, $s0, 1
 j for1tst
exit1: lw $s0, 0($sp)
 lw $s1, 4($sp)
 lw $s2, 8($sp)
 lw $s3, 12($sp)
 lw $ra, 16($sp)
 addi $sp, $sp, 20
 jr $ra

