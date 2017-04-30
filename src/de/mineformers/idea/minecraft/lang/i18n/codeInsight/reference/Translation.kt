package de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference

import com.intellij.psi.PsiElement
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers.IdentifierUtil

/**
 * TransitionElement

 * @author PaleoCrafter
 */
data class Translation(val foldingElement: PsiElement?,
                       val referenceElement: PsiElement?,
                       val key: String,
                       val varKey: String,
                       val text: String?,
                       val formattingError: Boolean = false,
                       val containsVariable: Boolean = false) {
    val regexPattern = Regex(varKey.split(IdentifierUtil.VALUE_VARIABLE).map { Regex.escape(it) }.joinToString("(.*?)"))
}
