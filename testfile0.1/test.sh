#!/bin/bash

echo "start..."

for ((i=1; i<=5; i++))
do
  echo $i
    TEST="testfile$i"
    touch "$TEST.txt"
    touch "input$i.txt"
    echo "#include <stdio.h>" > "$TEST.c"
    echo "int getint() {int r; scanf(\"%d\",&r); return r;}" >> "$TEST.c"
    cat "$TEST.txt" >> "$TEST.c"
    gcc "$TEST.c" -o a.out
    ./a.out <"input$i.txt" >"output$i.txt"
    rm a.out
done

rm ./*.c

zip test.zip *.txt
