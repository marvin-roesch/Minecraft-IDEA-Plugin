package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes

/**
 * I18nReferenceProvider

 * @author PaleoCrafter
 */
class I18nReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withChild(PlatformPatterns.psiElement().withElementType(I18nTypes.KEY)),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val property = element as I18nProperty
                    return arrayOf(I18nReference(element,
                                                 TextRange(0, property.key.length),
                                                 true,
                                                 property.key,
                                                 property.key))
                }
            })
    }
}
