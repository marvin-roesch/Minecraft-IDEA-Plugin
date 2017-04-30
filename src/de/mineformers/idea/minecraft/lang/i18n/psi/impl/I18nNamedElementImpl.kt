package de.mineformers.idea.minecraft.lang.i18n.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nNamedElement

/**
 * I18nNamedElementImpl

 * @author PaleoCrafter
 */
abstract class I18nNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), I18nNamedElement
