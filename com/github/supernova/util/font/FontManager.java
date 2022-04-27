package com.github.supernova.util.font;

import net.minecraft.util.ResourceLocation;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

public class FontManager {

    private static Font monoFontFile = null;

    static {
        try {
            File fontFile = new File("assets/supernova/MonoFont.ttf");
            System.out.println(fontFile.getAbsolutePath());
            monoFontFile = Font.createFont(Font.TRUETYPE_FONT,
                    fontFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GlyphPageFontRenderer monoFont15 = GlyphPageFontRenderer.create(monoFontFile.deriveFont(15f));
    public static GlyphPageFontRenderer consolas15 = GlyphPageFontRenderer.create("Consolas", 15, false, false ,false);

}
