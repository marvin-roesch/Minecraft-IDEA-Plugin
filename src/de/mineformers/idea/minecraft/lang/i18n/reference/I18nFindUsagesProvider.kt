package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import de.mineformers.idea.minecraft.lang.i18n.parser.I18nLexer
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes

/**
 * I18nFindUsagesProvider

 * @author PaleoCrafter
 */
class I18nFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner? = WORDS_SCANNER

    override fun canFindUsagesFor(psiElement: PsiElement) = psiElement is PsiNamedElement

    override fun getHelpId(psiElement: PsiElement) = null

    override fun getType(element: PsiElement) =
        if (element is I18nProperty)
            "translation"
        else
            ""

    override fun getDescriptiveName(element: PsiElement) =
        if (element is I18nProperty)
            element.key
        else
            ""

    override fun getNodeText(element: PsiElement, useFullName: Boolean) =
        if (element is I18nProperty)
            element.key + "=" + element.value
        else
            ""

    companion object {
        private val WORDS_SCANNER = {
            val s = DefaultWordsScanner(I18nLexer(),
                                        TokenSet.create(I18nTypes.KEY),
                                        TokenSet.create(I18nTypes.COMMENT),
                                        TokenSet.EMPTY)
            s.setMayHaveFileRefsInLiterals(true)
            s
        }()
    }
}