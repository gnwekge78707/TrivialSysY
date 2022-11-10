declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
@b1 = dso_local global i32 2
@b2 = dso_local global i32 -5
@b3 = dso_local global i32 6

define dso_local i32 @main() {
		call void @putch(i32 49)
	call void @putch(i32 57)
	call void @putch(i32 50)
	call void @putch(i32 51)
	call void @putch(i32 49)
	call void @putch(i32 50)
	call void @putch(i32 48)
	call void @putch(i32 52)

		call void @putch(i32 10)

	%1 = alloca i32
	store i32 10, i32* %1
	br label %2
2:
	%3 = load i32, i32* %1
	%4 = icmp ne i32 %3, 0
	br i1 %4, label %5, label %12
5:
	%6 = load i32, i32* %1
	%7 = sub i32 %6, 1
	store i32 %7, i32* %1
	%8 = load i32, i32* %1
	%9 = load i32, i32* @b3
	%10 = icmp slt i32 %8, %9
	%11 = icmp ne i1 %10, 0
	br i1 %11, label %21, label %17
12:
	%13 = load i32, i32* %1
	%14 = load i32, i32* @b1
	%15 = icmp ne i32 %13, %14
	%16 = icmp ne i1 %15, 0
	br i1 %16, label %41, label %36
17:
	%18 = load i32, i32* %1
	%19 = icmp slt i32 %18, 1
	%20 = icmp ne i1 %19, 0
	br i1 %20, label %26, label %22
21:
	br label %2
22:
	%23 = load i32, i32* %1
	%24 = icmp eq i32 %23, 3
	%25 = icmp ne i1 %24, 0
	br i1 %25, label %32, label %33
26:
	br label %12
27:
	%28 = load i32, i32* %1
	%29 = load i32, i32* @b1
	%30 = icmp eq i32 %28, %29
	%31 = icmp ne i1 %30, 0
	br i1 %31, label %35, label %34
32:
		call void @putch(i32 43)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 99)
	call void @putch(i32 111)
	call void @putch(i32 114)
	call void @putch(i32 114)
	call void @putch(i32 101)
	call void @putch(i32 99)
	call void @putch(i32 116)
	call void @putch(i32 33)
	call void @putch(i32 92)
	call void @putch(i32 110)

	br label %27
33:
		call void @putch(i32 43)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 101)
	call void @putch(i32 114)
	call void @putch(i32 114)
	call void @putch(i32 111)
	call void @putch(i32 114)
	call void @putch(i32 33)
	call void @putch(i32 92)
	call void @putch(i32 110)

	br label %27
34:
	br label %2
35:
	br label %12
36:
	%37 = load i32, i32* @b1
	%38 = add i32 1, %37
		call void @putch(i32 97)
	call void @putch(i32 49)
	call void @putch(i32 43)
	call void @putch(i32 98)
	call void @putch(i32 49)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)

	call void @putint(i32 %38)
		call void @putch(i32 10)

	%39 = load i32, i32* @b2
	%40 = add i32 3, %39
		call void @putch(i32 97)
	call void @putch(i32 50)
	call void @putch(i32 43)
	call void @putch(i32 98)
	call void @putch(i32 50)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)

	call void @putint(i32 %40)
		call void @putch(i32 10)

	ret i32 0
41:
	%42 = load i32, i32* %1
	%43 = zext i1 0 to i32
	%44 = icmp eq i32 %42, %43
	%45 = icmp ne i1 %44, 0
	br i1 %45, label %47, label %48
46:
	br label %36
47:
		call void @putch(i32 66)
	call void @putch(i32 114)
	call void @putch(i32 101)
	call void @putch(i32 97)
	call void @putch(i32 107)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 101)
	call void @putch(i32 114)
	call void @putch(i32 114)
	call void @putch(i32 111)
	call void @putch(i32 114)
	call void @putch(i32 33)
	call void @putch(i32 92)
	call void @putch(i32 110)

	br label %46
48:
		call void @putch(i32 67)
	call void @putch(i32 111)
	call void @putch(i32 110)
	call void @putch(i32 116)
	call void @putch(i32 105)
	call void @putch(i32 110)
	call void @putch(i32 117)
	call void @putch(i32 101)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 101)
	call void @putch(i32 114)
	call void @putch(i32 114)
	call void @putch(i32 111)
	call void @putch(i32 114)
	call void @putch(i32 33)
	call void @putch(i32 92)
	call void @putch(i32 110)

	br label %46
}
