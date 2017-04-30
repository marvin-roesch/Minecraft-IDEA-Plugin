package de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers

import com.intellij.psi.PsiElement
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.Translation

/**
 * ITranslationIdentifier

 * @author PaleoCrafter
 */
abstract class TranslationIdentifier<T : PsiElement> {
    @SuppressWarnings("unchecked")
    fun identifyUnsafe(element: PsiElement): Translation? {
        return identify(element as T)
    }

    abstract fun identify(element: T): Translation?

    abstract fun elementClass(): Class<T>
}
