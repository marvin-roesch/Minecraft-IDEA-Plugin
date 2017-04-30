package de.mineformers.idea.minecraft.lang.i18n.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes
import java.util.*

/**
 * I18nBlock

 * @author PaleoCrafter
 */
class I18nBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks = ArrayList<Block>()
        var child: ASTNode? = myNode.firstChildNode
        var previousChild: ASTNode? = null
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE && (previousChild == null || previousChild.elementType !== I18nTypes.CRLF ||
                                                                child.elementType !== I18nTypes.CRLF)) {
                val block = I18nBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(),
                                      spacingBuilder)
                blocks.add(block)
            }
            previousChild = child
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
        //        return Spacing.createSpacing(0, 0, 0, true, 0);
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}