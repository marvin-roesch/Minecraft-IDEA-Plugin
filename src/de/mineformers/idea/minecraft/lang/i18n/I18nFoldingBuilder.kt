package de.mineformers.idea.minecraft.lang.i18n

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.TranslationFinder

/**
 * I18nFoldingBuilder

 * @author PaleoCrafter
 */
class I18nFoldingBuilder : FoldingBuilderEx() {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean) = TranslationFinder.fold(root)

    override fun getPlaceholderText(node: ASTNode) = "..."

    override fun isCollapsedByDefault(node: ASTNode) = true
}