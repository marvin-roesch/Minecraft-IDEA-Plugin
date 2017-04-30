package de.mineformers.idea.minecraft.lang.i18n.psi

import com.intellij.psi.tree.IElementType
import de.mineformers.idea.minecraft.lang.i18n.I18nLanguage
import org.jetbrains.annotations.NonNls

/**
 * I18nElementType

 * @author PaleoCrafter
 */
class I18nTokenType(@NonNls debugName: String) : IElementType(debugName, I18nLanguage.INSTANCE) {
    override fun toString(): String {
        if (this === I18nTypes.SEPARATOR) {
            return "="
        }
        if (this === I18nTypes.KEY) {
            return "translation key"
        }
        if (this === I18nTypes.COMMENT) {
            return "comment"
        }
        if (this === I18nTypes.CRLF) {
            return "end of line"
        }
        return super.toString()
    }
}