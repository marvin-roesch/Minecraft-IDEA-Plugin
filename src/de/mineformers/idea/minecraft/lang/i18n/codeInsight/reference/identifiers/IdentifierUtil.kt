package de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import de.mineformers.idea.minecraft.lang.i18n.I18nUtil
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.Translation
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.TranslationFinder
import de.mineformers.idea.minecraft.util.PsiSearchUtil
import java.util.*

/**
 * IdentifierUtil

 * @author PaleoCrafter
 */
object IdentifierUtil {
    final val VALUE_VARIABLE = "\$IDEA_TRANSLATION_VALUE"

    fun identify(project: Project, element: PsiExpression, container: PsiElement, referenceElement: PsiElement): Translation? {
        if (container is PsiPolyadicExpression)
            return identify(project, container, container.parent, referenceElement);
        if (container is PsiExpressionList && container.parent is PsiCallExpression) {
            val call = container.parent as PsiCallExpression
            val index = container.expressions.indexOf(element)
            val value = PsiSearchUtil.evaluateExpression(element, "", VALUE_VARIABLE) ?: ""

            val method = PsiSearchUtil.getReferencedMethod(call)
            for (function in TranslationFinder.translationFunctions) {
                if (function.matches(method, index)) {
                    val result = function.getTranslationKey(call) ?: continue
                    val translationKey = result.second.trim()
                    val fullKey = translationKey.replace(VALUE_VARIABLE, value)
                    val properties = I18nUtil.findDefaultProperties(project,
                                                                    key = fullKey)
                    val translation =
                        if (properties.size > 0)
                            properties[0].value
                        else
                            null
                    if (translation == null && function.setter)
                        return null
                    if (translation != null)
                        try {
                            return Translation(if (function.foldParameters) container else call,
                                               if (result.first) referenceElement else null,
                                               fullKey,
                                               translationKey,
                                               function.format(translation, call) ?: translation,
                                               containsVariable = fullKey.contains(VALUE_VARIABLE))
                        } catch (ignored: MissingFormatArgumentException) {
                            return Translation(if (function.foldParameters) container else call,
                                               if (result.first) referenceElement else null,
                                               fullKey,
                                               translation,
                                               translationKey,
                                               true,
                                               containsVariable = fullKey.contains(VALUE_VARIABLE))
                        }
                    else
                        return Translation(null,
                                           if (result.first) referenceElement else null,
                                           fullKey,
                                           translationKey,
                                           null,
                                           containsVariable = fullKey.contains(VALUE_VARIABLE))
                }
            }
            return null;
        }
        return null
    }
}
