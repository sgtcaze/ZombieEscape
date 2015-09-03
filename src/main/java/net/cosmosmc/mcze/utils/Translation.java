package net.cosmosmc.mcze.utils;

import net.cosmosmc.mcze.core.constants.Language;

public final class Translation {

    private final Language LANGUAGE;
    private final String TRANSLATION;

    public Translation(Language lang, String translation) {
        this.LANGUAGE = lang;
        this.TRANSLATION = translation;
    }

    public Language getLanguage() {
        return this.LANGUAGE;
    }

    public String getTranslation(){
        return this.TRANSLATION;
    }

}
