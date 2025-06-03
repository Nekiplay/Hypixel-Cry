package com.nekiplay.hypixelcry.utils;

import org.lwjgl.util.vector.Vector2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ApecUtils {

    /** If you are in a fml workspace set this variable to true */
    public static boolean inFMLFramework = false;

    private static final String[] colorCodes = { "§0","§1","§2","§3","§4","§5","§6","§7","§8","§9","§a","§b","§c","§d","§e","§f" };

    private static final HashMap <String,Integer> multipleNotations = new HashMap<String, Integer>() {{
        put("k",1000);
        put("m",1000000);
    }};

    public static HashMap<String,String> unObfedFieldNames = new HashMap<String,String>() {{
        if (!inFMLFramework) {
            put("footer", "field_175255_h");
            put("header", "field_175256_i");
            put("upperChestInventory", "field_147016_v");
            put("lowerChestInventory", "field_147015_w");
            put("persistantChatGUI", "field_73840_e");
            put("sentMessages", "field_146248_g");
            put("streamIndicator", "field_152127_m");
            put("updateCounter", "field_73837_f");
            put("overlayPlayerList", "field_175196_v");
            put("guiIngame", "field_175251_g");
            put("chatMessages", "field_146253_i");
            put("theSlot","field_147006_u");
            put("stackTagCompound","field_77990_d");
        }
    }};

    public static HashMap<String,String> getUnObfedMethodNames = new HashMap<String, String>() {{
        if (!inFMLFramework) {
            put("handleMouseClick", "func_146984_a");
            put("drawItemStack","func_146982_a");
            put("drawGradientRect","func_73733_a");
        }
    }};

    /**
     * @param s = Input string
     * @return Returns a string with all the formating tags removed
     */

    public static String removeAllCodes(String s) {
        while (s.contains("§")) {
            s = s.replace("§"+s.charAt(s.indexOf("§") + 1),"");
        }
        return s;
    }

    /**
     * @param s = Input string
     * @return Returns a string with all the color tags removed
     */

    public static String removeColorCodes(String s) {
        for (String code : colorCodes) {
            s = s.replace(code,"§r");
            s = s.replace(code.toUpperCase(),"§r");
        }
        return s;
    }

    /**
     * @brief Shown the specified message in the chat if debug messages are on
     * @param string = Input message
     */

    public static void showMessage(String string) {
        //if (ApecMain.Instance.settingsManager.getSettingState(SettingID.SHOW_DEBUG_MESSAGES))
        //    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(string));
    }

    /**
     * @brief This is made since there is this weird character in the purse text that im too lazy to see what unicode it has so now we have this
     * @return Returns a string that has all non numerical characters removed from a string
     */

    public static String removeNonNumericalChars(String s) {

        StringBuilder _s = new StringBuilder();

        for (int i = 0;i < s.length();i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || c == '.') _s.append(c);
        }

        return _s.toString();

    }

    /**
     * @param s1 = The string in which the sequence will be searched
     * @param s2 = The char sequence
     * @return Returns true if the specified character sequence is present in the input string.
     * The characters can have other characters between them only the order in which they
     * exist matters.
     */

    public static boolean containedByCharSequence(String s1,String s2) {

        char[] c = s2.toCharArray();
        char[] s = s1.toCharArray();
        int cIdx = 0;
        for (int i = 0;i < s.length && cIdx < c.length;i++) {
            if (s[i] == c[cIdx]) cIdx++;
        }

        return cIdx == c.length;

    }

    /**
     * @param s = Input string
     * @return Returns a string without white spaces at the start of it, if any are present
     */

    public static String removeFirstSpaces(String s) {
        if (s.equals("")) return s;
        int nonSpaceIdx = 0;
        for (int i = 0;s.charAt(i) == ' ';i++) {
            nonSpaceIdx = i+1;
        }
        return s.substring(nonSpaceIdx);
    }

    /**
     * @param l A list of strings
     * @return Returns an ordered list of the input strings by their width
     */

    public static List<String> orderByWidth (List<String> l) {
        List<Integer> arr = new ArrayList<Integer>();
        for (String s : l) {
            arr.add(Minecraft.getMinecraft().fontRendererObj.getStringWidth(s));
        }
        bubbleSort(arr,l);
        return l;
    }


    // A wise man once said bubble sort is good enough when there are not a lot of elements
    public static <T> void  bubbleSort(List<Integer> arr,List<T> s) {
        int n = arr.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr.get(j) < arr.get(j + 1)) {
                    int temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);

                    T _temp = s.get(j);
                    s.set(j, s.get(j + 1));
                    s.set(j + 1, _temp);
                }
    }

    public enum SegmentationOptions {

        TOTALLY_EXCLUSIVE,
        TOTALLY_INCLUSIVE,
        ALL_INSTANCES_RIGHT,
        ALL_INSTANCES_LEFT

    }

    public static String segmentString(String string,String symbol,char leftChar,char rightChar,int allowedInstancesL,int allowedInstancesR, SegmentationOptions... options) {
        boolean totallyExclusive = false, totallyInclusive = false, allInstancesR = false,allInstancesL = false;
        for (SegmentationOptions option : options) {
            if (option == SegmentationOptions.TOTALLY_EXCLUSIVE) totallyExclusive = true;
            if (option == SegmentationOptions.TOTALLY_INCLUSIVE) totallyInclusive = true;
            if (option == SegmentationOptions.ALL_INSTANCES_RIGHT) allInstancesR = true;
            if (option == SegmentationOptions.ALL_INSTANCES_LEFT) allInstancesL = true;
        }
        return segmentString(string, symbol, leftChar, rightChar, allowedInstancesL, allowedInstancesR, totallyExclusive,totallyInclusive,allInstancesR,allInstancesL);
    }

    /**
     * @param string = The string you want to extract data from
     * @param symbol = A string that will act as a pivot
     * @param leftChar = It will copy all the character from the left of the pivot until it encounters this character
     * @param rightChar = It will copy all the character from the right of the pivot until it encounters this character
     * @param allowedInstancesL = How many times can it encounter the left char before it stops copying the characters
     * @param allowedInstancesR = How many times can it encounter the right char before it stops copying the characters
     * @param totallyExclusive = Makes so that the substring wont include the character from the left index
     * @return Returns the string that is defined by the bounds of leftChar and rightChar encountered allowedInstacesL  respectively allowedInctancesR - 1 within it
     *         allowedInsracesL only if totallyExclusive = false else allowedInstacesL - 1
     */

    public static String segmentString(String string,String symbol,char leftChar,char rightChar,int allowedInstancesL,int allowedInstancesR,boolean totallyExclusive,boolean totallyInclusive,boolean allInstancesR,boolean allInstancesL) {

        int leftIdx = 0,rightIdx = 0;

        if (string.contains(symbol)) {

            int symbolIdx = string.indexOf(symbol);

            for (int i = 0; symbolIdx - i > -1; i++) {
                leftIdx = symbolIdx - i;
                if (string.charAt(symbolIdx - i) == leftChar) allowedInstancesL--;
                if (allowedInstancesL == 0) {
                    break;
                }
            }

            symbolIdx += symbol.length() - 1;

            for (int i = 0; symbolIdx + i < string.length(); i++) {
                rightIdx = symbolIdx + i;
                if (string.charAt(symbolIdx + i) == rightChar) allowedInstancesR--;
                if (allowedInstancesR == 0) {
                    break;
                }
            }

            if (allowedInstancesL != 0 && allInstancesL) return null;
            if (allowedInstancesR != 0 && allInstancesR) return null;
            return string.substring(leftIdx + (totallyExclusive ? 1 : 0), rightIdx + (totallyInclusive ? 1 : 0));
        } else {
            return null;
        }

    }

    public static int RomanSymbolToValue(char s) {
        switch (s) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
        }
        return 0;
    }

    public static int RomanStringToValue(String str) {
        int res = 0;
        for (int i = 0; i < str.length(); i++)
        {
            int s1 = RomanSymbolToValue(str.charAt(i));
            if (i + 1 < str.length())
            {
                int s2 = RomanSymbolToValue(str.charAt(i + 1));
                if (s1 >= s2) {
                    res = res + s1;
                }
                else
                {
                    res = res + s2 - s1;
                    i++;
                }
            }
            else {
                res = res + s1;
            }
        }
        return res;
    }

    /**
     * @brief Converts values strings which contain values represented in short form (ex: "20k") to float
     * @param s = input string
     * @return Converted output
     */
    public static float hypixelShortValueFormattingToFloat(String s) {
        s = s.replace(",","");
        for (String notation : multipleNotations.keySet()) {
            if (s.contains(notation)) {
                s = s.replace(notation,"");
                return Float.parseFloat(s) * multipleNotations.get(notation);
            }
        }
        return Float.parseFloat(s);
    }
}