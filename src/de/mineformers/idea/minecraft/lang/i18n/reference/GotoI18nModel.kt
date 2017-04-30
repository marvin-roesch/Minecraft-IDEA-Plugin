package de.mineformers.idea.minecraft.lang.i18n.reference

import com.google.common.collect.Lists
import com.intellij.ide.util.gotoByName.ContributorsBasedGotoByModel
import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.util.indexing.FindSymbolParameters
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import java.util.*

/**
 * GotoI18nModel

 * @author PaleoCrafter
 */
class GotoI18nModel(project: Project, val filter: Regex? = null) : ContributorsBasedGotoByModel(project,
                                                                                         arrayOf<ChooseByNameContributor>(
                                                                                             Extensions.findExtension(
                                                                                                 ChooseByNameContributor.SYMBOL_EP_NAME,
                                                                                                 I18nGotoSymbolContributor::class.java))) {

    override fun acceptItem(item: NavigationItem?): Boolean {
        return (item as I18nProperty).containingFile.virtualFile.nameWithoutExtension == "en_US"
    }

    override fun getElementsByName(name: String, parameters: FindSymbolParameters, canceled: ProgressIndicator): Array<Any> {
        val superResult = Lists.newArrayList(*super.getElementsByName(name, parameters, canceled))
        val result = TreeSet<Any> { o1, o2 -> (o1 as I18nProperty).key.compareTo((o2 as I18nProperty).key) }
        if (filter != null)
            result.addAll(superResult.filter { filter.matches((it as I18nProperty).key) })
        else
            result.addAll(superResult)
        return result.toArray()
    }

    override fun getPromptText(): String {
        return "Choose translation to use"
    }

    override fun getNotInMessage(): String {
        return "test"
    }

    override fun getNotFoundMessage(): String {
        return "test"
    }

    override fun getCheckBoxName(): String? {
        return "Include non-project translations"
    }

    override fun getCheckBoxMnemonic(): Char {
        return 0.toChar()
    }

    override fun loadInitialCheckBoxState(): Boolean {
        return false
    }

    override fun saveInitialCheckBoxState(state: Boolean) {

    }

    override fun getSeparators(): Array<String> {
        return emptyArray()
    }

    override fun getFullName(element: Any): String? {
        return element.toString()
    }

    override fun willOpenEditor(): Boolean {
        return false
    }
}
