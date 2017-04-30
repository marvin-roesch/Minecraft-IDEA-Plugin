package de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers

import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.psi.PsiLiteralExpression
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.Translation

/**
 * LiteralTranslationIdentifier

 * @author PaleoCrafter
 */
class LiteralTranslationIdentifier : TranslationIdentifier<PsiLiteralExpression>() {
    override fun identify(element: PsiLiteralExpression): Translation? {
        val statement = element.parent
        if (element.value is String) {
            val result = IdentifierUtil.identify(element.project, element, statement, element)
            return result?.copy(key = result.key.replace(CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED, ""),
                                varKey = result.varKey.replace(CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED, ""))
        }
        return null
    }

    override fun elementClass(): Class<PsiLiteralExpression> = PsiLiteralExpression::class.java
}
