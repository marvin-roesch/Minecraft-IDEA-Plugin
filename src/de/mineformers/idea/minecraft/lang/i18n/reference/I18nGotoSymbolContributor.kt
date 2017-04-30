package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import de.mineformers.idea.minecraft.lang.i18n.I18nUtil
import java.util.*

/**
 * I18nGotoSymbolContributor

 * @author PaleoCrafter
 */
class I18nGotoSymbolContributor : ChooseByNameContributor {
    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        val properties = if (includeNonProjectItems) I18nUtil.findProperties(project) else I18nUtil.findProperties(
            project,
            I18nUtil.Scope.PROJECT)
        val names = ArrayList<String>(properties.size)
        for (property in properties) {
            if (property.key != null && property.key.length > 0) {
                names.add(property.key)
            }
        }
        return names.toArray<String>(arrayOfNulls<String>(names.size))
    }

    override fun getItemsByName(name: String, pattern: String, project: Project, includeNonProjectItems: Boolean): Array<NavigationItem> {
        val properties =
            if (includeNonProjectItems)
                I18nUtil.findProperties(project, key = name)
            else
                I18nUtil.findProperties(project, I18nUtil.Scope.PROJECT, name)
        return properties.map { it as NavigationItem }.toTypedArray()
    }
}
