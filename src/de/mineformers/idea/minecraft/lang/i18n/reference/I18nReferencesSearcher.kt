package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.find.FindModel
import com.intellij.find.impl.FindInProjectUtil
import com.intellij.psi.PsiReference
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.usages.FindUsagesProcessPresentation
import com.intellij.usages.UsageViewPresentation
import com.intellij.util.Processor
import com.intellij.util.QueryExecutor
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty

/**
 * ${JDOC}
 */
fun <T> List<T>.append(elem: T): List<T> {
    val copy = this.toMutableList()
    copy.add(elem)
    return copy.toList()
}

fun <T> Set<T>.append(elem: T): Set<T> {
    val copy = this.toMutableSet()
    copy.add(elem)
    return copy.toSet()
}

fun <T> List<T>.append(elems: Iterable<T>): List<T> {
    val copy = this.toMutableList()
    elems.forEach { copy.add(it) }
    return copy.toList()
}

fun <T> Set<T>.append(elems: Iterable<T>): Set<T> {
    val copy = this.toMutableSet()
    elems.forEach { copy.add(it) }
    return copy.toSet()
}

class I18nReferencesSearcher : QueryExecutor<PsiReference, ReferencesSearch.SearchParameters> {
    override fun execute(parameters: ReferencesSearch.SearchParameters, consumer: Processor<PsiReference>): Boolean {
        val property = parameters.elementToSearch
        if (property is I18nProperty) {
            fun <A> power(s: List<A>): Set<List<A>> {
                tailrec fun pwr(s: List<A>, acc: Set<List<A>>): Set<List<A>> =
                    if (s.isEmpty())
                        acc
                    else
                        pwr(s.takeLast(s.size - 1),
                            acc.append(acc.map { it.append(s.first()) }))
                return pwr(s, setOf(emptyList()))
            }

            val model = FindModel()
            model.customScope = parameters.effectiveSearchScope
            model.isCaseSensitive = true
            model.searchContext = FindModel.SearchContext.IN_STRING_LITERALS
            model.isRegularExpressions = true
            model.stringToFind = power(property.key.split("."))
                .filter { it.isNotEmpty() }
                .map { Regex.escape(it.joinToString(".")) }
                .joinToString("|")
            FindInProjectUtil.findUsages(model, parameters.project,
                                         {
                                             if (it.file != null && it.element != null && it.rangeInElement != null) {
                                                 val highlighted = it.file!!.findElementAt(it.rangeInElement!!.startOffset)
                                                 val ref = highlighted?.parent?.references?.find { it is I18nReference } as I18nReference?
                                                 if (ref != null && ref.key == property.key)
                                                     consumer.process(ref)
                                             }
                                             true
                                         },
                                         FindUsagesProcessPresentation(UsageViewPresentation()))
        }
        return true
    }
}