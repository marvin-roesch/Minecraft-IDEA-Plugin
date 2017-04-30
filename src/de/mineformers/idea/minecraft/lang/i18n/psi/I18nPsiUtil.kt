package de.mineformers.idea.minecraft.lang.i18n.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import de.mineformers.idea.minecraft.icons.MCIcons
import javax.swing.Icon

/**
 * I18nPsiUtil

 * @author PaleoCrafter
 */
object I18nPsiUtil {
    fun getKey(element: I18nProperty): String? {
        val keyNode = element.node.findChildByType(I18nTypes.KEY)
        if (keyNode != null) {
            return keyNode.text
        } else {
            return null
        }
    }

    fun getValue(element: I18nProperty): String? {
        val valueNode = element.node.findChildByType(I18nTypes.VALUE)
        if (valueNode != null) {
            return valueNode.text
        } else {
            return null
        }
    }


    fun getName(element: I18nProperty): String? {
        return getKey(element)
    }

    fun setName(element: I18nProperty, newName: String): PsiElement {
        val keyNode = element.node.findChildByType(I18nTypes.KEY)
        if (keyNode != null) {

            val property = I18nElementFactory.createProperty(element.project, newName)
            val newKeyNode = property.firstChild.node
            element.node.replaceChild(keyNode, newKeyNode)
        }
        return element
    }

    fun getNameIdentifier(element: I18nProperty): PsiElement? {
        val keyNode = element.node.findChildByType(I18nTypes.KEY)
        if (keyNode != null) {
            return keyNode.psi
        } else {
            return null
        }
    }

    fun getPresentation(element: I18nProperty): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return element.key
            }

            override fun getLocationString(): String? {
                return element.containingFile.name
            }

            override fun getIcon(unused: Boolean): Icon? {
                return MCIcons.LANGUAGE_FILE
            }
        }
    }

    fun toString(element: I18nProperty): String {
        return "I18nProperty(" + element.key + "=" + element.value + ")"
    }

}