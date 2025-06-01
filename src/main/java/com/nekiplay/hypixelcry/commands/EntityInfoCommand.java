package com.nekiplay.hypixelcry.commands;

import com.mojang.authlib.properties.Property;
import com.nekiplay.hypixelcry.HypixelCry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.List;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class EntityInfoCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "entityinfo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        StringBuilder copy = new StringBuilder();

        Entity nametagEntity = getNametag();
        Entity name = getName();
        if (nametagEntity != null && name != null) {
            String nametag = nametagEntity.getCustomNameTag();
            sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + "[Custom name] " + nametag));
            sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + "[Name] " + name.getName()));
            copy.append("[Custom name] ").append(nametag).append("\n");
            copy.append("[Name] ").append(name.getName()).append("\n");
        }

        Entity skullEntity = getSkull();
        if (skullEntity != null) {
            EntityArmorStand armorStand = (EntityArmorStand)skullEntity;
            ItemStack helmet = armorStand.getEquipmentInSlot(4);
            if (helmet.getItem() == Items.skull && helmet.hasTagCompound()) {
                NBTTagCompound tag = helmet.getTagCompound();
                if (tag.hasKey("SkullOwner", 10)) {
                    NBTTagCompound skullOwner = tag.getCompoundTag("SkullOwner");
                    if (skullOwner.hasKey("Id", 8)) {
                        String id = skullOwner.getString("Id");
                        sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + "[ArmorStand SkullOwner] " + id));
                        copy.append("[ArmorStand SkullOwner] ").append(id).append("\n");
                    }
                }
            }
        }

        Entity headEntity = getHeadName();
        if (headEntity != null) {
            EntityArmorStand armorStand = (EntityArmorStand)headEntity;
            ItemStack helmet = armorStand.getEquipmentInSlot(4);
            if (helmet != null && !helmet.getDisplayName().isEmpty()) {
                sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + "[ArmorStand Head Name] " + helmet.getDisplayName()));
                copy.append("[ArmorStand Head name] ").append(helmet.getDisplayName()).append("\n");
            }
        }

        Entity playerEntity = getPlayer();
        if (playerEntity != null) {
            EntityPlayer player = (EntityPlayer)playerEntity;
            Map<String, Collection<Property>> m = player.getGameProfile().getProperties().asMap();
            Collection<Property> textures = m.get("textures");

            for (Property entry : textures) {
                sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + "[" + player.getName() + "] [Skin id] " + entry.getValue()));
                copy.append("[").append(player.getName()).append("] [Skin id] ").append(entry.getValue()).append("\n");
            }
        }


        if (!copy.toString().isEmpty()) {
            GuiScreen.setClipboardString(copy.toString());
        }
    }

    @Nullable
    private Entity getName() {
        return mc.theWorld.getLoadedEntityList().
                stream().
                filter(entity ->
                        entity != mc.thePlayer)
                .min(Comparator.comparingDouble(entity -> entity.getDistanceSqToCenter(mc.thePlayer.getPosition()))).orElse(null);
    }

    @Nullable
    private Entity getNametag() {
        return mc.theWorld.getLoadedEntityList().
                stream().
                filter(Entity::hasCustomName)
                .min(Comparator.comparingDouble(entity -> entity.getDistanceSqToCenter(mc.thePlayer.getPosition()))).orElse(null);
    }

    @Nullable
    private Entity getHeadName() {
        return mc.theWorld.getLoadedEntityList().
                stream().
                filter(entity ->
                        entity instanceof EntityArmorStand && ((EntityArmorStand) entity).getEquipmentInSlot(4) != null)
                .min(Comparator.comparingDouble(entity -> entity.getDistanceSqToCenter(mc.thePlayer.getPosition()))).orElse(null);
    }

    @Nullable
    private Entity getSkull() {
        return mc.theWorld.getLoadedEntityList().
                stream().
                filter(entity ->
                        entity instanceof EntityArmorStand && ((EntityArmorStand) entity).getEquipmentInSlot(4) != null && ((EntityArmorStand) entity).getEquipmentInSlot(4).getItem() == Items.skull && ((EntityArmorStand) entity).getEquipmentInSlot(4).hasTagCompound() && ((EntityArmorStand) entity).getEquipmentInSlot(4).getTagCompound().hasKey("SkullOwner", 10) && ((EntityArmorStand) entity).getEquipmentInSlot(4).getTagCompound().getCompoundTag("SkullOwner").hasKey("Id", 8))
                .min(Comparator.comparingDouble(entity -> entity.getDistanceSqToCenter(mc.thePlayer.getPosition()))).orElse(null);
    }

    @Nullable
    private Entity getPlayer() {
        return mc.theWorld.getLoadedEntityList().
                stream().
                filter(entity ->
                        entity instanceof EntityPlayer && entity != mc.thePlayer)
                .min(Comparator.comparingDouble(entity -> entity.getDistanceSqToCenter(mc.thePlayer.getPosition()))).orElse(null);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@NotNull ICommand iCommand) {
        return 0;
    }
}
