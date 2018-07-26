package br.ufal.ic.lexic;

public enum TokenCategory {
    main, id, tInt, tFloat, tBool, tChar, tString, scopeBeg, scopeEnd, paramBeg, paramEnd, arrayBeg, arrayEnd, lineEnd,
    commaSep, consNumInt, consNumFlo, consBool, consChar, consString, rwPrint, rwRead, rwIf, rwElif, rwElse, rwFor,
    rwWhile, rwReturn, opAssign, opLogic, opNot, opAditiv, opConc, opUnMinus, opMult, opRel, opEquals
}
