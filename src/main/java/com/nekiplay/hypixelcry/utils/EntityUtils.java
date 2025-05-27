package com.nekiplay.hypixelcry.utils;

import com.mojang.authlib.properties.Property;
import com.nekiplay.hypixelcry.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import static com.nekiplay.hypixelcry.Main.mc;

public class EntityUtils {
    @Nullable
    public static String getCustomNametag(Entity entity) {
        if (entity.hasCustomName()) {
            return entity.getCustomNameTag();
        }
        else {
            return null;
        }
    }

    @Nullable
    public static String getArmorStandHeadId(EntityArmorStand entity) {
        if (entity.getEquipmentInSlot(4) != null) {
            ItemStack helmet = entity.getEquipmentInSlot(4);
            if (helmet.getItem() == Items.skull && helmet.hasTagCompound()) {
                NBTTagCompound tag = helmet.getTagCompound();
                if (tag.hasKey("SkullOwner", 10)) {
                    NBTTagCompound skullOwner = tag.getCompoundTag("SkullOwner");
                    if (skullOwner.hasKey("Id", 8)) {
                        return skullOwner.getString("Id");
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public static String getArmorStandHeadName(EntityArmorStand entity) {
        if (entity.getEquipmentInSlot(4) != null) {
            ItemStack helmet = entity.getEquipmentInSlot(4);
            if (helmet != null && !helmet.getDisplayName().isEmpty()) {
                return helmet.getDisplayName();
            }
        }
        return null;
    }

    @Nullable
    public static String getPlayerSkin(EntityPlayer player) {
        if (player.getGameProfile() == null) {
            return null;
        }
        Map<String, Collection<Property>> map = player.getGameProfile().getProperties().asMap();
        Collection<Property> textures = map.get("textures");

        Property texture = textures.stream().findFirst().orElse(null);
        if (texture != null) {
            return texture.getValue();
        }
        return null;
    }
}
