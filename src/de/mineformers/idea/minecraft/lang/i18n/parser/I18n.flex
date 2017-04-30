package de.mineformers.idea.minecraft.lang.i18n.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes;
import com.intellij.psi.TokenType;

%%

%class _I18nLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF= \n|\r|\r\n
WHITE_SPACE=[\ \t\f]
FIRST_VALUE_CHARACTER=[^ \n\r\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
SEPARATOR=[=]
KEY_CHARACTER=[^=\n\r\t\f\\] | "\\"{CRLF}

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return I18nTypes.COMMENT; }

<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return I18nTypes.KEY; }

<YYINITIAL> {SEPARATOR}                                     { yybegin(WAITING_VALUE); return I18nTypes.SEPARATOR; }

<WAITING_VALUE> {CRLF}                                     { yybegin(YYINITIAL); return I18nTypes.CRLF; }

<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return I18nTypes.VALUE; }

{CRLF}                                                     { yybegin(YYINITIAL); return I18nTypes.CRLF; }

{WHITE_SPACE}+                                              { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

.                                                           { return TokenType.BAD_CHARACTER; }