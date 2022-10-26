# design checker

## PARSER

### notifier

- to modify exception of undefined token type
  - 错误处理中可能出现 undefinedTokenType， 需要错误处理+集中处理？
- pushUp pushBack 
  - pushUp可以出现在check中，一旦pushUp就确定了当前parse过程的正确
  - pushBack朝前看的回退操作
  - 需要选择时朝前看，朝前看则直接判断，否则用check函数
  - 可以改的优雅，统一朝前看操作，设计实现分离
    - checkToken前要确定step（即，清空tokenStack）（仅有lookForward时需要注意这个问题）
    - 所以，在checkToken下handleError时，不许在checkToken下reverse；但是，在lookForward下需要reverse


- LVAL or EXP
  - 左值是可以包含于表达式的，即表达式可以是一个左值，也可在左值后面接别的东西
  - 在STMTParser里面，需要决定LVAL EXP，LVAL分析到'='之u后，可以确定是否是EXP（EXP不会接=）。如何不回溯？
    - 法一：把LVAL当成EXP处理，如果得到的EXP只包含LVAL，则重新提取出LVAL
    - 法二：直接处理，先读出LVAL，再通过特殊的方法构造EXP（传入已经解析好的LVAL）
    - 法三：允许回溯
    - 应该考虑拓展性的选择

- error handle prev 
  - i j k型错误，前一个**非终结符**的行号
  - 由于已经checktoken不合法则自动reverse，因此这里不需要再reverse
  
### general design

- myParserDesign
  - **lookForward**朝前看基本步骤:
    - updateToken();
    - if lookForward 函数匹配
      - if 匹配的终结符在 当前文法 中是终结符
        - step
      - if 匹配的终结符在 当前文法 中属于某非终结符
        - reverse
    - else
      - reverse
  - **checkToken**相当于先updateToken，如果匹配则前进，否则reverse:
    - if !checkToken(...)
      - handleError 注意前一个非终结符的含义
  - 回溯基本步骤:
    - 确定回溯点 putCheckpoint()
      - 回溯点之前，一定要处于step过或reverse过的状态 in definite state
    - 正常parse操作
    - 决定点，不匹配则回溯 restoreCheckpoint()
      - better be in definite state

- parse: `[...2]...3`
  - lookForward 判断3
    - 否：解析2，吃掉3


## AST noter

- ast是保存简单的children还是保存详细的语义信息？
  - 如果是只保存`List<Node> children`, 则处理语义信息，buildIR的时候还需要parse

  
## MidEnd Design
- 是否表示为Op+AbstractVar的形式，还是直接采取中间变量temp_i的形式(ans：OP+AbstractVar的形式)
- checkError同时构建符号表：checkError需要用到context的情况 -> 一定是NODE的信息帮不到的情况。
  - EXPR：(LVal,UnaryExp)检查未定义名字，(FuncFParam)实参个数、()类型不匹配
    - context: 
  - DECL：(VarDef, ConstDef)检查名字重定义，
    - context：维护symbol
  - STMT：检查常量改变，void函数返回，printf，continue，break
  - FUNC：检查函数名重定义，参数名重定义，检查函数末尾是否存在返回
- errorType, errorToken declairXXXX
- error: 对于缺右括号、分号的情况：一定要检查，parser中是否出现以右括号、分号决定parsing的情况
  - 不能有！！！
  - 只能有检查右括号、分号来决定handleError
  - 对于检查handleError，一定要确保handleError完成后，需要register的register。以避免空值。