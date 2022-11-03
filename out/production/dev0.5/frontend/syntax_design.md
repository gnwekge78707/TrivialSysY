# SYNTAX to AST_NODE

CompUnit        := {ConstDecl | VarDecl} {FuncDef} MainFuncDef
### EXPR
```text
__________________________________________________________________________________
<Exp>           := <AddExp>
------------------> /
-----------------{| BinaryExpNode
__________________________________________________________________________________
<Cond>          := <LOrExp>
------------------> /
-----------------{| BinaryExpNode
__________________________________________________________________________________
<LVal>          := Ident { '[' <Exp> ']' } // 普通变量，一维或二维数组
------------------> LValNode
-----------------{| Token, Exp (AddExpNode)
__________________________________________________________________________________
<PrimaryExp>    := '(' <Exp> ')' | <LVal> | <Number> // 三种情况, 子表达式, 左值, 字面量
------------------> /
-----------------{| Exp (AddExpNode) | LValNode | NumberNode
__________________________________________________________________________________
<Number>        := IntConst
------------------> TokenNode
-----------------{| Token
__________________________________________________________________________________
<UnaryExp>      := <PrimaryExp> | <Ident> '(' [ <FuncRParams> ] ')' | <UnaryOp> <UnaryExp> // PrimaryExp 或者 FunctionCall 或者含有一元运算符
------------------> /
-----------------{| PrimaryExp (ExpNode | LValNode | NumberNode) 
------------------| FuncCallNode (Token, FuncRParamsNode) 
------------------| UnaryExpNode (Token, UnaryExp...)
__________________________________________________________________________________
<UnaryOp>       := '+' | '-' | '!'  // '!' 仅能在条件表达式中出现
------------------> TokenNode
__________________________________________________________________________________
<FuncRParams>   := <Exp> { ',' <Exp> } 
------------------> FuncRParamsNode
-----------------{| Exp (AddExpNode)
__________________________________________________________________________________
<MulExp>        := <UnaryExp> { ('*' | '/' | '%') <UnaryExp>}
------------------> BinaryExpNode
-----------------{| UnaryExp (PrimaryExp (AddExpNode | LValNode | NumberNode) | FuncCallNode | UnaryExpNode)
__________________________________________________________________________________
<AddExp>        := <MulExp> { ('+' | '−') <MulExp>}
------------------> BinaryExpNode
-----------------{| BinaryExpNode
<RelExp>        := <AddExp> { ('<' | '>' | '<=' | '>=') <AddExp>}

<EqExp>         := <RelExp> { ('==' | '!=') <RelExp>}

<LAndExp>       := <EqExp> { '&&' <EqExp>}

<LOrExp>        := <LAndExp> { '||' <LAndExp>}

<ConstExp>      := <AddExp>

```
### DECL
`<a> = <b> | <a>{<a>}` 's node can be like `<a> -> <a><b><a><b>...`
```text
<BType>         := 'int'
------------------> TokenNode
__________________________________________________________________________________
// Const
<ConstDecl>     := 'const' <BType> <ConstDef> { ',' <ConstDef> } ';'
------------------> varDeckNode
-----------------{| varDefNode
__________________________________________________________________________________
<ConstDef>      := Ident { '[' <ConstExp> ']' } '=' <ConstInitVal>
------------------> varDefNode
-----------------{| Token, {AddExpNode}, AddExpNode | InitValNode
__________________________________________________________________________________
<ConstInitVal>  := <ConstExp> | '{' [ <ConstInitVal> { ',' <ConstInitVal> } ] '}'
------------------> InitValNode
-----------------{| InitValNode
__________________________________________________________________________________
// Var
<VarDecl>       := <BType> <VarDef> { ',' <VarDef> } ';'
------------------> 
-----------------{| 
__________________________________________________________________________________
<VarDef>        := Ident { '[' <ConstExp> ']' }  [ '=' <InitVal> ]
------------------> 
-----------------{| 
__________________________________________________________________________________
<InitVal>       := <Exp> | '{' [ <InitVal> { ',' <InitVal> } ] '}'
------------------> 
-----------------{| 
```
### STMT
```text
__________________________________________________________________________________
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
__________________________________________
------------------> AssignNode
-----------------{| LValNode, Exp (AddExpNode)
__________________________________________
------------------> ConditionNode
-----------------{| Cond (BinaryExpNode), Stmt, Stmt
__________________________________________
------------------> LoopNode
-----------------{| Cond (BinaryExpNode), Stmt
__________________________________________
------------------> ContinueNode, BreakNode
-----------------{| /
__________________________________________
------------------> ReturnNode
-----------------{| Exp (AddExpNode)
__________________________________________
------------------> PrintNode
-----------------{| Token (FormatString), {Exp (AddExpNode)}
__________________________________________________________________________________
<Block> := '{' { ConstDecl | VarDecl | Stmt } '}'
------------------> BlockNode
-----------------{| VarDeclNode 
------------------| Stmt (BlockNode | AddExpNode | AssignNode | ConditionNode |
--------------------------LoopNode | ContinueNode | BreakNode | ReturnNode | PrintNode)
__________________________________________________________________________________
```
### FUNC
```text
<FuncDef>       := <FuncType> Ident '(' [<FuncFParams> ] ')' <Block>
------------------> FuncDefNode
-----------------{| BType (TokenNode), Token, [], BlockNode
__________________________________________________________________________________
<MainFuncDef>   := 'int' 'main' '(' ')' <Block>
------------------> /
-----------------{| BlockNode
__________________________________________________________________________________
<FuncType>      := 'void' | 'int'
------------------> TokenNode
-----------------{| 
__________________________________________________________________________________
<FuncFParams>   := <FuncFParam> { ',' <FuncFParam> }
------------------> FuncFParamsNode
-----------------{| FuncFParamNode
__________________________________________________________________________________
<FuncFParam>    := <BType> Ident [ '[' ']' { '[' <ConstExp> ']' } ]
------------------> FuncFParamNode
-----------------{| TokenNode, Token, {AddExpNode}
__________________________________________________________________________________
<CompUnit>      := {<ConstDecl> | <VarDecl>} {<FuncDef>} <MainFuncDef>
------------------> RootNode
-----------------{| {VarDefNode} {FuncDefNode} BlockNode
```

# AST_NOTE to NON_TERMINATOR