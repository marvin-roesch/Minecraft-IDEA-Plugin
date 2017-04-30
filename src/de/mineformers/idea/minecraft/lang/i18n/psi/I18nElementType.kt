package de.mineformers.idea.minecraft.lang.i18n.psi

import com.intellij.psi.tree.IElementType
import de.mineformers.idea.minecraft.lang.i18n.I18nLanguage
import org.jetbrains.annotations.NonNls

/**
 * I18nElementType

 * @author PaleoCrafter
 */
class I18nElementType(@NonNls debugName: String) : IElementType(debugName, I18nLanguage.INSTANCE)