// This is a generated file. Not intended for manual editing.
package de.mineformers.idea.minecraft.lang.i18n.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import de.mineformers.idea.minecraft.lang.i18n.psi.impl.*;

public interface I18nTypes {

  IElementType PROPERTY = new I18nElementType("PROPERTY");

  IElementType COMMENT = new I18nTokenType("COMMENT");
  IElementType CRLF = new I18nTokenType("CRLF");
  IElementType KEY = new I18nTokenType("KEY");
  IElementType SEPARATOR = new I18nTokenType("SEPARATOR");
  IElementType VALUE = new I18nTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == PROPERTY) {
        return new I18nPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
