package de.mineformers.idea.minecraft.lang.i18n.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFile
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import java.util.*

/**
 * I18nStructureViewElement

 * @author PaleoCrafter
 */
class I18nStructureViewElement(private val element: PsiElement) : StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any {
        return element
    }

    override fun navigate(requestFocus: Boolean) {
        if (element is NavigationItem) {
            element.navigate(requestFocus)
        }
    }

    override fun canNavigate(): Boolean {
        return element is NavigationItem && element.canNavigate()
    }

    override fun canNavigateToSource(): Boolean {
        return element is NavigationItem && element.canNavigateToSource()
    }

    override fun getAlphaSortKey(): String {
        return (element as PsiNamedElement).name!!
    }

    override fun getPresentation(): ItemPresentation {
        return (element as NavigationItem).presentation!!
    }

    override fun getChildren(): Array<TreeElement> {
        if (element is I18nFile) {
            val properties = PsiTreeUtil.getChildrenOfType(element, I18nProperty::class.java)
            if (properties != null) {
                var treeElements = ArrayList<TreeElement>(properties.size)
                for (property in properties) {
                    treeElements.add(I18nStructureViewElement(property))
                }
                return treeElements.toTypedArray()
            }
            return emptyArray()
        } else {
            return emptyArray()
        }
    }
}