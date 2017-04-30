package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.IncorrectOperationException
import de.mineformers.idea.minecraft.icons.MCIcons
import de.mineformers.idea.minecraft.lang.i18n.I18nUtil
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers.IdentifierUtil
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import java.util.*

/**
 * I18nReference

 * @author PaleoCrafter
 */
class I18nReference(element: PsiElement,
                    textRange: TextRange,
                    private val useDefault: Boolean,
                    val key: String,
                    val varKey: String) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement.project
        val properties =
            if (useDefault)
                I18nUtil.findDefaultProperties(project, key = key)
            else
                I18nUtil.findProperties(project, key = key)
        val results = ArrayList<ResolveResult>()
        for (property in properties) {
            results.add(PsiElementResolveResult(property))
        }
        return results.toArray<ResolveResult>(arrayOfNulls<ResolveResult>(results.size))
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val project = myElement.project
        val properties = I18nUtil.findDefaultProperties(project)
        val variants = ArrayList<LookupElement>()
        val stringPattern =
            if (varKey.contains(IdentifierUtil.VALUE_VARIABLE))
                varKey.split(IdentifierUtil.VALUE_VARIABLE).map { Regex.escape(it) }.joinToString("(.*?)")
            else
                "(" + Regex.escape(varKey) + ".*?)"
        val pattern = Regex(stringPattern)
        for (property in properties) {
            if (property.key != null && property.key.length > 0) {
                val match = pattern.matchEntire(property.key)
                if (match != null) {
                    variants.add(
                        LookupElementBuilder
                            .create(if (match.groups.size <= 1) property.key else match.groupValues[1])
                            .withIcon(MCIcons.LANGUAGE_FILE)
                            .withTypeText(property.containingFile.name)
                            .withPresentableText(property.key))
                }
            }
        }
        return variants.toArray()
    }

    @Throws(IncorrectOperationException::class)
    override fun handleElementRename(newElementName: String): PsiElement {
        val stringPattern =
            if (varKey.contains(IdentifierUtil.VALUE_VARIABLE))
                varKey.split(IdentifierUtil.VALUE_VARIABLE).map { Regex.escape(it) }.joinToString("(.*?)")
            else
                "(" + Regex.escape(varKey) + ")"
        val pattern = Regex(stringPattern)
        val match = pattern.matchEntire(newElementName)
        return super.handleElementRename(
            if (match != null && match.groups.size > 1)
                match.groupValues[1]
            else
                newElementName
        )
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return element is I18nProperty && element.key == key
    }
}
