; ModuleID = 'llvm-link'
source_filename = "llvm-link"
target datalayout = "e-m:o-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-apple-macosx11.0.0"

@var_val_0 = dso_local global [8 x i32] zeroinitializer
@var_val_1 = dso_local global [2 x i32] [i32 10, i32 9]
@var_val_2 = dso_local global [4 x i32] [i32 3, i32 2, i32 1, i32 0]
@.str = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.1 = private unnamed_addr constant [3 x i8] c"%c\00", align 1

define dso_local i32 @func1(i32* %0, i32 %1) {
  %3 = alloca i32, align 4
  store i32 %1, i32* %3, align 4
  %4 = load i32, i32* %3, align 4
  %5 = sub i32 %4, 1
  %6 = alloca i32, align 4
  store i32 %5, i32* %6, align 4
  br label %7

7:                                                ; preds = %11, %2
  %8 = load i32, i32* %6, align 4
  %9 = icmp sge i32 %8, 0
  %10 = icmp ne i1 %9, false
  br i1 %10, label %11, label %22

11:                                               ; preds = %7
  %12 = getelementptr i32, i32* %0, i32 1
  %13 = load i32, i32* %12, align 4
  %14 = load i32, i32* %6, align 4
  %15 = mul i32 %14, 1
  %16 = getelementptr i32, i32* %0, i32 %15
  %17 = load i32, i32* %16, align 4
  %18 = add i32 %13, %17
  %19 = getelementptr i32, i32* %0, i32 1
  store i32 %18, i32* %19, align 4
  %20 = load i32, i32* %6, align 4
  %21 = sub i32 %20, 1
  store i32 %21, i32* %6, align 4
  br label %7

22:                                               ; preds = %7
  %23 = getelementptr i32, i32* %0, i32 1
  %24 = load i32, i32* %23, align 4
  call void @putch(i32 97)
  call void @putch(i32 91)
  call void @putch(i32 49)
  call void @putch(i32 93)
  call void @putch(i32 61)
  call void @putint(i32 %24)
  call void @putch(i32 10)
  %25 = getelementptr i32, i32* %0, i32 1
  %26 = load i32, i32* %25, align 4
  ret i32 %26
}

define dso_local void @func2(i32* %0, i32* %1) {
  %3 = alloca i32, align 4
  store i32 1, i32* %3, align 4
  br label %4

4:                                                ; preds = %8, %2
  %5 = load i32, i32* %3, align 4
  %6 = icmp sge i32 %5, 0
  %7 = icmp ne i1 %6, false
  br i1 %7, label %8, label %23

8:                                                ; preds = %4
  %9 = load i32, i32* %3, align 4
  %10 = mul i32 %9, 1
  %11 = getelementptr i32, i32* %0, i32 %10
  %12 = load i32, i32* %11, align 4
  %13 = load i32, i32* %3, align 4
  %14 = mul i32 %13, 1
  %15 = getelementptr [2 x i32], [2 x i32]* @var_val_1, i32 0, i32 %14
  %16 = load i32, i32* %15, align 4
  %17 = add i32 %12, %16
  %18 = load i32, i32* %3, align 4
  %19 = mul i32 %18, 1
  %20 = getelementptr i32, i32* %0, i32 %19
  store i32 %17, i32* %20, align 4
  %21 = load i32, i32* %3, align 4
  %22 = sub i32 %21, 1
  store i32 %22, i32* %3, align 4
  br label %4

23:                                               ; preds = %4
  %24 = getelementptr i32, i32* %0, i32 0
  %25 = load i32, i32* %24, align 4
  %26 = getelementptr i32, i32* %0, i32 1
  %27 = load i32, i32* %26, align 4
  call void @putch(i32 97)
  call void @putch(i32 91)
  call void @putch(i32 48)
  call void @putch(i32 93)
  call void @putch(i32 61)
  call void @putint(i32 %25)
  call void @putch(i32 44)
  call void @putch(i32 97)
  call void @putch(i32 91)
  call void @putch(i32 49)
  call void @putch(i32 93)
  call void @putch(i32 61)
  call void @putint(i32 %27)
  call void @putch(i32 10)
  %28 = load i32, i32* %3, align 4
  %29 = mul i32 %28, 2
  %30 = mul i32 0, 1
  %31 = add i32 %29, %30
  %32 = getelementptr i32, i32* %1, i32 %31
  %33 = load i32, i32* %32, align 4
  %34 = load i32, i32* %3, align 4
  %35 = mul i32 %34, 2
  %36 = mul i32 0, 1
  %37 = add i32 %35, %36
  %38 = getelementptr [4 x i32], [4 x i32]* @var_val_2, i32 0, i32 %37
  %39 = load i32, i32* %38, align 4
  %40 = add i32 %33, %39
  %41 = load i32, i32* %3, align 4
  %42 = mul i32 %41, 2
  %43 = mul i32 0, 1
  %44 = add i32 %42, %43
  %45 = getelementptr i32, i32* %1, i32 %44
  store i32 %40, i32* %45, align 4
  %46 = load i32, i32* %3, align 4
  %47 = mul i32 %46, 2
  %48 = mul i32 1, 1
  %49 = add i32 %47, %48
  %50 = getelementptr i32, i32* %1, i32 %49
  %51 = load i32, i32* %50, align 4
  %52 = load i32, i32* %3, align 4
  %53 = mul i32 %52, 2
  %54 = mul i32 1, 1
  %55 = add i32 %53, %54
  %56 = getelementptr [4 x i32], [4 x i32]* @var_val_2, i32 0, i32 %55
  %57 = load i32, i32* %56, align 4
  %58 = add i32 %51, %57
  %59 = load i32, i32* %3, align 4
  %60 = mul i32 %59, 2
  %61 = mul i32 0, 1
  %62 = add i32 %60, %61
  %63 = getelementptr i32, i32* %1, i32 %62
  store i32 %58, i32* %63, align 4
  %64 = load i32, i32* %3, align 4
  %65 = mul i32 %64, 2
  %66 = mul i32 0, 1
  %67 = add i32 %65, %66
  %68 = getelementptr i32, i32* %1, i32 %67
  %69 = load i32, i32* %68, align 4
  %70 = load i32, i32* %3, align 4
  %71 = mul i32 %70, 2
  %72 = mul i32 0, 1
  %73 = add i32 %71, %72
  %74 = getelementptr [4 x i32], [4 x i32]* @var_val_2, i32 0, i32 %73
  %75 = load i32, i32* %74, align 4
  %76 = add i32 %69, %75
  %77 = load i32, i32* %3, align 4
  %78 = mul i32 %77, 2
  %79 = mul i32 0, 1
  %80 = add i32 %78, %79
  %81 = getelementptr i32, i32* %1, i32 %80
  store i32 %76, i32* %81, align 4
  %82 = load i32, i32* %3, align 4
  %83 = mul i32 %82, 2
  %84 = mul i32 1, 1
  %85 = add i32 %83, %84
  %86 = getelementptr i32, i32* %1, i32 %85
  %87 = load i32, i32* %86, align 4
  %88 = load i32, i32* %3, align 4
  %89 = mul i32 %88, 2
  %90 = mul i32 1, 1
  %91 = add i32 %89, %90
  %92 = getelementptr [4 x i32], [4 x i32]* @var_val_2, i32 0, i32 %91
  %93 = load i32, i32* %92, align 4
  %94 = add i32 %87, %93
  %95 = load i32, i32* %3, align 4
  %96 = mul i32 %95, 2
  %97 = mul i32 0, 1
  %98 = add i32 %96, %97
  %99 = getelementptr i32, i32* %1, i32 %98
  store i32 %94, i32* %99, align 4
  %100 = getelementptr i32, i32* %0, i32 0
  %101 = load i32, i32* %100, align 4
  %102 = getelementptr i32, i32* %0, i32 1
  %103 = load i32, i32* %102, align 4
  call void @putch(i32 97)
  call void @putch(i32 91)
  call void @putch(i32 48)
  call void @putch(i32 93)
  call void @putch(i32 61)
  call void @putint(i32 %101)
  call void @putch(i32 44)
  call void @putch(i32 97)
  call void @putch(i32 91)
  call void @putch(i32 49)
  call void @putch(i32 93)
  call void @putch(i32 61)
  call void @putint(i32 %103)
  call void @putch(i32 10)
  %104 = alloca [2 x i32], align 4
  %105 = getelementptr [2 x i32], [2 x i32]* %104, i32 0, i32 0
  store i32 0, i32* %105, align 4
  %106 = getelementptr [2 x i32], [2 x i32]* %104, i32 0, i32 1
  store i32 0, i32* %106, align 4
  %107 = getelementptr i32, i32* %0, i32 0
  %108 = call i32 @func1(i32* %107, i32 2)
  %109 = getelementptr [2 x i32], [2 x i32]* %104, i32 0, i32 0
  store i32 %108, i32* %109, align 4
  %110 = getelementptr [2 x i32], [2 x i32]* %104, i32 0, i32 0
  %111 = load i32, i32* %110, align 4
  %112 = srem i32 %111, 123
  %113 = getelementptr [2 x i32], [2 x i32]* %104, i32 0, i32 1
  store i32 %112, i32* %113, align 4
  %114 = getelementptr [2 x i32], [2 x i32]* %104, i32 0, i32 0
  %115 = load i32, i32* %114, align 4
  %116 = getelementptr [2 x i32], [2 x i32]* %104, i32 0, i32 1
  %117 = load i32, i32* %116, align 4
  call void @putint(i32 %115)
  call void @putch(i32 44)
  call void @putint(i32 %117)
  call void @putch(i32 10)
  ret void
}

define dso_local i32 @main() {
  call void @putch(i32 49)
  call void @putch(i32 57)
  call void @putch(i32 51)
  call void @putch(i32 55)
  call void @putch(i32 51)
  call void @putch(i32 54)
  call void @putch(i32 53)
  call void @putch(i32 48)
  call void @putch(i32 10)
  %1 = alloca [4 x i32], align 4
  %2 = getelementptr [4 x i32], [4 x i32]* %1, i32 0, i32 0
  store i32 0, i32* %2, align 4
  %3 = getelementptr [4 x i32], [4 x i32]* %1, i32 0, i32 1
  store i32 0, i32* %3, align 4
  %4 = getelementptr [4 x i32], [4 x i32]* %1, i32 0, i32 2
  store i32 0, i32* %4, align 4
  %5 = getelementptr [4 x i32], [4 x i32]* %1, i32 0, i32 3
  store i32 0, i32* %5, align 4
  %6 = alloca [2 x i32], align 4
  %7 = getelementptr [2 x i32], [2 x i32]* %6, i32 0, i32 0
  store i32 100, i32* %7, align 4
  %8 = getelementptr [2 x i32], [2 x i32]* %6, i32 0, i32 1
  store i32 50, i32* %8, align 4
  %9 = getelementptr [2 x i32], [2 x i32]* %6, i32 0, i32 0
  %10 = getelementptr [4 x i32], [4 x i32]* %1, i32 0, i32 0
  call void @func2(i32* %9, i32* %10)
  call void @putch(i32 116)
  call void @putch(i32 101)
  call void @putch(i32 115)
  call void @putch(i32 116)
  call void @putch(i32 32)
  call void @putch(i32 105)
  call void @putch(i32 115)
  call void @putch(i32 32)
  call void @putch(i32 111)
  call void @putch(i32 118)
  call void @putch(i32 101)
  call void @putch(i32 114)
  call void @putch(i32 10)
  ret i32 0
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
