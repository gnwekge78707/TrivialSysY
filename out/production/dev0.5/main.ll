declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
@a = dso_local global [510 x i32] zeroinitializer
@b = dso_local global [260100 x i32] zeroinitializer
@n = dso_local global i32 0
@m = dso_local global i32 0
@xxxx = dso_local global i32 0
@yyyy = dso_local global i32 0
@t = dso_local global i32 1234
@u = dso_local global i32 2345
@d = dso_local global [4 x i32] [i32 4, i32 -2, i32 15, i32 6]
@tmp_0 = dso_local global [5 x i32] [i32 0, i32 1, i32 2, i32 3, i32 4]

define dso_local void @pr() {
		call void @putch(i32 50)
	call void @putch(i32 48)
	call void @putch(i32 51)
	call void @putch(i32 55)
	call void @putch(i32 51)
	call void @putch(i32 52)
	call void @putch(i32 52)
	call void @putch(i32 55)
	call void @putch(i32 10)

	ret void
}

define dso_local void @nonsense() {
	ret void
}

define dso_local void @matrix() {
	%1 = alloca [8 x i32]
	%2 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 0
	store i32 -1, i32* %2
	%3 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 1
	store i32 8, i32* %3
	%4 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 2
	store i32 -8, i32* %4
	%5 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 3
	store i32 24, i32* %5
	%6 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 4
	store i32 -1, i32* %6
	%7 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 5
	store i32 -8, i32* %7
	%8 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 6
	store i32 1, i32* %8
	%9 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 7
	store i32 2, i32* %9
	%10 = alloca [3 x i32]
	%11 = getelementptr [3 x i32], [3 x i32]* %10, i32 0, i32 0
	store i32 1, i32* %11
	%12 = getelementptr [3 x i32], [3 x i32]* %10, i32 0, i32 1
	store i32 -2, i32* %12
	%13 = getelementptr [3 x i32], [3 x i32]* %10, i32 0, i32 2
	store i32 3, i32* %13
	%14 = alloca [4 x i32]
	%15 = load i32, i32* @t
	%16 = load i32, i32* @u
	%17 = add i32 %15, %16
	%18 = alloca i32
	store i32 %17, i32* %18
	%19 = load i32, i32* @t
	%20 = load i32, i32* @u
	%21 = mul i32 %19, %20
	store i32 %21, i32* %18
	%22 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 0
	store i32 26, i32* %22
	%23 = getelementptr [8 x i32], [8 x i32]* %1, i32 0, i32 0
	store i32 33, i32* %23
	ret void
}

define dso_local void @logic() {
	%1 = alloca i32
	store i32 0, i32* %1
	%2 = alloca i32
	store i32 0, i32* %2
	%3 = load i32, i32* %1
	%4 = load i32, i32* %2
	%5 = icmp sle i32 %3, %4
	%6 = icmp ne i1 %5, 0
	br i1 %6, label %12, label %7
7:
	%8 = load i32, i32* %1
	%9 = load i32, i32* %2
	%10 = icmp sge i32 %8, %9
	%11 = icmp ne i1 %10, 0
	br i1 %11, label %18, label %14
12:
	%13 = load i32, i32* %2
	store i32 %13, i32* %1
	br label %7
14:
	%15 = load i32, i32* %1
	%16 = icmp slt i32 %15, 100
	%17 = icmp ne i1 %16, 0
	br i1 %17, label %26, label %20
18:
	%19 = load i32, i32* %2
	store i32 %19, i32* %1
	br label %14
20:
	%21 = load i32, i32* %1
	%22 = zext i1 0 to i32
	%23 = icmp eq i32 %21, %22
	%24 = icmp ne i1 %23, 0
	br i1 %24, label %36, label %30
25:
	br label %20
26:
	%27 = load i32, i32* %2
	%28 = icmp sgt i32 %27, 100
	%29 = icmp ne i1 %28, 0
	br i1 %29, label %25, label %20
30:
	%31 = load i32, i32* %1
	%32 = zext i1 0 to i32
	%33 = icmp eq i32 %31, %32
	%34 = icmp ne i1 %33, 0
	br i1 %34, label %46, label %41
35:
	br label %30
36:
	%37 = load i32, i32* %2
	%38 = zext i1 0 to i32
	%39 = icmp ne i32 %37, %38
	%40 = icmp ne i1 %39, 0
	br i1 %40, label %35, label %30
41:
	%42 = load i32, i32* %1
	%43 = icmp eq i32 %42, 0
	%44 = icmp ne i1 %43, 0
	br i1 %44, label %53, label %54
45:
		call void @putch(i32 89)
	call void @putch(i32 101)
	call void @putch(i32 71)
	call void @putch(i32 49)
	call void @putch(i32 10)

	br label %41
46:
	%47 = load i32, i32* %2
	%48 = icmp eq i32 %47, 0
	%49 = icmp ne i1 %48, 0
	br i1 %49, label %45, label %41
50:
	%51 = load i32, i32* %1
	%52 = icmp ne i32 %51, 0
	br i1 %52, label %61, label %62
53:
		call void @putch(i32 89)
	call void @putch(i32 101)
	call void @putch(i32 71)
	call void @putch(i32 50)
	call void @putch(i32 10)

	br label %50
54:
	%55 = load i32, i32* %2
	%56 = icmp ne i32 %55, 0
	br i1 %56, label %53, label %50
57:
	%58 = load i32, i32* %1
	%59 = icmp eq i32 %58, 0
	%60 = icmp ne i1 %59, 0
	br i1 %60, label %70, label %71
61:
		call void @putch(i32 89)
	call void @putch(i32 101)
	call void @putch(i32 71)
	call void @putch(i32 51)
	call void @putch(i32 10)

	br label %57
62:
	%63 = load i32, i32* %2
	%64 = icmp eq i32 %63, 0
	%65 = icmp ne i1 %64, 0
	br i1 %65, label %61, label %57
66:
	%67 = load i32, i32* %1
	%68 = icmp eq i32 %67, 0
	%69 = icmp ne i1 %68, 0
	br i1 %69, label %83, label %75
70:
		call void @putch(i32 89)
	call void @putch(i32 101)
	call void @putch(i32 71)
	call void @putch(i32 52)
	call void @putch(i32 10)

	br label %66
71:
	%72 = load i32, i32* %2
	%73 = icmp eq i32 %72, 0
	%74 = icmp ne i1 %73, 0
	br i1 %74, label %70, label %66
75:
	%76 = load i32, i32* %1
	%77 = icmp eq i32 %76, 0
	%78 = load i32, i32* %2
	%79 = zext i1 %77 to i32
	%80 = icmp ne i32 %79, %78
	%81 = icmp ne i1 %80, 0
	br i1 %81, label %94, label %87
82:
		call void @putch(i32 89)
	call void @putch(i32 101)
	call void @putch(i32 71)
	call void @putch(i32 53)
	call void @putch(i32 10)

	br label %75
83:
	%84 = load i32, i32* %2
	%85 = icmp eq i32 %84, 0
	%86 = icmp ne i1 %85, 0
	br i1 %86, label %82, label %75
87:
	%88 = load i32, i32* %1
	%89 = load i32, i32* %2
	%90 = icmp eq i32 %89, 0
	%91 = zext i1 %90 to i32
	%92 = icmp ne i32 %88, %91
	%93 = icmp ne i1 %92, 0
	br i1 %93, label %97, label %99
94:
	%95 = load i32, i32* %2
	store i32 %95, i32* %1
	br label %87
96:
	ret void
97:
	%98 = load i32, i32* %1
	store i32 %98, i32* %2
	br label %96
99:
	%100 = load i32, i32* %1
	%101 = load i32, i32* %1
	%102 = sub i32 0, %101
	%103 = mul i32 %102, 100
	%104 = sdiv i32 %103, 10
	%105 = srem i32 %104, 10
	%106 = mul i32 %105, -1
	%107 = sub i32 %100, %106
	store i32 %107, i32* %2
	br label %96
}

define dso_local i32 @getInt() {
	%1 = alloca i32
	%2 = call i32 @getint()
	store i32 %2, i32* %1
	%3 = load i32, i32* %1
	ret i32 %3
}

define dso_local void @prd(i32*,i32,i32*) {
	%4 = alloca i32
	store i32 %1, i32* %4
	%5 = alloca i32
	store i32 0, i32* %5
	%6 = alloca i32
	store i32 0, i32* %6
	br label %7
7:
	%8 = load i32, i32* %5
	%9 = icmp sge i32 %8, 0
	%10 = icmp ne i1 %9, 0
	br i1 %10, label %11, label %24
11:
	%12 = load i32, i32* %5
	%13 = load i32, i32* %6
	%14 = mul i32 %12, 2
	%15 = mul i32 %13, 1
	%16 = add i32 %14, %15
	%17 = getelementptr i32, i32* %0, i32 %16
	%18 = load i32, i32* %17
	call void @putint(i32 %18)
		call void @putch(i32 10)

	%19 = load i32, i32* %6
	%20 = add i32 %19, 1
	store i32 %20, i32* %6
	%21 = load i32, i32* %6
	%22 = icmp eq i32 %21, 2
	%23 = icmp ne i1 %22, 0
	br i1 %23, label %29, label %25
24:
	ret void
25:
	%26 = load i32, i32* %5
	%27 = icmp eq i32 %26, 2
	%28 = icmp ne i1 %27, 0
	br i1 %28, label %33, label %34
29:
	store i32 0, i32* %6
	%30 = load i32, i32* %5
	%31 = add i32 %30, 1
	store i32 %31, i32* %5
	br label %25
32:
	br label %7
33:
	br label %24
34:
	br label %7
}

define dso_local void @f1(i32) {
	%2 = alloca i32
	store i32 %0, i32* %2
	%3 = load i32, i32* %2
	call void @putint(i32 %3)
	ret void
}

define dso_local void @f2(i32*) {
	%2 = getelementptr i32, i32* %0, i32 1
	store i32 10000, i32* %2
	ret void
}

define dso_local void @f3(i32) {
	%2 = alloca i32
	store i32 %0, i32* %2
	ret void
}

define dso_local void @f4(i32*) {
	ret void
}

define dso_local void @f5(i32,i32*) {
	%3 = alloca i32
	store i32 %0, i32* %3
	ret void
}

define dso_local i32 @main() {
	call void @pr()
	call void @nonsense()
	%1 = call i32 @getInt()
	store i32 %1, i32* @n
	%2 = call i32 @getInt()
	store i32 %2, i32* @m
	call void @matrix()
	%3 = load i32, i32* @n
	call void @f1(i32 %3)
	%4 = getelementptr [4 x i32], [4 x i32]* @d, i32 0, i32 0
	call void @f2(i32* %4)
	%5 = load i32, i32* @n
	%6 = load i32, i32* @m
	%7 = add i32 %5, %6
	call void @f3(i32 %7)
	%8 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 0
	%9 = load i32, i32* %8
	%10 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 1
	%11 = load i32, i32* %10
	%12 = add i32 %9, %11
	call void @f3(i32 %12)
	call void @f3(i32 4)
	%13 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 0
	%14 = load i32, i32* %13
	%15 = mul i32 2, %14
	call void @f3(i32 %15)
	%16 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 0
	%17 = load i32, i32* %16
	%18 = load i32, i32* @n
	%19 = add i32 %17, %18
	call void @f3(i32 %19)
	%20 = load i32, i32* @m
	%21 = add i32 2, %20
	call void @f3(i32 %21)
	%22 = getelementptr [4 x i32], [4 x i32]* @d, i32 0, i32 2
	call void @f4(i32* %22)
	%23 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 0
	%24 = load i32, i32* %23
	%25 = getelementptr [4 x i32], [4 x i32]* @d, i32 0, i32 0
	call void @f5(i32 %24,i32* %25)
	%26 = load i32, i32* @n
	%27 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 0
	call void @f5(i32 %26,i32* %27)
	call void @logic()
	%28 = load i32, i32* @n
	%29 = alloca i32
	store i32 %28, i32* %29
	%30 = load i32, i32* @m
	%31 = alloca i32
	store i32 %30, i32* %31
	%32 = load i32, i32* %29
	%33 = load i32, i32* %29
	%34 = sub i32 0, %33
	%35 = add i32 %32, %34
	store i32 %35, i32* %29
	%36 = load i32, i32* %31
	%37 = load i32, i32* %29
	%38 = sub i32 0, %37
	%39 = sub i32 %36, %38
	store i32 %39, i32* %31
	%40 = load i32, i32* %29
	%41 = load i32, i32* %31
	%42 = icmp eq i32 %40, %41
	%43 = icmp ne i1 %42, 0
	br i1 %43, label %48, label %49
44:
	%45 = load i32, i32* %29
	%46 = icmp eq i32 %45, 0
	%47 = icmp ne i1 %46, 0
	br i1 %47, label %74, label %75
48:
	store i32 1, i32* %29
	store i32 1, i32* %31
	br label %44
49:
	%50 = load i32, i32* %29
	%51 = load i32, i32* %31
	%52 = icmp sle i32 %50, %51
	%53 = icmp ne i1 %52, 0
	br i1 %53, label %55, label %56
54:
	br label %44
55:
	store i32 1, i32* %29
	store i32 2, i32* %31
	br label %54
56:
	store i32 2, i32* %29
	store i32 1, i32* %31
	br label %54
57:
	%58 = load i32, i32* @n
	%59 = load i32, i32* @m
	call void @putint(i32 %58)
		call void @putch(i32 32)
	call void @putch(i32 45)
	call void @putch(i32 45)
	call void @putch(i32 32)

	call void @putint(i32 %59)
		call void @putch(i32 10)

	%60 = getelementptr [4 x i32], [4 x i32]* @d, i32 0, i32 0
	%61 = getelementptr [5 x i32], [5 x i32]* @tmp_0, i32 0, i32 0
	call void @prd(i32* %60,i32 100,i32* %61)
	%62 = load i32, i32* @n
	%63 = load i32, i32* @m
	%64 = add i32 %62, %63
	call void @putint(i32 %64)
		call void @putch(i32 10)

	%65 = load i32, i32* @n
	%66 = load i32, i32* @m
	%67 = sub i32 %65, %66
	call void @putint(i32 %67)
		call void @putch(i32 10)

	%68 = load i32, i32* @n
	%69 = load i32, i32* @m
	%70 = mul i32 %68, %69
	call void @putint(i32 %70)
		call void @putch(i32 10)

	%71 = load i32, i32* @n
	%72 = load i32, i32* @m
	%73 = srem i32 %71, %72
	call void @putint(i32 %73)
		call void @putch(i32 10)

	ret i32 0
74:
	store i32 10000, i32* %29
	br label %57
75:
	br label %57
}
