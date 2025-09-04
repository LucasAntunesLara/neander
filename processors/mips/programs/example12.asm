//
// swap ( int v[], int k)
// {
// int temp;
// temp = v[k];
// v[k] = v[k+1];
// v[k+1]= temp;
// }
// data address range begin at address 0
.data
vet:  .word 1, 2, 3, 4, 5
k: .word 2
.code
// stack pointer = 100
 addi $sp, $zero, 100
// The arguments to be passed to the function are stored in the registers $a0 (vetO and $a1 (k)
  add $a0,$zero,$zero
 lw $a1,k($zero)
// Call fact function
  jal swap
  nop
  nop
  nop
  nop
 nop
//
// swap function
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
