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
  - 
- control flow of parser