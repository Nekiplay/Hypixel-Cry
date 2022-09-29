package com.example.examplemod.auto;

import com.example.examplemod.Main;
import com.example.examplemod.events.RemoteServerMessage;
import com.example.examplemod.remotecontrol.RemoteServer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoteAccess {
    @SubscribeEvent
    public void onRemoteMessage(RemoteServerMessage event) {
        if (event.message.equals("GetNickname")) {
            EntityPlayerSP player = Main.mc.thePlayer;
            RemoteServer.sendMessage(player.getName());
        }
        else if (event.message.equals("GetPos")) {
            EntityPlayerSP player = Main.mc.thePlayer;
            RemoteServer.sendMessage("x:" + player.posX + " y:" + + player.posY + " z:" + + player.posZ);
        }
        else if (event.message.startsWith("AddChatText:")) {
            Pattern pattern = Pattern.compile("AddChatText:(.*)");
            Matcher matcher = pattern.matcher(event.message);
            if (matcher.find()) {
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(matcher.group(1)));
            }
        }
        else if (event.message.startsWith("SendChatText:")) {
            Pattern pattern = Pattern.compile("SendChatText:(.*)");
            Matcher matcher = pattern.matcher(event.message);
            if (matcher.find()) {
                Main.mc.thePlayer.sendChatMessage(matcher.group(1));
            }
        }
        else if (event.message.startsWith("Down key:")) {
            Pattern pattern = Pattern.compile("Down key:(.*)");
            Matcher matcher = pattern.matcher(event.message);
            if (matcher.find()) {
                int key;
                try {
                    key = Integer.parseInt(matcher.group(1));
                }
                catch (NumberFormatException e) {
                    key = 0;
                }
                KeyBinding.setKeyBindState(key, true);
            }
        }
        else if (event.message.startsWith("Up key:")) {
            Pattern pattern = Pattern.compile("Up key:(.*)");
            Matcher matcher = pattern.matcher(event.message);
            if (matcher.find()) {
                int key;
                try {
                    key = Integer.parseInt(matcher.group(1));
                }
                catch (NumberFormatException e) {
                    key = 0;
                }
                KeyBinding.setKeyBindState(key, false);
            }
        }
        else if (event.message.startsWith("Set angle:")) {
            Pattern pattern = Pattern.compile("Set angle: yaw:(.*) pitch:(.*)");
            Matcher matcher = pattern.matcher(event.message);
            if (matcher.find()) {
                try {
                    float yaw = Integer.parseInt(matcher.group(1));
                    float pitch = Integer.parseInt(matcher.group(2));
                    Main.mc.thePlayer.rotationYaw = yaw;
                    Main.mc.thePlayer.rotationPitch = pitch;
                }
                catch (NumberFormatException e) {

                }

            }
        }
    }
}
