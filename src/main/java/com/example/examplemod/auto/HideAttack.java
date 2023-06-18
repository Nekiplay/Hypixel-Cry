package com.example.examplemod.auto;

import com.example.examplemod.Main;
import com.example.examplemod.events.AttackEntity;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HideAttack {
    int last_slot = -1;
    @SubscribeEvent
    public void onAttackEntity(AttackEntity event)
    {
        if (event != null && Main.myConfigFile != null && Main.myConfigFile.HideAttack && event.attacked != null && Main.mc.thePlayer != null && Main.mc.thePlayer.inventory != null && Main.mc.getNetHandler().getNetworkManager() != null) {
            if (!event.isCanceled()) {
                event.setCanceled(true);
            }
            last_slot = event.player.inventory.currentItem;
            NetworkManager client = Main.mc.getNetHandler().getNetworkManager();
            PlayerControllerMP controllerMP = Main.mc.playerController;

            if (client != null && controllerMP != null) {
                if (last_slot != Main.myConfigFile.HideAttackWeaponSlot) {
                    client.sendPacket(new C09PacketHeldItemChange(Main.myConfigFile.HideAttackWeaponSlot - 1));
                }
                client.sendPacket(new C02PacketUseEntity(event.attacked, C02PacketUseEntity.Action.ATTACK));
                if (controllerMP.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
                    event.player.attackTargetEntityWithCurrentItem(event.attacked);
                }
                if (last_slot != Main.myConfigFile.HideAttackWeaponSlot && last_slot != -1) {
                    client.sendPacket(new C09PacketHeldItemChange(last_slot));
                }
            }
        }
    }
}
