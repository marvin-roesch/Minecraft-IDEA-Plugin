package de.mineformers.idea.minecraft.lang.i18n.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFile;
import de.mineformers.idea.minecraft.lang.i18n.I18nLanguage;
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes;
import org.jetbrains.annotations.NotNull;

/**
 * I18nParserDefinition
 *
 * @author PaleoCrafter
 */
public class I18nParserDefinition implements ParserDefinition
{
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(I18nTypes.COMMENT);

    public static final IFileElementType FILE = new IFileElementType(Language.<I18nLanguage>findInstance(I18nLanguage.class));

    @NotNull
    @Override
    public Lexer createLexer(Project project)
    {
        return new I18nLexer();
    }

    @NotNull
    public TokenSet getWhitespaceTokens()
    {
        return WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens()
    {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements()
    {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiParser createParser(final Project project)
    {
        return new I18nParser();
    }

    @Override
    public IFileElementType getFileNodeType()
    {
        return FILE;
    }

    public PsiFile createFile(FileViewProvider viewProvider)
    {
        return new I18nFile(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
    {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node)
    {
        return I18nTypes.Factory.createElement(node);
    }
}
