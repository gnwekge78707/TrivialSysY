# TrivialSysY Compiler

| An optimized compiler that translates SysY(a subset of C) to LLVM_IR&MIPS | Jiarui Wu | email：wujiarui@buaa.edu.cn |
| ------------------------------------------------------------ | --------- | --------------------------- |

------

[Document-CN](https://github.com/gnwekge78707/TrivialSysY/blob/master/TECH_REPORT.md) | [Document-EN](https://github.com/gnwekge78707/TrivialSysY/blob/master/TECH_REPORT.md)

This project implements a [Sysy language](https://gitlab.eduxiji.net/nscscc/docs/-/blob/master/SysY%E8%AF%AD%E8%A8%80%E5%AE%9A%E4%B9%89.pdf) optimized compilation system with LLVM_IR and MIPS dual backends. This compiler uses java as the programming language, and refers to excellent projects such as LLVM in terms of architecture. It supports part of the C language grammar (the specific grammar is Sysy2021, which supports all Sysy features including multidimensional arrays), and implements multiple optimization algorithm to boost performance. The design and implementation of this compiling system will be further described below.



## Architecture

```mermaid
graph TD
c1[[source code-源代码]]
c1==>a1
subgraph FrontEnd
a1([tokenizer-词法分析])
a1==>c2
c2[[token flow-词元流]]
c2==>a2
a2([parser-语法分析])
a2==>c3
c3[[ast-抽象语法树]]

end
a2==>c4

c4[[symbol table-符号表]]
c4==>a3
c3==>a3

subgraph MiddleEnd

subgraph LLVM_IR
a3([LLVM Builer-生成LLVM])
b1[Module]
b2[Value]
b3[User]
b4[basicBlock, Function, ...]
end
b1==>a5
a5==>b1
a5([pass-代码优化])
end

b1==>c5
c5[[LLVM Code-llvm目标码]]
c5==>a6
subgraph backEnd
a6([regAlloc-寄存器分配])==>
a7([pass-代码优化])

end
a7==>c6
c6[[Mips Code-mips目标码]]

style FrontEnd fill: #C6DFF8FF, stroke: #333, stroke-width: 4px;
style backEnd fill: #C6DFF8FF, stroke: #333, stroke-width: 4px;
style MiddleEnd  fill: #C6DFF8FF, stroke: #333, stroke-width: 4px;
style LLVM_IR fill: #71CED77E, stroke: #333, stroke-width: 4px;
c1:::aa
c2:::aa
c3:::aa
c4:::aa
c5:::aa
c6:::aa



classDef aa fill: #DAAEF8FF, stroke: #333, stroke-width: 4px;
classDef bb fill: #f9a, stroke: #333, stroke-width: 4px;
```

- It can be seen that this compiler mainly consists of frontend, middle-end and backend:
  - The front end preforms lexical analysis and syntax analysis, which can generate **Abstract Syntax Tree** representation from source files, and perform certain semantic analysis and error handling.
  - **The middle end is LLVM IR**, which is divided into abstract structures such as Value, Module, User, and Use according to the architecture of the llvm compiler. The front end and the middle end will collaborate to convert AST to LLVM IR. And some optimizations will be carried out in the mid-end, the most important ones include dominance analysis, alias analysis, Mem2Reg, global value label (equivalent to propagation merge + common subexpression deletion), phi node deletion, dead code deletion, branch optimization, active variable analysis, conflict graph construction, etc.
  - After the conflict graph is built, the backend will do register allocation. The backend outputs **Mips Assembly Code**, which manages a LLVMIR-to-MIPS generation template, mips isa instructions, and mips registers.



### System Diagram



```mermaid
graph TD

subgraph Driver

	d1[(Config-全局配置)]
	d2([Output-全局输出])
	d3([CompilerDriver-运行驱动])
end
d3==>f3
subgraph FrontEnd
	f1([error-错误处理])
	f2([symbol-符号表])
	f3([tokenize-词法分析])
	subgraph syntax
        s1([ast-抽象语法树])
        s2([parser-语法分析器])
	end
	s2-->s1
	f3-->s2
end
s2==>i5==>p1
subgraph MidEnd
	subgraph IR
        subgraph Values
            v1(instructions)
            v2(function)
            v3(basicblock)
            v2-->v3-->v1
        end
        i1(LLVMType-llvm类型系统)
        i2(Value)
        i3(User)
        i4(Use)
        i5(Module)
        i5-->i2-->i3
	end
	subgraph PASS
        p1([BBPredSuccAnalysis])-->
        p4([Mem2Reg])-->
        p2([...])-->
        p3([phiElimination])
	end
end
p3==>b1
subgraph BackEnd
	b1([MipsAssembly-mips寄存器管理])
	subgraph isa
        b2([MipsBranch])
        b4([...])
        b5([MipsRegReg])
	end
	subgraph template
        b6([mulTemplate])
        b7([...])
        b8([BrTemplate])
	end
	b1-->b6
	b1-->b7
	b1-->b8
	b6-->b2
	b8-->b5
end


style FrontEnd fill: #C6DFF8FF, stroke: #333, stroke-width: 4px;
style MidEnd fill: #f9a, stroke: #333, stroke-width: 4px;
style BackEnd fill: #C6DFF8FF, stroke: #333, stroke-width: 4px;
style Driver fill: #f9a, stroke: #333, stroke-width: 4px;
style syntax fill: #71CED77E, stroke: #333, stroke-width: 4px;
style IR fill: #C6DFF8FF, stroke: #333, stroke-width: 4px;
style Values fill: #71CED77E, stroke: #333, stroke-width: 4px;
style PASS fill: #DAAEF8FF, stroke: #333, stroke-width: 4px;
style template fill: #f9a, stroke: #333, stroke-width: 4px;
style isa fill: #f9a, stroke: #333, stroke-width: 4px;

classDef aa fill: #DAAEF8FF, stroke: #333, stroke-width: 4px;
classDef bb fill: #f9a, stroke: #333, stroke-width: 4px;
```



### File Structure

```scss
├─backend
│  ├─isa
│  └─template
├─driver
├─frontend
│  ├─error
│  ├─exceptions
│  ├─symbol
│  ├─syntax
│  │  ├─decl
│  │  │  ├─ast
│  │  │  └─parse
│  │  ├─expr
│  │  │  ├─ast
│  │  │  └─parse
│  │  ├─func
│  │  │  ├─ast
│  │  │  └─parse
│  │  └─stmt
│  │      ├─ast
│  │      └─parser
│  └─tokenize
├─midend
│  ├─ir
│  │  ├─type
│  │  └─value
│  │      └─instr
│  │          ├─binary
│  │          ├─mem
│  │          └─terminator
│  └─pass
└─util
```



## Supported Grammar (in BNF)

````markdown
## original syntax
CompUnit        := {ConstDecl | VarDecl} {FuncDef} MainFuncDef
### EXPR
```text
<Exp>           := <AddExp>
<Cond>          := <LOrExp>
<LVal>          := Ident { '[' <Exp> ']' } // 普通变量，一维或二维数组
<PrimaryExp>    := '(' <Exp> ')' | <LVal> | <Number> // 三种情况, 子表达式, 左值, 字面量
<Number>        := IntConst
<UnaryExp>      := <PrimaryExp> | <Ident> '(' [ <FuncRParams> ] ')' | <UnaryOp> <UnaryExp> // PrimaryExp 或者 FunctionCall 或者含有一元运算符
<UnaryOp>       := '+' | '-' | '!'  // '!' 仅能在条件表达式中出现
<FuncRParams>   := <Exp> { ',' <Exp> } 
<MulExp>        := <UnaryExp> | <MulExp> ( '*' | '/' | '%' ) <UnaryExp>
<AddExp>        := <MulExp> | <AddExp> ( '+' | '-' ) <MulExp>
<RelExp>        := <AddExp> | <RelExp> ( '<' | '>' | '<=' | '>=' ) <AddExp>
<EqExp>         := <RelExp> | <EqExp> ( '==' | '!=' ) <RelExp>
<LAndExp>       := <EqExp> | <LAndExp> '&&' <EqExp>
<LOrExp>        := <LAndExp> | <LOrExp> '||' <LAndExp>
```
### STMT 
```text
<Stmt> := <LVal> '=' <Exp> ';'
    | [<Exp>] ';'
    | <Block>
    | 'if' '(' <Cond> ')' <Stmt> [ 'else' <Stmt> ]
    | 'while' '(' <Cond> ')' <Stmt>
    | 'break' ';' | 'continue' ';'
    | 'return' [<Exp>] ';'
    | <LVal> '=' 'getint' '(' ')' ';'
    | 'printf' '(' FormatString { ',' <Exp> } ')' ';'

<BlockItem> := <Decl> | <Stmt>  // <Decl> 目前还未分析，相应代码暂时留空，下一节补上

<Block> := '{' { <BlockItem> } '}'
```
### DECL
```text
<Decl>          := <ConstDecl> | <VarDecl>
<BType>         := 'int'
// Const
<ConstDecl>     := 'const' <BType> <ConstDef> { ',' <ConstDef> } ';'
<ConstDef>      := Ident { '[' <ConstExp> ']' } '=' <ConstInitVal>
<ConstInitVal>  := <ConstExp> | '{' [ <ConstInitVal> { ',' <ConstInitVal> } ] '}'
// Var
<VarDecl>       := <BType> <VarDef> { ',' <VarDef> } ';'
<VarDef>        := Ident { '[' <ConstExp> ']' } | Ident { '[' <ConstExp> ']' } '=' <InitVal>
<InitVal>       := <Exp> | '{' [ <InitVal> { ',' <InitVal> } ] '}'
```
### FUNC
```text
<FuncDef>       := <FuncType> Ident '(' [<FuncFParams> ] ')' <Block>
<MainFuncDef>   := 'int' 'main' '(' ')' <Block>
<FuncType>      := 'void' | 'int'
<FuncFParams>   := <FuncFParam> { ',' <FuncFParam> }
<FuncFParam>    := <BType> Ident [ '[' ']' { '[' <ConstExp> ']' } ]
```
````

