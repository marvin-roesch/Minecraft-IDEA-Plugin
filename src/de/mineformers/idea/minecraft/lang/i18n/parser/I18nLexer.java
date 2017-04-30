package de.mineformers.idea.minecraft.lang.i18n.parser;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

/**
 * I18nLexer
 *
 * @author PaleoCrafter
 */
public class I18nLexer extends FlexAdapter
{
    public I18nLexer()
    {
        super(new _I18nLexer((Reader) null));
    }
}
