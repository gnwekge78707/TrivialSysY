cp llvm_ir.txt main.ll
llvm-link main.ll lib.ll -S -o out.ll
lli out.ll < input.txt > myanswer.txt
