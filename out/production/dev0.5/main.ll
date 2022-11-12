declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
@d = dso_local global i32 1
@e = dso_local global [2 x i32] [i32 1, i32 2]
@f = dso_local global [4 x i32] [i32 1, i32 2, i32 3, i32 4]

define dso_local i32 @fk() {
	store i32 10, i32* @d
	%1 = load i32, i32* @d
	%2 = getelementptr [2 x i32], [2 x i32]* @e, i32 0, i32 1
	%3 = load i32, i32* %2
	%4 = add i32 %1, %3
	ret i32 %4
}

define dso_local i32 @pm(i32,i32) {
	%3 = alloca i32
	store i32 %0, i32* %3
	%4 = alloca i32
	store i32 %1, i32* %4
	%5 = load i32, i32* %3
	%6 = load i32, i32* %4
	%7 = add i32 %5, %6
	ret i32 %7
}

define dso_local void @mp(i32,i32*,i32*) {
	%4 = alloca i32
	store i32 %0, i32* %4
	%5 = load i32, i32* %4
	%6 = getelementptr i32, i32* %2, i32 2
	%7 = load i32, i32* %6
	%8 = add i32 %5, %7
	%9 = getelementptr i32, i32* %1, i32 0
	store i32 %8, i32* %9
	ret void
}

define dso_local i32 @main() {
		call void @putch(i32 65)
	call void @putch(i32 117)
	call void @putch(i32 116)
	call void @putch(i32 104)
	call void @putch(i32 111)
	call void @putch(i32 114)
	call void @putch(i32 58)
	call void @putch(i32 32)
	call void @putch(i32 49)
	call void @putch(i32 57)
	call void @putch(i32 51)
	call void @putch(i32 55)
	call void @putch(i32 54)
	call void @putch(i32 49)
	call void @putch(i32 54)
	call void @putch(i32 48)
	call void @putch(i32 10)

	%1 = alloca i32
	store i32 6, i32* %1
	%2 = alloca i32
	store i32 3, i32* %2
	%3 = alloca i32
	store i32 11, i32* %3
	%4 = alloca [2 x i32]
	%5 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 0
	store i32 11, i32* %5
	%6 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 1
	store i32 22, i32* %6
	%7 = alloca [4 x i32]
	%8 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 0
	store i32 11, i32* %8
	%9 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 1
	store i32 22, i32* %9
	%10 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 2
	store i32 33, i32* %10
	%11 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 3
	store i32 44, i32* %11
	br label %12
12:
	br label %13
13:
	%14 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 1
	%15 = load i32, i32* %14
	%16 = icmp eq i32 %15, -1
	%17 = icmp ne i1 %16, 0
	br i1 %17, label %47, label %41
18:
	call void @putint(i32 1)
		call void @putch(i32 10)

	%19 = load i32, i32* %3
	call void @putint(i32 %19)
		call void @putch(i32 10)

	%20 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 0
	%21 = load i32, i32* %20
	call void @putint(i32 %21)
		call void @putch(i32 10)

	%22 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 3
	%23 = load i32, i32* %22
	call void @putint(i32 %23)
		call void @putch(i32 10)

		call void @putch(i32 84)
	call void @putch(i32 104)
	call void @putch(i32 101)
	call void @putch(i32 32)
	call void @putch(i32 101)
	call void @putch(i32 110)
	call void @putch(i32 100)

	ret i32 0
24:
	%25 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 1
	store i32 1, i32* %25
	%26 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 1
	%27 = load i32, i32* %26
	%28 = sdiv i32 %27, 2
	%29 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 1
	store i32 %28, i32* %29
	br label %12
30:
	%31 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 1
	store i32 12, i32* %31
	br label %12
41:
	%42 = call i32 @fk()
	%43 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 0
	store i32 %42, i32* %43
	%44 = load i32, i32* %3
	%45 = getelementptr [2 x i32], [2 x i32]* %4, i32 0, i32 0
	%46 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 0
	call void @mp(i32 %44,i32* %45,i32* %46)
	br label %62
47:
	%48 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 0
	%49 = load i32, i32* %48
	%50 = icmp eq i32 %49, 5
	%51 = icmp ne i1 %50, 0
	br i1 %51, label %52, label %41
52:
	%53 = icmp ne i1 1, 0
	br i1 %53, label %54, label %41
54:
	%55 = call i32 @fk()
	%56 = zext i1 0 to i32
	%57 = icmp eq i32 %55, %56
	%58 = icmp ne i1 %57, 0
	br i1 %58, label %59, label %41
59:
	%60 = icmp ne i1 0, 0
	br i1 %60, label %30, label %41
61:
	br label %18
62:
		call void @putch(i32 84)
	call void @putch(i32 104)
	call void @putch(i32 101)
	call void @putch(i32 114)
	call void @putch(i32 101)
	call void @putch(i32 10)

		call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 10)

		call void @putch(i32 110)
	call void @putch(i32 111)
	call void @putch(i32 116)
	call void @putch(i32 104)
	call void @putch(i32 105)
	call void @putch(i32 110)
	call void @putch(i32 103)
	call void @putch(i32 10)

		call void @putch(i32 115)
	call void @putch(i32 112)
	call void @putch(i32 101)
	call void @putch(i32 99)
	call void @putch(i32 105)
	call void @putch(i32 97)
	call void @putch(i32 108)
	call void @putch(i32 10)

	br label %61
63:
	%64 = getelementptr [4 x i32], [4 x i32]* %7, i32 0, i32 0
	%65 = load i32, i32* %64
	%66 = icmp eq i32 %65, 5
	%67 = icmp ne i1 %66, 0
	br i1 %67, label %62, label %68
68:
	br label %62
69:
	br label %62
}
