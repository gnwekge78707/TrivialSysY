.data
	global:
	9 
	str_1: .asciiz "appear\n"
	str_0: .asciiz "\n"
	str_6: .asciiz "print return value :\n"
	str_5: .asciiz "/**/"
	str_2: .asciiz "not\n"
	str_4: .asciiz "/****/"
	str_3: .asciiz "print123\n"
	str_7: .asciiz "in : 5 ,out = "


.text
		la $28, global
		addiu	 $29, $29, -144

# function_i32 @main():
		f_main:
	# basicBlock_0
		# 	%1 = alloca i32
		# 	store i32 89, i32* %1
		addiu	 $1, $0, 89
		sw		 $1, 4($29)
		# 	%2 = load i32, i32* %1
		lw		 $5, 4($29)
		# 	%3 = alloca i32
		# 	store i32 %2, i32* %3
		sw		 $5, 12($29)
		# 	%4 = alloca i32
		# 	store i32 89, i32* %4
		addiu	 $1, $0, 89
		sw		 $1, 16($29)
		# 	%5 = alloca i32
		# 	store i32 18, i32* %5
		addiu	 $1, $0, 18
		sw		 $1, 20($29)
		# 	%6 = load i32, i32* @d
		lw		 $6, 0($28)
		# 	%7 = alloca i32
		# 	store i32 %6, i32* %7
		sw		 $6, 28($29)
		# 	call void @putint(i32 19231177)
		lui $4, 293
		ori		 $4, $4, 29129
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	call void @testGetInt()
		sw		 $5, 8($29)
		sw		 $6, 24($29)
		sw		 $31, 0($29)
		addiu	 $29, $29, -40
		jal f_testGetInt
		addiu	 $29, $29, 40
		lw		 $31, 0($29)
		# 	%8 = alloca i32
		# 	store i32 1, i32* %8
		addiu	 $1, $0, 1
		sw		 $1, 32($29)
		# 	br label %9
		j bb_main_9
	# basicBlock_9
		bb_main_9:
		# 	%10 = load i32, i32* %8
		lw		 $5, 32($29)
		# 	%11 = icmp slt i32 %10, 10
		slti	 $6, $5, 10
		# 	%12 = icmp ne i1 %11, 0
		# 	br i1 %12, label %13, label %24
		addiu	 $1, $0, 0
		bne		 $6, $1, bb_main_13
		j bb_main_24
	# basicBlock_13
		bb_main_13:
		# 	%14 = load i32, i32* %8
		lw		 $5, 32($29)
		# 	%15 = call i32 @mycheck(i32 %14)
		sw		 $5, 48($29)
		sw		 $31, 0($29)
		sw		 $5, -84($29)
		addiu	 $29, $29, -88
		jal f_mycheck
		addiu	 $29, $29, 88
		lw		 $31, 0($29)
		xor		 $5, $2, $0
		# 	call void @putint(i32 %15)
		xor		 $4, $5, $0
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	%16 = load i32, i32* %8
		lw		 $6, 32($29)
		# 	%17 = call i32 @mycheck(i32 %16)
		sw		 $5, 52($29)
		sw		 $6, 56($29)
		sw		 $31, 0($29)
		sw		 $6, -84($29)
		addiu	 $29, $29, -88
		jal f_mycheck
		addiu	 $29, $29, 88
		lw		 $31, 0($29)
		xor		 $5, $2, $0
		# 	%18 = load i32, i32* %8
		lw		 $6, 32($29)
		# 	%19 = call i32 @mycheck(i32 %18)
		sw		 $5, 60($29)
		sw		 $6, 64($29)
		sw		 $31, 0($29)
		sw		 $6, -84($29)
		addiu	 $29, $29, -88
		jal f_mycheck
		addiu	 $29, $29, 88
		lw		 $31, 0($29)
		xor		 $5, $2, $0
		# 	%20 = sdiv i32 %19, 20
		addiu	 $1, $0, 20
		div		 $5, $1
		mflo	 $6
		# 	%21 = call i32 @mycheck(i32 %20)
		sw		 $5, 68($29)
		sw		 $6, 72($29)
		sw		 $31, 0($29)
		sw		 $6, -84($29)
		addiu	 $29, $29, -88
		jal f_mycheck
		addiu	 $29, $29, 88
		lw		 $31, 0($29)
		xor		 $5, $2, $0
		# 	%22 = icmp slt i32 %17, %21
		lw		 $6, 60($29)
		slt		 $7, $6, $5
		# 	%23 = icmp ne i1 %22, 0
		# 	br i1 %23, label %40, label %41
		addiu	 $1, $0, 0
		bne		 $7, $1, bb_main_40
		j bb_main_41
	# basicBlock_24
		bb_main_24:
		# 	%25 = alloca i32
		# 	store i32 1, i32* %25
		addiu	 $1, $0, 1
		sw		 $1, 88($29)
		# 	%26 = alloca i32
		# 	store i32 2, i32* %26
		addiu	 $1, $0, 2
		sw		 $1, 92($29)
		# 	%27 = alloca i32
		# 	store i32 3, i32* %27
		addiu	 $1, $0, 3
		sw		 $1, 96($29)
		la $4, str_3
		addiu	 $2, $0, 4
		syscall
		# 	call void @putint(i32 1)
		addiu	 $4, $0, 1
		addiu	 $2, $0, 1
		syscall
		# 	call void @putint(i32 2)
		addiu	 $4, $0, 2
		addiu	 $2, $0, 1
		syscall
		# 	call void @putint(i32 3)
		addiu	 $4, $0, 3
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	%28 = load i32, i32* %26
		lw		 $5, 92($29)
		# 	%29 = load i32, i32* %27
		lw		 $6, 96($29)
		la $4, str_4
		addiu	 $2, $0, 4
		syscall
		# 	call void @putint(i32 1)
		addiu	 $4, $0, 1
		addiu	 $2, $0, 1
		syscall
		# 	call void @putint(i32 %28)
		xor		 $4, $5, $0
		addiu	 $2, $0, 1
		syscall
		# 	call void @putint(i32 %29)
		xor		 $4, $6, $0
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	%30 = load i32, i32* %25
		lw		 $7, 88($29)
		# 	%31 = load i32, i32* %26
		lw		 $8, 92($29)
		# 	%32 = load i32, i32* %27
		lw		 $9, 96($29)
		la $4, str_5
		addiu	 $2, $0, 4
		syscall
		# 	call void @putint(i32 %30)
		xor		 $4, $7, $0
		addiu	 $2, $0, 1
		syscall
		# 	call void @putint(i32 %31)
		xor		 $4, $8, $0
		addiu	 $2, $0, 1
		syscall
		# 	call void @putint(i32 %32)
		xor		 $4, $9, $0
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		la $4, str_6
		addiu	 $2, $0, 4
		syscall
		# 	%33 = alloca i32
		# 	store i32 5, i32* %33
		addiu	 $1, $0, 5
		sw		 $1, 120($29)
		# 	%34 = call i32 @mycheck(i32 5)
		sw		 $5, 100($29)
		sw		 $6, 104($29)
		sw		 $7, 108($29)
		sw		 $8, 112($29)
		sw		 $9, 116($29)
		sw		 $31, 0($29)
		addiu	 $1, $0, 5
		sw		 $1, -84($29)
		addiu	 $29, $29, -88
		jal f_mycheck
		addiu	 $29, $29, 88
		lw		 $31, 0($29)
		xor		 $5, $2, $0
		la $4, str_7
		addiu	 $2, $0, 4
		syscall
		# 	call void @putint(i32 %34)
		xor		 $4, $5, $0
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	%35 = load i32, i32* %33
		lw		 $6, 120($29)
		# 	%36 = call i32 @mycheck(i32 %35)
		sw		 $5, 124($29)
		sw		 $6, 128($29)
		sw		 $31, 0($29)
		sw		 $6, -84($29)
		addiu	 $29, $29, -88
		jal f_mycheck
		addiu	 $29, $29, 88
		lw		 $31, 0($29)
		xor		 $5, $2, $0
		la $4, str_7
		addiu	 $2, $0, 4
		syscall
		# 	call void @putint(i32 %36)
		xor		 $4, $5, $0
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	ret i32 0
		sw		 $5, 132($29)
		addiu	 $2, $0, 10
		syscall
	# basicBlock_37
		bb_main_37:
		# 	%38 = load i32, i32* %8
		lw		 $5, 32($29)
		# 	%39 = add i32 %38, 1
		addiu	 $6, $5, 1
		# 	store i32 %39, i32* %8
		sw		 $6, 32($29)
		# 	br label %9
		j bb_main_9
	# basicBlock_40
		bb_main_40:
		la $4, str_1
		addiu	 $2, $0, 4
		syscall
		# 	br label %37
		j bb_main_37
	# basicBlock_41
		bb_main_41:
		la $4, str_2
		addiu	 $2, $0, 4
		syscall
		# 	br label %37
		j bb_main_37

# function_i32 @mycheck(i32):
		f_mycheck:
	# basicBlock_1
		# 	%2 = alloca i32
		# 	store i32 %0, i32* %2
		lw		 $5, 4($29)
		sw		 $5, 8($29)
		# 	%3 = load i32, i32* %2
		lw		 $6, 8($29)
		# 	%4 = zext i1 1 to i32
		addiu	 $7, $0, 1
		# 	%5 = icmp eq i32 %3, %4
		seq		 $8, $6, $7
		# 	%6 = icmp ne i1 %5, 0
		# 	br i1 %6, label %11, label %7
		addiu	 $1, $0, 0
		bne		 $8, $1, bb_mycheck_11
		j bb_mycheck_7
	# basicBlock_7
		bb_mycheck_7:
		# 	%8 = load i32, i32* %2
		lw		 $5, 8($29)
		# 	%9 = icmp eq i32 %8, 2
		addiu	 $1, $0, 2
		seq		 $6, $5, $1
		# 	%10 = icmp ne i1 %9, 0
		# 	br i1 %10, label %16, label %12
		addiu	 $1, $0, 0
		bne		 $6, $1, bb_mycheck_16
		j bb_mycheck_12
	# basicBlock_11
		bb_mycheck_11:
		# 	ret i32 10
		addiu	 $2, $0, 10
		jr $31
	# basicBlock_12
		bb_mycheck_12:
		# 	%13 = load i32, i32* %2
		lw		 $5, 8($29)
		# 	%14 = icmp eq i32 %13, 4
		addiu	 $1, $0, 4
		seq		 $6, $5, $1
		# 	%15 = icmp ne i1 %14, 0
		# 	br i1 %15, label %21, label %22
		addiu	 $1, $0, 0
		bne		 $6, $1, bb_mycheck_21
		j bb_mycheck_22
	# basicBlock_16
		bb_mycheck_16:
		# 	ret i32 20
		addiu	 $2, $0, 20
		jr $31
	# basicBlock_17
		bb_mycheck_17:
		# 	%18 = load i32, i32* %2
		lw		 $5, 8($29)
		# 	%19 = icmp eq i32 %18, 3
		addiu	 $1, $0, 3
		seq		 $6, $5, $1
		# 	%20 = icmp ne i1 %19, 0
		# 	br i1 %20, label %35, label %36
		addiu	 $1, $0, 0
		bne		 $6, $1, bb_mycheck_35
		j bb_mycheck_36
	# basicBlock_21
		bb_mycheck_21:
		# 	ret i32 40
		addiu	 $2, $0, 40
		jr $31
	# basicBlock_22
		bb_mycheck_22:
		# 	%23 = load i32, i32* %2
		lw		 $5, 8($29)
		# 	%24 = icmp eq i32 %23, 5
		addiu	 $1, $0, 5
		seq		 $6, $5, $1
		# 	%25 = icmp ne i1 %24, 0
		# 	br i1 %25, label %27, label %28
		addiu	 $1, $0, 0
		bne		 $6, $1, bb_mycheck_27
		j bb_mycheck_28
	# basicBlock_26
		bb_mycheck_26:
		# 	br label %17
		j bb_mycheck_17
	# basicBlock_27
		bb_mycheck_27:
		# 	ret i32 50
		addiu	 $2, $0, 50
		jr $31
	# basicBlock_28
		bb_mycheck_28:
		# 	%29 = load i32, i32* %2
		lw		 $5, 8($29)
		# 	%30 = icmp eq i32 %29, 6
		addiu	 $1, $0, 6
		seq		 $6, $5, $1
		# 	%31 = icmp ne i1 %30, 0
		# 	br i1 %31, label %33, label %32
		addiu	 $1, $0, 0
		bne		 $6, $1, bb_mycheck_33
		j bb_mycheck_32
	# basicBlock_32
		bb_mycheck_32:
		# 	br label %26
		j bb_mycheck_26
	# basicBlock_33
		bb_mycheck_33:
		# 	ret i32 60
		addiu	 $2, $0, 60
		jr $31
	# basicBlock_34
		bb_mycheck_34:
		# 	ret i32 0
		addiu	 $2, $0, 0
		jr $31
	# basicBlock_35
		bb_mycheck_35:
		# 	ret i32 30
		addiu	 $2, $0, 30
		jr $31
	# basicBlock_36
		bb_mycheck_36:
		# 	ret i32 99
		addiu	 $2, $0, 99
		jr $31

# function_void @testGetInt():
		f_testGetInt:
	# basicBlock_0
		# 	%1 = alloca i32
		# 	store i32 0, i32* %1
		addiu	 $1, $0, 0
		sw		 $1, 4($29)
		# 	br label %2
		j bb_testGetInt_2
	# basicBlock_2
		bb_testGetInt_2:
		# 	%3 = load i32, i32* %1
		lw		 $5, 4($29)
		# 	%4 = icmp slt i32 %3, 9
		slti	 $6, $5, 9
		# 	%5 = icmp ne i1 %4, 0
		# 	br i1 %5, label %6, label %12
		addiu	 $1, $0, 0
		bne		 $6, $1, bb_testGetInt_6
		j bb_testGetInt_12
	# basicBlock_6
		bb_testGetInt_6:
		# 	%7 = alloca i32
		# 	%8 = call i32 @getint()
		addiu	 $2, $0, 5
		syscall
		xor		 $5, $2, $0
		# 	store i32 %8, i32* %7
		sw		 $5, 20($29)
		# 	%9 = load i32, i32* %7
		lw		 $6, 20($29)
		# 	call void @putint(i32 %9)
		xor		 $4, $6, $0
		addiu	 $2, $0, 1
		syscall
		la $4, str_0
		addiu	 $2, $0, 4
		syscall
		# 	%10 = load i32, i32* %1
		lw		 $7, 4($29)
		# 	%11 = add i32 %10, 1
		addiu	 $8, $7, 1
		# 	store i32 %11, i32* %1
		sw		 $8, 4($29)
		# 	br label %2
		j bb_testGetInt_2
	# basicBlock_12
		bb_testGetInt_12:
		# 	ret void
		jr $31
