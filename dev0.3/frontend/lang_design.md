## original syntax

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

## modified syntax
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
<MulExp>        := <UnaryExp> { ('*' | '/' | '%') <UnaryExp>}
<AddExp>        := <MulExp> { ('+' | '−') <MulExp>}
<RelExp>        := <AddExp> { ('<' | '>' | '<=' | '>=') <AddExp>}
<EqExp>         := <RelExp> { ('==' | '!=') <RelExp>}
<LAndExp>       := <EqExp> { '&&' <EqExp>}
<LOrExp>        := <LAndExp> { '||' <LAndExp>}
<ConstExp>      := <AddExp>
```
### DECL
```text
<BType>         := 'int'
// Const
<ConstDecl>     := 'const' <BType> <ConstDef> { ',' <ConstDef> } ';'
<ConstDef>      := Ident { '[' <ConstExp> ']' } '=' <ConstInitVal>
<ConstInitVal>  := <ConstExp> | '{' [ <ConstInitVal> { ',' <ConstInitVal> } ] '}'
// Var
<VarDecl>       := <BType> <VarDef> { ',' <VarDef> } ';'
<VarDef>        := Ident { '[' <ConstExp> ']' }  [ '=' <InitVal> ]
<InitVal>       := <Exp> | '{' [ <InitVal> { ',' <InitVal> } ] '}'
```
### STMT
```text
<Stmt> := <LVal> '=' <Exp> ';'
    | <Exp> ';'
    | ';'
    | <Block>
    | 'if' '(' <Cond> ')' <Stmt> [ 'else' <Stmt> ]
    | 'while' '(' <Cond> ')' <Stmt>
    | 'break' ';' | 'continue' ';'
    | 'return' [<Exp>] ';'
    | <LVal> '=' 'getint' '(' ')' ';'
    | 'printf' '(' FormatString { ',' <Exp> } ')' ';'

<Block> := '{' { ConstDecl | VarDecl | Stmt } '}'
```
### FUNC
```text
<FuncDef>       := <FuncType> Ident '(' [<FuncFParams> ] ')' <Block>
<MainFuncDef>   := 'int' 'main' '(' ')' <Block>
<FuncType>      := 'void' | 'int'
<FuncFParams>   := <FuncFParam> { ',' <FuncFParam> }
<FuncFParam>    := <BType> Ident [ '[' ']' { '[' <ConstExp> ']' } ]
CompUnit        := {ConstDecl | VarDecl} {FuncDef} MainFuncDef****
```