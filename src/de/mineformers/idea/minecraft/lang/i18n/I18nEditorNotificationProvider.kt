package de.mineformers.idea.minecraft.lang.i18n

import com.google.common.collect.Maps
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import com.intellij.util.ui.UIUtil
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nElementFactory
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import java.awt.Color

/**
 * I18nEditorNotificationProvider

 * @author PaleoCrafter
 */
class I18nEditorNotificationProvider(private val myProject: Project) : EditorNotifications.Provider<I18nEditorNotificationProvider.InfoPanel>() {
    private var show: Boolean = true

    override fun getKey(): Key<InfoPanel> {
        return KEY
    }

    override fun createNotificationPanel(file: VirtualFile, fileEditor: FileEditor): InfoPanel? {
        if (!show || !file.name.endsWith(".lang") || file.nameWithoutExtension == "en_US") {
            return null
        }

        val defaultProperties = I18nUtil.findDefaultProperties(myProject, scope = I18nUtil.Scope.PROJECT)
        val properties = I18nUtil.findProperties(myProject, file = file, scope = I18nUtil.Scope.PROJECT)
        val defaultKeys = defaultProperties.map { it?.key }.toMutableSet()
        val keys = properties.map { it?.key }.toMutableSet()
        val propertyMap = Maps.newHashMap<String, I18nProperty>()
        for (property in defaultProperties)
            propertyMap.put(property.key, property)

        if (!keys.containsAll(defaultKeys)) {
            val panel = InfoPanel()
            panel.setText("Translation file doesn't match default one (en_US.lang).")
            panel.createActionLabel("Add missing translations") {
                val psi = PsiManager.getInstance(myProject).findFile(file)
                object : WriteCommandAction.Simple<Unit>(myProject, psi) {
                    @Throws(Throwable::class)
                    override fun run() {
                        defaultKeys.removeAll(keys)
                        for (key in defaultKeys) {
                            if (key != null && propertyMap[key]?.value != null)
                                if (psi != null) {
                                    psi.add(I18nElementFactory.createCRLF(myProject))
                                    psi.add(I18nElementFactory.createProperty(myProject, key, propertyMap[key]?.value!!))
                                }
                        }
                        EditorNotifications.updateAll()
                    }
                }.execute()
            }
            panel.createActionLabel("Hide notification") {
                panel.isVisible = false
                show = false
            }
            return panel
        }
        return null
    }

    class InfoPanel : EditorNotificationPanel() {
        override fun getBackground(): Color {
            val color = EditorColorsManager.getInstance().globalScheme.getColor(EditorColors.NOTIFICATION_BACKGROUND)
            return color ?: UIUtil.getPanelBackground()
        }
    }

    companion object {
        private val KEY = Key.create<InfoPanel>("minecraft.editors.translations")
    }
}