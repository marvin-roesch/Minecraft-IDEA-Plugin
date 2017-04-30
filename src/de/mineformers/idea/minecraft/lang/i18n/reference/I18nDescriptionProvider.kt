package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.psi.ElementDescriptionLocation
import com.intellij.psi.ElementDescriptionProvider
import com.intellij.psi.PsiElement
import com.intellij.usageView.UsageViewTypeLocation
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty

/**
 * I18nDescriptionProvider

 * @author PaleoCrafter
 */
class I18nDescriptionProvider : ElementDescriptionProvider {
    override fun getElementDescription(element: PsiElement, location: ElementDescriptionLocation): String? {
        if (element is I18nProperty) {
            if (location is UsageViewTypeLocation)
                return "translation"
            return element.key
        }
        return null
    }
}
