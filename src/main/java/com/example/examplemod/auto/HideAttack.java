package com.example.examplemod.auto;

import com.example.examplemod.Main;
import com.example.examplemod.events.AttackEntity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HideAttack {
    int last_slot = -1;
    @SubscribeEvent
    public void onAttackEntity(AttackEntity event)
    {
        if (Main.configFile.HideAttack && event.attacked != null && Main.mc.thePlayer != null && Main.mc.thePlayer.inventory != null) {
            last_slot = Main.mc.thePlayer.inventory.currentItem;
            event.setCanceled(true);
            if (last_slot != Main.configFile.HideAttackWeaponSlot)
                Main.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(Main.configFile.HideAttackWeaponSlot));
            Main.mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(event.attacked, C02PacketUseEntity.Action.ATTACK));
            if (Main.mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
                Main.mc.thePlayer.attackTargetEntityWithCurrentItem(event.attacked);
            }
            if (last_slot != Main.configFile.HideAttackWeaponSlot)
                Main.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(last_slot));
        }
    }
}
