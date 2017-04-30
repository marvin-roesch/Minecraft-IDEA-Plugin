package de.mineformers.idea.minecraft.lang.i18n.style

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import de.mineformers.idea.minecraft.icons.MCIcons

import javax.swing.*

/**
 * I18nColorSettingsPage

 * @author PaleoCrafter
 */
class I18nColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon? {
        return MCIcons.LANGUAGE_FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return I18nSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return "#PARSE_ESCAPES\n" +
               "tile.stone.name=Stone\n" +
               "item.apple.name=Apple"
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? {
        return null
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Minecraft localization"
    }

    companion object {
        private val DESCRIPTORS = arrayOf(AttributesDescriptor("Comment", I18nSyntaxHighlighter.COMMENT),
                                          AttributesDescriptor("Key", I18nSyntaxHighlighter.KEY),
                                          AttributesDescriptor("Separator", I18nSyntaxHighlighter.SEPARATOR),
                                          AttributesDescriptor("Value", I18nSyntaxHighlighter.VALUE))
    }
}