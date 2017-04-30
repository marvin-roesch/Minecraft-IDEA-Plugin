// This is a generated file. Not intended for manual editing.
package de.mineformers.idea.minecraft.lang.i18n.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes.*;
import de.mineformers.idea.minecraft.lang.i18n.psi.*;
import com.intellij.navigation.ItemPresentation;

public class I18nPropertyImpl extends I18nNamedElementImpl implements I18nProperty {

  public I18nPropertyImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof I18nVisitor) ((I18nVisitor)visitor).visitProperty(this);
    else super.accept(visitor);
  }

  public String getKey() {
    return I18nPsiUtil.INSTANCE.getKey(this);
  }

  public String getValue() {
    return I18nPsiUtil.INSTANCE.getValue(this);
  }

  public String getName() {
    return I18nPsiUtil.INSTANCE.getName(this);
  }

  public PsiElement setName(String newName) {
    return I18nPsiUtil.INSTANCE.setName(this, newName);
  }

  public PsiElement getNameIdentifier() {
    return I18nPsiUtil.INSTANCE.getNameIdentifier(this);
  }

  public ItemPresentation getPresentation() {
    return I18nPsiUtil.INSTANCE.getPresentation(this);
  }

  public String toString() {
    return I18nPsiUtil.INSTANCE.toString(this);
  }

}
