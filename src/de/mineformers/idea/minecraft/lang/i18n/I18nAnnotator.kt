package de.mineformers.idea.minecraft.lang.i18n

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions.RemoveDuplicatesIntention
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions.RemoveNonMatchedPropertyIntention
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions.TrimKeyIntention
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty

/**
 * I18nAnnotator

 * @author PaleoCrafter
 */
class I18nAnnotator : Annotator {
    override fun annotate(psiElement: PsiElement, annotationHolder: AnnotationHolder) {
        if (psiElement is I18nProperty) {
            checkPropertyMatchesDefault(psiElement, annotationHolder)
            checkPropertyKey(psiElement, annotationHolder)
            checkPropertyDuplicates(psiElement, psiElement.parent.children, annotationHolder)
        }
    }

    private fun checkPropertyKey(property: I18nProperty, annotations: AnnotationHolder) {
        if (property.key.endsWith(" "))
            annotations.createWarningAnnotation(TextRange(property.textRange.startOffset,
                                                          property.textRange.startOffset + property.key.length),
                                                "Translation key ends with space").registerFix(TrimKeyIntention())
    }

    private fun checkPropertyDuplicates(property: I18nProperty, siblings: Array<PsiElement>, annotations: AnnotationHolder) {
        val count = siblings.count { it is I18nProperty && property.key == it.key }
        if (count > 1) {
            annotations.createWarningAnnotation(property,
                                                "Duplicate translation keys \"" + property.key + "\"").registerFix(
                    RemoveDuplicatesIntention(property))
        }
    }

    private fun checkPropertyMatchesDefault(property: I18nProperty, annotations: AnnotationHolder) {
        for (prop in I18nUtil.findDefaultProperties(property.project)) {
            if (prop.key == property.key)
                return
        }
        annotations.createWarningAnnotation(property.textRange,
                                            "Translation key not included in default localization file")
                .registerFix(RemoveNonMatchedPropertyIntention())
    }
}
