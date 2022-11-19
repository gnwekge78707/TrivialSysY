.data
	global:
	4491858 -597 0 1983 2323 9254 0 -1269906 0 0 0 0 0 0 0 0 0 287 -98662 287 11505 0 
	str_0: .asciiz "\n"


.text
		la $28, global
		addiu	 $29, $29, -8

# function_i32 @main():
		main:
	# basicBlock_0
		# 	%1 = call i32 @_M__a_G_I___c(i32 245512106)
		sw		 $31, 0($29)
		lui $1, 3746
		ori		 $1, $1, 14250
		sw		 $1, -32($29)
		addiu	 $29, $29, -36
		jal _M__a_G_I___c
		addiu	 $29, $29, 36
		lw		 $31, 0($29)
		xor		 $5, $2, $0
		# 	call void @putint(i32 %1)
		xor		 $4, $5, $0
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	ret i32 0
		sw		 $5, 4($29)
		addiu	 $2, $0, 10
		syscall

# function_i32 @_M__a_G_I___c(i32):
		_M__a_G_I___c:
	# basicBlock_1
		# 	%2 = alloca i32
		# 	store i32 %0, i32* %2
		lw		 $5, 4($29)
		sw		 $5, 8($29)
		# 	%3 = load i32, i32* %2
		lw		 $6, 8($29)
		# 	%4 = add i32 %3, -763835168
		lui $1, 11655
		ori		 $1, $1, 13088
		subu	 $7, $6, $1
		# 	%5 = srem i32 %4, 1996325548
		lui $1, 30461
		ori		 $1, $1, 33452
		div		 $7, $1
		mfhi	 $8
		# 	%6 = mul i32 %5, 1881457024
		lui $1, 28708
		ori		 $1, $1, 49536
		mult	 $8, $1
		mflo	 $9
		# 	%7 = sdiv i32 %6, 11
		addiu	 $1, $0, 11
		div		 $9, $1
		mflo	 $10
		# 	%8 = mul i32 %7, 540488577
		lui $1, 8247
		ori		 $1, $1, 13185
		mult	 $10, $1
		mflo	 $11
		# 	ret i32 %8
		sw		 $6, 12($29)
		sw		 $7, 16($29)
		sw		 $8, 20($29)
		sw		 $9, 24($29)
		sw		 $10, 28($29)
		sw		 $11, 32($29)
		xor		 $2, $11, $0
		jr $31
