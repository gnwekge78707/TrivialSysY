; ModuleID = 'llvm-link'
source_filename = "llvm-link"
target datalayout = "e-m:o-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-apple-macosx11.0.0"

@b1 = dso_local global i32 2
@b2 = dso_local global i32 -5
@b3 = dso_local global i32 6
@.str = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.1 = private unnamed_addr constant [3 x i8] c"%c\00", align 1

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
  %1 = alloca i32, align 4
  store i32 10, i32* %1, align 4
  br label %2

2:                                                ; preds = %34, %21, %0
  %3 = load i32, i32* %1, align 4
  %4 = icmp ne i32 %3, 0
  br i1 %4, label %5, label %12

5:                                                ; preds = %2
  %6 = load i32, i32* %1, align 4
  %7 = sub i32 %6, 1
  store i32 %7, i32* %1, align 4
  %8 = load i32, i32* %1, align 4
  %9 = load i32, i32* @b3, align 4
  %10 = icmp slt i32 %8, %9
  %11 = icmp ne i1 %10, false
  br i1 %11, label %21, label %17

12:                                               ; preds = %35, %26, %2
  %13 = load i32, i32* %1, align 4
  %14 = load i32, i32* @b1, align 4
  %15 = icmp ne i32 %13, %14
  %16 = icmp ne i1 %15, false
  br i1 %16, label %41, label %36

17:                                               ; preds = %5
  %18 = load i32, i32* %1, align 4
  %19 = icmp slt i32 %18, 1
  %20 = icmp ne i1 %19, false
  br i1 %20, label %26, label %22

21:                                               ; preds = %5
  br label %2

22:                                               ; preds = %17
  %23 = load i32, i32* %1, align 4
  %24 = icmp eq i32 %23, 3
  %25 = icmp ne i1 %24, false
  br i1 %25, label %32, label %33

26:                                               ; preds = %17
  br label %12

27:                                               ; preds = %33, %32
  %28 = load i32, i32* %1, align 4
  %29 = load i32, i32* @b1, align 4
  %30 = icmp eq i32 %28, %29
  %31 = icmp ne i1 %30, false
  br i1 %31, label %35, label %34

32:                                               ; preds = %22
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

33:                                               ; preds = %22
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

34:                                               ; preds = %27
  br label %2

35:                                               ; preds = %27
  br label %12

36:                                               ; preds = %46, %12
  %37 = load i32, i32* @b1, align 4
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
  %39 = load i32, i32* @b2, align 4
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

41:                                               ; preds = %12
  %42 = load i32, i32* %1, align 4
  %43 = zext i1 false to i32
  %44 = icmp eq i32 %42, %43
  %45 = icmp ne i1 %44, false
  br i1 %45, label %47, label %48

46:                                               ; preds = %48, %47
  br label %36

47:                                               ; preds = %41
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

48:                                               ; preds = %41
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
