package de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiCall
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiSubstitutor
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.MethodSignature
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers.IdentifierUtil
import de.mineformers.idea.minecraft.util.PsiSearchUtil
import java.util.regex.Pattern

/**
 * TranslationFunction

 * @author PaleoCrafter
 */
class TranslationFunction(val className: String, val methodName: String, val parameterTypes: String,
                          val matchedIndex: Int, val formatting: Boolean, val setter: Boolean = false,
                          val foldParameters: Boolean = false, val prefix: String = "", val suffix: String = "") {
    fun matches(method: PsiMethod?, paramIndex: Int): Boolean {
        if (method == null)
            return false
        val scope = GlobalSearchScope.allScope(method.project)
        val psiClass = JavaPsiFacade.getInstance(method.project).findClass(className, scope) ?: return false
        val referenceMethod = psiClass.findMethodsByName(methodName, false)
            .first { convertSignatureToDescriptor(it.getSignature(PsiSubstitutor.EMPTY)) == parameterTypes };
        if (setter)
            return PsiSearchUtil.isMethodCalling(method, referenceMethod, paramIndex, matchedIndex)
        else
            return PsiSearchUtil.isMethodReturningResultOf(method, referenceMethod, paramIndex, matchedIndex)
    }

    fun convertSignatureToDescriptor(signature: MethodSignature): String {
        fun typeToDesc(type: String): String = when (type) {
            "byte"    -> "B"
            "char"    -> "C"
            "double"  -> "D"
            "float"   -> "F"
            "int"     -> "I"
            "long"    -> "J"
            "short"   -> "S"
            "boolean" -> "Z"
            else      ->
                if (type.endsWith("]")) {
                    val dimension = type.count { it == '[' }
                    "[".repeat(dimension) + typeToDesc(type.takeWhile { it != '[' })
                } else
                    "L$type;"
        }

        return "${signature.parameterTypes.map { typeToDesc(it.getCanonicalText(true)) }.joinToString("")}"
    }

    fun getCalls(call: PsiCall, paramIndex: Int): Iterable<PsiCall> {
        val scope = GlobalSearchScope.allScope(call.project)
        val psiClass = JavaPsiFacade.getInstance(call.project).findClass(className, scope) ?: return emptyList()
        val referenceMethod = psiClass.findMethodsByName(methodName, false)
            .first { convertSignatureToDescriptor(it.getSignature(PsiSubstitutor.EMPTY)) == parameterTypes };
        if (setter)
            return PsiSearchUtil.getCallsOf(call,
                                            referenceMethod,
                                            paramIndex,
                                            matchedIndex)
        else
            return PsiSearchUtil.getCallsReturningResultOf(call,
                                                           referenceMethod,
                                                           paramIndex,
                                                           matchedIndex)
    }

    fun getTranslationKey(call: PsiCall): Pair<Boolean, String>? {
        data class Step(val propagate: Boolean, val validReference: Boolean, val key: String) {
            val result = Pair(validReference, key)
        }

        fun resolveCall(depth: Int, single: Boolean, referenced: PsiMethod, call: PsiCall, acc: Step): Step? {
            if (acc.propagate)
                return acc
            val method = PsiSearchUtil.getReferencedMethod(call)
            val isReferencedMethod = referenced === method
            val param = call.argumentList?.expressions?.get(matchedIndex)
            if (method != null && param != null) {
                val result = PsiSearchUtil.evaluateExpression(param, null, IdentifierUtil.VALUE_VARIABLE) ?: return null
                if (!result.contains(IdentifierUtil.VALUE_VARIABLE) && (depth > 0 || isReferencedMethod))
                    return Step(true, single, result)
                return Step(false, true,
                            if (acc.key.contains(IdentifierUtil.VALUE_VARIABLE))
                                result.replace(IdentifierUtil.VALUE_VARIABLE, acc.key)
                            else
                                result)
            } else
                return null
        }

        val calls = getCalls(call, matchedIndex)
        val scope = GlobalSearchScope.allScope(call.project)
        val psiClass = JavaPsiFacade.getInstance(call.project).findClass(className, scope) ?: return null
        val referenced = psiClass.findMethodsByName(methodName, false)
            .first { convertSignatureToDescriptor(it.getSignature(PsiSubstitutor.EMPTY)) == parameterTypes };
        val result = calls.foldIndexed(Step(false, true, "") as Step?,
                                       {
                                           depth, acc, v ->
                                           if (acc == null)
                                               acc
                                           else
                                               resolveCall(depth, calls.count() == 1, referenced, v, acc)
                                       })?.result
        return result?.copy(second = prefix + result.second + suffix)
    }

    fun format(translation: String, call: PsiCall): String? {
        if (!formatting)
            return translation
        val format = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]").matcher(translation).replaceAll("%$1s")

        fun resolveCall(call: PsiCall, substitutions: Map<Int, Array<String?>?>): Map<Int, Array<String?>?> {
            val method = PsiSearchUtil.getReferencedMethod(call)
            val args = call.argumentList?.expressions
            if (method != null && args != null && args.size >= method.parameterList.parametersCount) {
                return method.parameterList.parameters
                    .mapIndexed {
                        i, parameter ->
                        if (parameter.isVarArgs) {
                            val varargType = method.getSignature(PsiSubstitutor.EMPTY).parameterTypes[i]
                            Pair(i, PsiSearchUtil.extractVarArgs(varargType, args.drop(i), substitutions, true))
                        } else {
                            Pair(i, PsiSearchUtil.substituteParameter(args[i], substitutions, true))
                        }
                    }.associate { it }
            } else
                return emptyMap()
        }

        val calls = getCalls(call, matchedIndex)
        if (calls.count() > 1) {
            val substitutions = calls
                .take(calls.count() - 1)
                .fold(emptyMap<Int, Array<String?>?>(), { acc, v -> resolveCall(v, acc) })
            val method = PsiSearchUtil.getReferencedMethod(calls.last()) ?: return translation
            val varargs = PsiSearchUtil.extractVarArgs(calls.last(), method.parameterList.parametersCount - 1,
                                                       substitutions, false)
            if (varargs.any { it == null })
                return null
            return String.format(format, *varargs)
        } else {
            val method = PsiSearchUtil.getReferencedMethod(calls.first()) ?: return translation
            val varargs = PsiSearchUtil.extractVarArgs(calls.first(), method.parameterList.parametersCount - 1,
                                                       emptyMap(), true)
            if (varargs.any { it == null })
                return null
            return String.format(format, *varargs)
        }
    }

    override fun toString(): String {
        return "$className.$methodName@$matchedIndex"
    }
}
