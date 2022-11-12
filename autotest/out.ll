; ModuleID = 'llvm-link'
source_filename = "llvm-link"
target datalayout = "e-m:o-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-apple-macosx11.0.0"

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
@.str = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.1 = private unnamed_addr constant [3 x i8] c"%c\00", align 1

define dso_local void @add_machine(i32 %0, i32 %1, i32 %2) {
  %4 = alloca i32, align 4
  store i32 %0, i32* %4, align 4
  %5 = alloca i32, align 4
  store i32 %1, i32* %5, align 4
  %6 = alloca i32, align 4
  store i32 %2, i32* %6, align 4
  %7 = alloca i32, align 4
  %8 = alloca i32, align 4
  %9 = alloca i32, align 4
  ret void
}

define dso_local i32 @arr_check_2(i32* %0, i32 %1, i32 %2) {
  %4 = alloca i32, align 4
  store i32 %1, i32* %4, align 4
  %5 = alloca i32, align 4
  store i32 %2, i32* %5, align 4
  ret i32 1
}

define dso_local void @arr_check_1(i32* %0) {
  %2 = alloca i32, align 4
  store i32 1, i32* %2, align 4
  %3 = load i32, i32* %2, align 4
  call void @putint(i32 %3)
  call void @putch(i32 10)
  ret void
}

define dso_local i32 @main() {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  store i32 1, i32* %2, align 4
  %3 = alloca i32, align 4
  store i32 2, i32* %3, align 4
  %4 = alloca i32, align 4
  store i32 0, i32* %4, align 4
  %5 = alloca i32, align 4
  store i32 0, i32* %5, align 4
  %6 = alloca i32, align 4
  store i32 0, i32* %6, align 4
  %7 = alloca i32, align 4
  store i32 1, i32* %7, align 4
  %8 = alloca i32, align 4
  store i32 2, i32* %8, align 4
  %9 = alloca i32, align 4
  store i32 0, i32* %9, align 4
  %10 = alloca i32, align 4
  store i32 2, i32* %10, align 4
  %11 = alloca i32, align 4
  store i32 2, i32* %11, align 4
  %12 = alloca i32, align 4
  store i32 2, i32* %12, align 4
  %13 = alloca i32, align 4
  store i32 3, i32* %13, align 4
  %14 = alloca [3 x i32], align 4
  %15 = getelementptr [3 x i32], [3 x i32]* %14, i32 0, i32 0
  store i32 1, i32* %15, align 4
  %16 = getelementptr [3 x i32], [3 x i32]* %14, i32 0, i32 1
  store i32 2, i32* %16, align 4
  %17 = getelementptr [3 x i32], [3 x i32]* %14, i32 0, i32 2
  store i32 3, i32* %17, align 4
  %18 = alloca i32, align 4
  %19 = alloca i32, align 4
  store i32 20, i32* %19, align 4
  %20 = alloca [10 x i32], align 4
  %21 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 0
  store i32 1, i32* %21, align 4
  %22 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 1
  store i32 1, i32* %22, align 4
  %23 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 2
  store i32 1, i32* %23, align 4
  %24 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 3
  store i32 1, i32* %24, align 4
  %25 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 4
  store i32 1, i32* %25, align 4
  %26 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 5
  store i32 2, i32* %26, align 4
  %27 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 6
  store i32 2, i32* %27, align 4
  %28 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 7
  store i32 2, i32* %28, align 4
  %29 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 8
  store i32 2, i32* %29, align 4
  %30 = getelementptr [10 x i32], [10 x i32]* %20, i32 0, i32 9
  store i32 2, i32* %30, align 4
  %31 = alloca [16 x i32], align 4
  %32 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 0
  store i32 1, i32* %32, align 4
  %33 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 1
  store i32 2, i32* %33, align 4
  %34 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 2
  store i32 3, i32* %34, align 4
  %35 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 3
  store i32 4, i32* %35, align 4
  %36 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 4
  store i32 1, i32* %36, align 4
  %37 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 5
  store i32 2, i32* %37, align 4
  %38 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 6
  store i32 3, i32* %38, align 4
  %39 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 7
  store i32 4, i32* %39, align 4
  %40 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 8
  store i32 1, i32* %40, align 4
  %41 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 9
  store i32 2, i32* %41, align 4
  %42 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 10
  store i32 3, i32* %42, align 4
  %43 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 11
  store i32 4, i32* %43, align 4
  %44 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 12
  store i32 1, i32* %44, align 4
  %45 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 13
  store i32 2, i32* %45, align 4
  %46 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 14
  store i32 3, i32* %46, align 4
  %47 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 15
  store i32 4, i32* %47, align 4
  %48 = load i32, i32* %10, align 4
  %49 = load i32, i32* %2, align 4
  %50 = add i32 %48, %49
  %51 = load i32, i32* %11, align 4
  %52 = load i32, i32* %12, align 4
  call void @add_machine(i32 %50, i32 %51, i32 %52)
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
  %55 = load i32, i32* %19, align 4
  %56 = add i32 4, %55
  %57 = load i32, i32* %10, align 4
  %58 = add i32 %56, %57
  store i32 %58, i32* %1, align 4
  %59 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 0
  store i32 1, i32* %59, align 4
  %60 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 1
  store i32 2, i32* %60, align 4
  %61 = getelementptr [100 x i32], [100 x i32]* @a12, i32 0, i32 0
  store i32 2, i32* %61, align 4
  store i32 0, i32* %3, align 4
  store i32 0, i32* %7, align 4
  %62 = load i32, i32* %4, align 4
  %63 = add i32 %62, 4
  store i32 %63, i32* %4, align 4
  %64 = load i32, i32* %4, align 4
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
  %66 = load i32, i32* %65, align 4
  %67 = load i32, i32* %4, align 4
  %68 = add i32 %66, %67
  %69 = getelementptr [16 x i32], [16 x i32]* %31, i32 0, i32 1
  store i32 %68, i32* %69, align 4
  %70 = alloca i32, align 4
  store i32 2, i32* %70, align 4
  %71 = alloca i32, align 4
  %72 = alloca i32, align 4
  %73 = alloca i32, align 4
  %74 = alloca i32, align 4
  %75 = alloca i32, align 4
  %76 = load i32, i32* %70, align 4
  %77 = add i32 %76, 1
  store i32 %77, i32* %70, align 4
  %78 = load i32, i32* %70, align 4
  %79 = add i32 %78, 1
  %80 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 0
  store i32 %79, i32* %80, align 4
  %81 = load i32, i32* %70, align 4
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
  %82 = load i32, i32* %4, align 4
  %83 = add i32 %82, 1
  store i32 %83, i32* %4, align 4
  %84 = load i32, i32* %4, align 4
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

85:                                               ; preds = %107, %0
  %86 = load i32, i32* %3, align 4
  %87 = icmp sle i32 %86, 9
  %88 = icmp ne i1 %87, false
  br i1 %88, label %89, label %90

89:                                               ; preds = %85
  store i32 0, i32* %7, align 4
  br label %94

90:                                               ; preds = %85
  %91 = load i32, i32* %3, align 4
  %92 = icmp sgt i32 %91, 1
  %93 = icmp ne i1 %92, false
  br i1 %93, label %114, label %117

94:                                               ; preds = %98, %89
  %95 = load i32, i32* %7, align 4
  %96 = icmp sle i32 %95, 9
  %97 = icmp ne i1 %96, false
  br i1 %97, label %98, label %107

98:                                               ; preds = %94
  %99 = load i32, i32* %3, align 4
  %100 = load i32, i32* %7, align 4
  %101 = mul i32 %99, 10
  %102 = mul i32 %100, 1
  %103 = add i32 %101, %102
  %104 = getelementptr [100 x i32], [100 x i32]* @a12, i32 0, i32 %103
  store i32 1, i32* %104, align 4
  %105 = load i32, i32* %7, align 4
  %106 = add i32 %105, 1
  store i32 %106, i32* %7, align 4
  br label %94

107:                                              ; preds = %94
  %108 = load i32, i32* %3, align 4
  %109 = add i32 %108, 1
  store i32 %109, i32* %3, align 4
  br label %85

110:                                              ; preds = %117, %114
  %111 = load i32, i32* %3, align 4
  %112 = icmp sgt i32 %111, 1
  %113 = icmp ne i1 %112, false
  br i1 %113, label %124, label %120

114:                                              ; preds = %90
  %115 = load i32, i32* %3, align 4
  %116 = sub i32 %115, 1
  store i32 %116, i32* %3, align 4
  store i32 1, i32* %3, align 4
  store i32 -1, i32* %3, align 4
  br label %110

117:                                              ; preds = %90
  %118 = load i32, i32* %3, align 4
  %119 = add i32 %118, 1
  store i32 %119, i32* %3, align 4
  br label %110

120:                                              ; preds = %124, %110
  %121 = load i32, i32* %3, align 4
  %122 = icmp sgt i32 %121, 1
  %123 = icmp ne i1 %122, false
  br i1 %123, label %134, label %125

124:                                              ; preds = %110
  br label %120

125:                                              ; preds = %134, %120
  %126 = getelementptr [10 x i32], [10 x i32]* @a9, i32 0, i32 0
  %127 = load i32, i32* %126, align 4
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
  %129 = load i32, i32* %128, align 4
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
  %131 = load i32, i32* %130, align 4
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
  %133 = load i32, i32* %132, align 4
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

134:                                              ; preds = %120
  br label %125
}

; Function Attrs: noinline nounwind optnone ssp uwtable
define i32 @getint() #0 {
  %1 = alloca i32, align 4
  %2 = call i32 ([3 x i8]*, ...) @scanf([3 x i8]* noundef @.str, i32* noundef %1)
  %3 = load i32, i32* %1, align 4
  ret i32 %3
}

declare i32 @scanf([3 x i8]* noundef, ...) #1

; Function Attrs: noinline nounwind optnone ssp uwtable
define void @putint(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, i32* %2, align 4
  %3 = load i32, i32* %2, align 4
  %4 = call i32 ([3 x i8]*, ...) @printf([3 x i8]* noundef @.str, i32 noundef %3)
  ret void
}

declare i32 @printf([3 x i8]* noundef, ...) #1

; Function Attrs: noinline nounwind optnone ssp uwtable
define void @putch(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, i32* %2, align 4
  %3 = load i32, i32* %2, align 4
  %4 = call i32 ([3 x i8]*, ...) @printf([3 x i8]* noundef @.str.1, i32 noundef %3)
  ret void
}

attributes #0 = { noinline nounwind optnone ssp uwtable "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="penryn" "target-features"="+cx16,+cx8,+fxsr,+mmx,+sahf,+sse,+sse2,+sse3,+sse4.1,+ssse3,+x87" "tune-cpu"="generic" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="penryn" "target-features"="+cx16,+cx8,+fxsr,+mmx,+sahf,+sse,+sse2,+sse3,+sse4.1,+ssse3,+x87" "tune-cpu"="generic" }

!llvm.ident = !{!0}
!llvm.module.flags = !{!1, !2, !3, !4}

!0 = !{!"Homebrew clang version 15.0.3"}
!1 = !{i32 1, !"wchar_size", i32 4}
!2 = !{i32 7, !"PIC Level", i32 2}
!3 = !{i32 7, !"uwtable", i32 2}
!4 = !{i32 7, !"frame-pointer", i32 2}
