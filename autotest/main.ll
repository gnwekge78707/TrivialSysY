declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
@g1 = dso_local global i32 0 
@g2 = dso_local global i32 0 
@g3 = dso_local global i32 0 
@e1 = dso_local global i32 1
@f1 = dso_local global i32 1
@f2 = dso_local global i32 1
@i1 = dso_local global i32 2
@i2 = dso_local global i32 0 
@a9 = dso_local global [10 x i32] zeroinitializer 
@a12 = dso_local global [100 x i32] zeroinitializer 
@h1 = dso_local global [10 x i32] zeroinitializer 
@h2 = dso_local global [100 x i32] zeroinitializer 
@h3 = dso_local global i32 0 

define dso_local void @add_machine(i32,i32,i32) {
	%4 = alloca i32
	store i32 %0, i32* %4
	%5 = alloca i32
	store i32 %1, i32* %5
	%6 = alloca i32
	store i32 %2, i32* %6
	%7 = alloca i32
	%8 = alloca i32
	%9 = alloca i32
	ret void
}

define dso_local i32 @arr_check_2(i32*,i32,i32) {
	%4 = alloca i32
	store i32 %1, i32* %4
	%5 = alloca i32
	store i32 %2, i32* %5
	ret i32 1
}

define dso_local void @arr_check_1(i32*) {
	%2 = alloca i32
	store i32 1, i32* %2
	%3 = load i32, i32* %2
	call void @putint(i32 %3)
		call void @putch(i32 10)

	ret void
}

define dso_local i32 @main() {
	%1 = alloca i32
	%2 = alloca i32
	store i32 1, i32* %2
	%3 = alloca i32
	store i32 2, i32* %3
	%4 = alloca i32
	store i32 0, i32* %4
	%5 = alloca i32
	store i32 0, i32* %5
	%6 = alloca i32
	store i32 0, i32* %6
	%7 = alloca i32
	store i32 1, i32* %7
	%8 = alloca i32
	store i32 2, i32* %8
	%9 = alloca i32
	store i32 0, i32* %9
	%10 = alloca i32
	store i32 2, i32* %10
	%11 = alloca i32
	store i32 2, i32* %11
	%12 = alloca i32
	store i32 2, i32* %12
	%13 = alloca i32
	store i32 3, i32* %13
	%14 = alloca [3 x i32]
	%15 = getelementptr [3 x i32], [3 x i32]* %14, i32 0, i32 0
	store i32 1, i32* %15
	%16 = getelementptr [3 x i32], [3 x i32]* %14, i32 0, i32 1
	store i32 2, i32* %16
	%17 = getelementptr [3 x i32], [3 x i32]* %14, i32 0, i32 2
	store i32 3, i32* %17
	%18 = alloca i32
	%19 = alloca i32
	store i32 20, i32* %19
	%20 = alloca [10 x i32]
	%21 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 0
	store i32 1, i32* %21
	%22 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 1
	store i32 1, i32* %22
	%23 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 2
	store i32 1, i32* %23
	%24 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 3
	store i32 1, i32* %24
	%25 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 4
	store i32 1, i32* %25
	%26 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 5
	store i32 2, i32* %26
	%27 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 6
	store i32 2, i32* %27
	%28 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 7
	store i32 2, i32* %28
	%29 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 8
	store i32 2, i32* %29
	%30 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 9
	store i32 2, i32* %30
	%31 = alloca [16 x i32]
	%32 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 0
	store i32 1, i32* %32
	%33 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 1
	store i32 2, i32* %33
	%34 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 2
	store i32 3, i32* %34
	%35 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 3
	store i32 4, i32* %35
	%36 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 4
	store i32 1, i32* %36
	%37 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 5
	store i32 2, i32* %37
	%38 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 6
	store i32 3, i32* %38
	%39 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 7
	store i32 4, i32* %39
	%40 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 8
	store i32 1, i32* %40
	%41 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 9
	store i32 2, i32* %41
	%42 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 10
	store i32 3, i32* %42
	%43 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 11
	store i32 4, i32* %43
	%44 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 12
	store i32 1, i32* %44
	%45 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 13
	store i32 2, i32* %45
	%46 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 14
	store i32 3, i32* %46
	%47 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 15
	store i32 4, i32* %47
	%48 = load i32, i32* %10
	%49 = load i32, i32* %2
	%50 = add i32 %48, %49
	%51 = load i32, i32* %11
	%52 = load i32, i32* %12
	call void @add_machine(i32 %50,i32 %51,i32 %52)
		call void @putch(i32 50)
	call void @putch(i32 48)
	call void @putch(i32 51)
	call void @putch(i32 55)
	call void @putch(i32 51)
	call void @putch(i32 54)
	call void @putch(i32 48)
	call void @putch(i32 56)
	call void @putch(i32 10)

	%53 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 0
	call void @arr_check_1(i32* %53)
	%54 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 0
	call void @arr_check_1(i32* %54)
	%55 = load i32, i32* %19
	%56 = add i32 4, %55
	%57 = load i32, i32* %10
	%58 = add i32 %56, %57
	store i32 %58, i32* %1
	%59 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 0
	store i32 1, i32* %59
	%60 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 1
	store i32 2, i32* %60
	%61 = getelementptr [100 x i32], [100 x i32]* @a12, i32 0, i32 0
	store i32 2, i32* %61
	store i32 0, i32* %3
	store i32 0, i32* %7
	%62 = load i32, i32* %4
	%63 = add i32 %62, 4
	store i32 %63, i32* %4
	%64 = load i32, i32* %4
		call void @putch(i32 116)
	call void @putch(i32 104)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 111)
	call void @putch(i32 117)
	call void @putch(i32 116)
	call void @putch(i32 115)
	call void @putch(i32 105)
	call void @putch(i32 100)
	call void @putch(i32 101)
	call void @putch(i32 32)
	call void @putch(i32 103)
	call void @putch(i32 49)
	call void @putch(i32 32)

	call void @putint(i32 %64)
		call void @putch(i32 10)

	%65 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 1
	%66 = load i32, i32* %65
	%67 = load i32, i32* %4
	%68 = add i32 %66, %67
	%69 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 1
	store i32 %68, i32* %69
	%70 = alloca i32
	store i32 2, i32* %70
	%71 = alloca i32
	%72 = alloca i32
	%73 = alloca i32
	%74 = alloca i32
	%75 = alloca i32
	%76 = load i32, i32* %70
	%77 = add i32 %76, 1
	store i32 %77, i32* %70
	%78 = load i32, i32* %70
	%79 = add i32 %78, 1
	%80 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 0
	store i32 %79, i32* %80
	%81 = load i32, i32* %70
		call void @putch(i32 116)
	call void @putch(i32 104)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 110)
	call void @putch(i32 115)
	call void @putch(i32 105)
	call void @putch(i32 100)
	call void @putch(i32 101)
	call void @putch(i32 32)
	call void @putch(i32 103)
	call void @putch(i32 49)
	call void @putch(i32 32)

	call void @putint(i32 %81)
		call void @putch(i32 10)

	%82 = load i32, i32* %4
	%83 = add i32 %82, 1
	store i32 %83, i32* %4
	%84 = load i32, i32* %4
		call void @putch(i32 116)
	call void @putch(i32 104)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)
	call void @putch(i32 111)
	call void @putch(i32 117)
	call void @putch(i32 116)
	call void @putch(i32 115)
	call void @putch(i32 105)
	call void @putch(i32 100)
	call void @putch(i32 101)
	call void @putch(i32 32)
	call void @putch(i32 103)
	call void @putch(i32 49)
	call void @putch(i32 32)

	call void @putint(i32 %84)
		call void @putch(i32 32)
	call void @putch(i32 97)
	call void @putch(i32 103)
	call void @putch(i32 115)
	call void @putch(i32 105)
	call void @putch(i32 110)
	call void @putch(i32 10)

	br label %85
85:
	%86 = load i32, i32* %3
	%87 = icmp sle i32 %86, 9
	%88 = icmp ne i1 %87, 0
	br i1 %88, label %89, label %90
89:
	store i32 0, i32* %7
	br label %94
90:
	%91 = load i32, i32* %3
	%92 = icmp sgt i32 %91, 1
	%93 = icmp ne i1 %92, 0
	br i1 %93, label %114, label %117
94:
	%95 = load i32, i32* %7
	%96 = icmp sle i32 %95, 9
	%97 = icmp ne i1 %96, 0
	br i1 %97, label %98, label %107
98:
	%99 = load i32, i32* %3
	%100 = load i32, i32* %7
	%101 = mul i32 %99, 10
	%102 = mul i32 %100, 1
	%103 = add i32 %101, %102
	%104 = getelementptr [100 x i32], [100 x i32]* @a12, i32 0, i32 %103
	store i32 1, i32* %104
	%105 = load i32, i32* %7
	%106 = add i32 %105, 1
	store i32 %106, i32* %7
	br label %94
107:
	%108 = load i32, i32* %3
	%109 = add i32 %108, 1
	store i32 %109, i32* %3
	br label %85
110:
	%111 = load i32, i32* %3
	%112 = icmp sgt i32 %111, 1
	%113 = icmp ne i1 %112, 0
	br i1 %113, label %124, label %120
114:
	%115 = load i32, i32* %3
	%116 = sub i32 %115, 1
	store i32 %116, i32* %3
	store i32 1, i32* %3
	store i32 -1, i32* %3
	br label %110
117:
	%118 = load i32, i32* %3
	%119 = add i32 %118, 1
	store i32 %119, i32* %3
	br label %110
120:
	%121 = load i32, i32* %3
	%122 = icmp sgt i32 %121, 1
	%123 = icmp ne i1 %122, 0
	br i1 %123, label %134, label %125
124:
	br label %120
125:
	%126 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 0
	%127 = load i32, i32* %126
		call void @putch(i32 97)
	call void @putch(i32 57)
	call void @putch(i32 91)
	call void @putch(i32 48)
	call void @putch(i32 93)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)

	call void @putint(i32 %127)
		call void @putch(i32 10)

	%128 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 1
	%129 = load i32, i32* %128
		call void @putch(i32 97)
	call void @putch(i32 49)
	call void @putch(i32 51)
	call void @putch(i32 91)
	call void @putch(i32 48)
	call void @putch(i32 93)
	call void @putch(i32 91)
	call void @putch(i32 49)
	call void @putch(i32 93)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)

	call void @putint(i32 %129)
		call void @putch(i32 10)

	%130 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 2
	%131 = load i32, i32* %130
		call void @putch(i32 97)
	call void @putch(i32 49)
	call void @putch(i32 51)
	call void @putch(i32 91)
	call void @putch(i32 48)
	call void @putch(i32 93)
	call void @putch(i32 91)
	call void @putch(i32 50)
	call void @putch(i32 93)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)

	call void @putint(i32 %131)
		call void @putch(i32 10)

	%132 = getelementptr [100 x i32], [100 x i32]* @a12, i32 0, i32 99
	%133 = load i32, i32* %132
		call void @putch(i32 97)
	call void @putch(i32 49)
	call void @putch(i32 50)
	call void @putch(i32 91)
	call void @putch(i32 57)
	call void @putch(i32 93)
	call void @putch(i32 91)
	call void @putch(i32 57)
	call void @putch(i32 93)
	call void @putch(i32 32)
	call void @putch(i32 105)
	call void @putch(i32 115)
	call void @putch(i32 32)

	call void @putint(i32 %133)
	ret i32 0
134:
	br label %125
}
