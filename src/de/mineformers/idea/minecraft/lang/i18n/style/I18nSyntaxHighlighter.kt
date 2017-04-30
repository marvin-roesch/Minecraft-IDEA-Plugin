package de.mineformers.idea.minecraft.lang.i18n.style

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import de.mineformers.idea.minecraft.lang.i18n.parser.I18nLexer
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes

import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey

/**
 * I18nSyntaxHighlighter

 * @author PaleoCrafter
 */
class I18nSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return I18nLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        if (tokenType == I18nTypes.SEPARATOR) {
            return SEPARATOR_KEYS
        } else if (tokenType == I18nTypes.KEY) {
            return KEY_KEYS
        } else if (tokenType == I18nTypes.VALUE) {
            return VALUE_KEYS
        } else if (tokenType == I18nTypes.COMMENT) {
            return COMMENT_KEYS
        } else if (tokenType == TokenType.BAD_CHARACTER) {
            return BAD_CHAR_KEYS
        } else {
            return EMPTY_KEYS
        }
    }

    companion object {
        val SEPARATOR = createTextAttributesKey("I18N_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val KEY = createTextAttributesKey("I18N_KEY", DefaultLanguageHighlighterColors.KEYWORD)
        val VALUE = createTextAttributesKey("I18N_VALUE", DefaultLanguageHighlighterColors.STRING)
        val COMMENT = createTextAttributesKey("I18N_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)

        internal val BAD_CHARACTER = createTextAttributesKey("I18N_BAD_CHARACTER",
                                                             DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE)

        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val SEPARATOR_KEYS = arrayOf(SEPARATOR)
        private val KEY_KEYS = arrayOf(KEY)
        private val VALUE_KEYS = arrayOf(VALUE)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}
