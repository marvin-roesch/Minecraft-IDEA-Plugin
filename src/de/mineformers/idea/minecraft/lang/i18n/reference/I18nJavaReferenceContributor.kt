package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.TranslationFinder

/**
 * I18nReferenceContributor

 * @author PaleoCrafter
 */
class I18nJavaReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        TranslationFinder.registerReferenceProviders(registrar)
    }
}
