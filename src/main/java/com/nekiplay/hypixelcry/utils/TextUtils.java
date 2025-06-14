package com.nekiplay.hypixelcry.utils;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextUtils {
    public static String textToFormattingString(Text text) {
        StringBuilder result = new StringBuilder();

        // Рекурсивно обрабатываем все части текста
        for (Text sibling : text.getSiblings()) {
            // Получаем стиль текущего текста
            Style style = sibling.getStyle();

            // Добавляем коды форматирования
            if (style.getColor() != null) {
                Formatting formatting = Formatting.byName(style.getColor().getName());
                if (formatting != null) {
                    result.append("§").append(formatting.getCode());
                }
            }

            result.append( style.isBold() ? "§l" : "" );
            result.append( style.isItalic() ? "§o" : "" );
            result.append( style.isUnderlined() ? "§n" : "" );
            result.append( style.isStrikethrough() ? "§m" : "" );
            result.append( style.isObfuscated() ? "§k" : "" );
        }

        return result.toString();
    }

}
