package de.mineformers.idea.minecraft.util

import com.intellij.psi.*
import com.intellij.psi.util.PsiUtil

/**
 * PsiSearchUtil

 * @author PaleoCrafter
 */
object PsiSearchUtil {
    fun getObjectClass(expression: PsiExpression): PsiClass? {
        val resolveResult = (expression as PsiReferenceExpression).advancedResolve(false).element
        if (resolveResult is PsiClass)
            return resolveResult
        else if (resolveResult is PsiMethod)
            return resolveResult.containingClass
        return null
    }

    fun getReferencedMethod(call: PsiCall): PsiMethod? =
        when (call) {
            is PsiMethodCallExpression -> call.methodExpression.advancedResolve(false).element as PsiMethod?
            is PsiNewExpression        -> call.resolveMethod()
            else                       -> null
        }

    fun evaluateExpression(expression: PsiAnnotationMemberValue, defaultValue: String?, parameterReplacement: String?): String? {
        if (expression is PsiTypeCastExpression && expression.operand != null)
            return evaluateExpression(expression.operand!!, defaultValue, parameterReplacement)
        if (expression is PsiReferenceExpression) {
            val reference = expression.advancedResolve(false).element
            if (reference is PsiParameter)
                return parameterReplacement
            if (reference is PsiVariable && reference.initializer != null) {
                return evaluateExpression(reference.initializer!!, null, parameterReplacement)
            }
        } else if (expression is PsiLiteral) {
            return expression.value.toString()
        } else if (expression is PsiPolyadicExpression) {
            var value = ""
            for (operand in expression.operands) {
                val operandResult = evaluateExpression(operand, defaultValue,
                                                       parameterReplacement) ?: return defaultValue
                when (expression.operationTokenType) {
                    JavaTokenType.PLUS -> value += operandResult
                }
            }
            return value
        }
        return defaultValue
    }

    fun isMethodCalling(method: PsiMethod?, reference: PsiMethod?, paramIndex: Int, referenceParamIndex: Int): Boolean {
        if (method === reference && paramIndex == referenceParamIndex)
            return true
        if (method == null || reference == null || paramIndex == -1)
            return false
        fun findFirstMethodCall(elem: PsiElement): PsiMethodCallExpression? =
            if (elem is PsiMethodCallExpression)
                elem
            else
                elem.children.map { findFirstMethodCall(it) }.find { it != null }

        val value = findFirstMethodCall(method)
        if (value is PsiMethodCallExpression) {
            if (getReferencedMethod(value) === reference) {
                val param = value.argumentList.expressions[referenceParamIndex]
                if (param is PsiReferenceExpression) {
                    val ref = param.advancedResolve(false).element
                    if (ref === method.parameterList.parameters[referenceParamIndex])
                        return true
                } else if (param is PsiPolyadicExpression) {
                    for (operand in param.operands)
                        if (operand is PsiReferenceExpression) {
                            val ref = operand.advancedResolve(false).element
                            if (ref === method.parameterList.parameters[paramIndex])
                                return true
                        }
                }
            } else {
                val ref = getReferencedMethod(value)
                if (ref != null)
                    return isMethodCalling(ref, reference, paramIndex, referenceParamIndex)
            }
        }
        return false
    }

    fun isMethodReturningResultOf(method: PsiMethod?, reference: PsiMethod?, paramIndex: Int, referenceParamIndex: Int): Boolean {
        if (method === reference && paramIndex == referenceParamIndex)
            return true
        if (method == null || reference == null || paramIndex == -1)
            return false
        if (method.isConstructor || reference.isConstructor)
            return isMethodConstructingType(method, reference, paramIndex, referenceParamIndex)
        if (method.returnType == null || reference.returnType == null)
            return false
        if (!method.returnType!!.isAssignableFrom(reference.returnType!!))
            return false
        for (returnStatement in PsiUtil.findReturnStatements(method)) {
            val value = returnStatement.returnValue
            if (value is PsiMethodCallExpression) {
                if (getReferencedMethod(value) === reference) {
                    val param = value.argumentList.expressions[referenceParamIndex]
                    if (param is PsiReferenceExpression) {
                        val ref = param.advancedResolve(false).element
                        return ref === method.parameterList.parameters[paramIndex]
                    } else if (param is PsiPolyadicExpression) {
                        for (operand in param.operands)
                            if (operand is PsiReferenceExpression) {
                                val ref = operand.advancedResolve(false).element
                                return ref === method.parameterList.parameters[paramIndex]
                            }
                    } else {
                        if (param === value.argumentList.expressions[paramIndex])
                            return true
                    }
                } else {
                    val ref = getReferencedMethod(value)
                    if (ref != null)
                        return isMethodReturningResultOf(ref, reference, paramIndex, referenceParamIndex)
                }
            }
        }
        return false
    }

    fun isMethodConstructingType(method: PsiMethod?, reference: PsiMethod?, paramIndex: Int, referenceParamIndex: Int): Boolean {
        if (method === reference && paramIndex == referenceParamIndex)
            return true
        if (method == null || reference == null || paramIndex == -1)
            return false
        if (!method.isConstructor || !reference.isConstructor)
            return false
        fun findFirstMethodCall(elem: PsiElement): PsiMethodCallExpression? =
            if (elem is PsiMethodCallExpression &&
                (elem.methodExpression.text == "super" || elem.methodExpression.text == "this"))
                elem
            else
                elem.children.map { findFirstMethodCall(it) }.find { it != null }

        val value = findFirstMethodCall(method)
        if (value is PsiMethodCallExpression) {
            if (getReferencedMethod(value) === reference) {
                val param = value.argumentList.expressions[referenceParamIndex]
                if (param is PsiReferenceExpression) {
                    val ref = param.advancedResolve(false).element
                    if (ref === method.parameterList.parameters[referenceParamIndex])
                        return true
                } else if (param is PsiPolyadicExpression) {
                    for (operand in param.operands)
                        if (operand is PsiReferenceExpression) {
                            val ref = operand.advancedResolve(false).element
                            if (ref === method.parameterList.parameters[paramIndex])
                                return true
                        }
                }
            } else {
                val ref = getReferencedMethod(value)
                if (ref != null)
                    return isMethodConstructingType(ref, reference, paramIndex, referenceParamIndex)
            }
        }
        return false
    }

    fun getSupersOf(call: PsiCall, reference: PsiMethod, paramIndex: Int, referenceParamIndex: Int): Iterable<PsiCall> {
        val method = call.resolveMethod() ?: return emptyList()
        if (!method.isConstructor)
            return emptyList()
        if (method === reference)
            return listOf(call)

        fun findFirstMethodCall(elem: PsiElement): PsiMethodCallExpression? =
            if (elem is PsiMethodCallExpression &&
                (elem.methodExpression.text == "super" || elem.methodExpression.text == "this"))
                elem
            else
                elem.children.map { findFirstMethodCall(it) }.find { it != null }

        val value = findFirstMethodCall(method)
        if (value is PsiMethodCallExpression) {
            if (getReferencedMethod(value) === reference) {
                val param = value.argumentList.expressions[referenceParamIndex]
                if (param is PsiReferenceExpression) {
                    val ref = param.advancedResolve(false).element
                    if (ref === method.parameterList.parameters[referenceParamIndex])
                        return listOf(call, value)
                } else if (param is PsiPolyadicExpression) {
                    for (operand in param.operands)
                        if (operand is PsiReferenceExpression) {
                            val ref = operand.advancedResolve(false).element
                            if (ref === method.parameterList.parameters[paramIndex])
                                return listOf(call, value)
                        }
                }
            } else {
                val result = mutableListOf(call)
                result.addAll(getSupersOf(value, reference, paramIndex, referenceParamIndex))
                return result
            }
        }
        return emptyList()
    }

    fun getCallsOf(call: PsiCall, reference: PsiMethod, paramIndex: Int, referenceParamIndex: Int): Iterable<PsiCall> {
        val method = call.resolveMethod() ?: return emptyList()
        if (method === reference)
            return listOf(call)

        fun findFirstMethodCall(elem: PsiElement): PsiMethodCallExpression? =
            if (elem is PsiMethodCallExpression)
                elem
            else
                elem.children.map { findFirstMethodCall(it) }.find { it != null }

        val value = findFirstMethodCall(method)
        if (value is PsiMethodCallExpression) {
            if (getReferencedMethod(value) === reference) {
                val param = value.argumentList.expressions[referenceParamIndex]
                if (param is PsiReferenceExpression) {
                    val ref = param.advancedResolve(false).element
                    if (ref === method.parameterList.parameters[referenceParamIndex])
                        return listOf(call, value)
                } else if (param is PsiPolyadicExpression) {
                    for (operand in param.operands)
                        if (operand is PsiReferenceExpression) {
                            val ref = operand.advancedResolve(false).element
                            if (ref === method.parameterList.parameters[paramIndex])
                                return listOf(call, value)
                        }
                }
            } else {
                val result = mutableListOf(call)
                result.addAll(getCallsOf(value, reference, paramIndex, referenceParamIndex))
                return result
            }
        }
        return emptyList()
    }

    fun getCallsReturningResultOf(call: PsiCall, reference: PsiMethod, paramIndex: Int, referenceParamIndex: Int): Iterable<PsiCall> {
        val method = getReferencedMethod(call) ?: return emptyList()
        if (method.isConstructor)
            return getSupersOf(call, reference, paramIndex, referenceParamIndex)
        if (!method.returnType!!.isAssignableFrom(reference.returnType!!))
            return emptyList()
        if (method === reference)
            return listOf(call)
        for (returnStatement in PsiUtil.findReturnStatements(method)) {
            val value = returnStatement.returnValue
            if (value is PsiMethodCallExpression) {
                if (getReferencedMethod(value) === reference) {
                    val param = value.argumentList.expressions[referenceParamIndex]
                    if (param is PsiReferenceExpression) {
                        val ref = param.advancedResolve(false).element
                        if (ref === method.parameterList.parameters[referenceParamIndex])
                            return listOf(call, value)
                    } else if (param is PsiPolyadicExpression) {
                        for (operand in param.operands)
                            if (operand is PsiReferenceExpression) {
                                val ref = operand.advancedResolve(false).element
                                if (ref === method.parameterList.parameters[paramIndex])
                                    return listOf(call, value)
                            }
                    } else {
                        if (evaluateExpression(param, null, null) != null)
                            return listOf(call, value)
                    }
                } else {
                    val result = mutableListOf(call)
                    result.addAll(getCallsReturningResultOf(value, reference, paramIndex, referenceParamIndex))
                    return result
                }
            }
        }
        return emptyList()
    }

    fun substituteParameter(expression: PsiExpression, substitutions: Map<Int, Array<String?>?>, allowReferences: Boolean): Array<String?>? {
        if (expression is PsiTypeCastExpression && expression.operand != null)
            return substituteParameter(expression.operand!!, substitutions, allowReferences)
        if (expression is PsiReferenceExpression) {
            val reference = expression.advancedResolve(false).element
            if (reference is PsiParameter && reference.parent is PsiParameterList) {
                val paramIndex = (reference.parent as PsiParameterList).getParameterIndex(reference)
                if (substitutions.containsKey(paramIndex))
                    return substitutions[paramIndex]
            }
            if (reference is PsiVariable && reference.initializer != null) {
                return substituteParameter(reference.initializer!!, substitutions, allowReferences)
            }
        } else if (expression is PsiLiteral) {
            return arrayOf(expression.value.toString())
        } else if (expression is PsiPolyadicExpression) {
            var value = ""
            for (operand in expression.operands) {
                val operandResult = evaluateExpression(operand, null, null) ?: return null
                when (expression.operationTokenType) {
                    JavaTokenType.PLUS -> value += operandResult
                }
            }
            return arrayOf(value)
        }
        if (allowReferences)
            return arrayOf("\${${expression.text}}")
        else
            return null
    }

    fun extractVarArgs(call: PsiCall, index: Int, substitutions: Map<Int, Array<String?>?>, allowReferences: Boolean): Array<String?> {
        val method = getReferencedMethod(call)
        val args = call.argumentList?.expressions ?: return emptyArray()
        if (method == null || args.size < (index + 1))
            return emptyArray()
        if (!method.parameterList.parameters[index].isVarArgs) {
            return arrayOf(evaluateExpression(args[index], null, null))
        }

        val varargType = method.getSignature(PsiSubstitutor.EMPTY).parameterTypes[index]
        val elements = args.drop(index)
        return extractVarArgs(varargType, elements, substitutions, allowReferences)
    }

    fun extractVarArgs(type: PsiType, elements: List<PsiExpression>, substitutions: Map<Int, Array<String?>?>, allowReferences: Boolean): Array<String?> {
        fun convertExpression(expression: PsiExpression): Array<String?>? =
            substituteParameter(expression, substitutions, allowReferences)

        fun resolveReference(expression: PsiExpression): Array<String?> {
            if (expression is PsiTypeCastExpression && expression.operand != null)
                return resolveReference(expression.operand!!)
            else if (expression is PsiReferenceExpression) {
                val reference = expression.advancedResolve(false).element
                if (reference is PsiParameter && reference.parent is PsiParameterList) {
                    val paramIndex = (reference.parent as PsiParameterList).getParameterIndex(reference)
                    if (substitutions.containsKey(paramIndex))
                        return substitutions[paramIndex] ?: arrayOf(null as String?)
                }
                return arrayOf(evaluateExpression(expression, null, null))
            }
            return arrayOf(evaluateExpression(expression, null, null))
        }

        if (elements[0].type == type) {
            // We're dealing with an array initialiser, let's analyse it!
            val initialiser = elements[0]
            if (initialiser is PsiNewExpression && initialiser.arrayInitializer != null)
                return initialiser.arrayInitializer!!.initializers
                    .flatMap { convertExpression(it)?.toList() ?: listOf<String?>(null) }
                    .toTypedArray()
            else
                return resolveReference(initialiser)
        } else
            return elements
                .flatMap { convertExpression(it)?.toList() ?: listOf<String?>(null) }
                .toTypedArray()
    }
}
