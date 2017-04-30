package de.mineformers.idea.minecraft.lang.i18n;

import com.intellij.lang.Language;

/**
 * ${JDOC}
 */
public class I18nLanguage extends Language {
    public static final I18nLanguage INSTANCE = new I18nLanguage();

    private I18nLanguage() {
        super("MCLang");
    }
}
