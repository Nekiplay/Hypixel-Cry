package com.example.examplemod.remotecontrol;

import com.example.examplemod.Main;
import com.example.examplemod.events.RemoteServerMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class RemoteServer {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static DataOutputStream out;
    private static BufferedReader in;

    public static boolean startServer(int port) {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (serverSocket == null || serverSocket.isClosed()) {
                serverSocket = new ServerSocket(port);
                Thread serverList = new Thread(() -> {
                    RemoteServer.listener();
                });
                serverList.start();
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.serverprefix + EnumChatFormatting.WHITE + "Remote Server started"));
                return true;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return false;
    }

    public static boolean awaitConnection()
    {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                Charset utf8 = Charset.forName("UTF-8");
                clientSocket = serverSocket.accept();
                out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), utf8));
                return true;
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return false;
    }
    public static void sendMessage(String text)  {
        if (serverSocket != null && !serverSocket.isClosed()) {
            if (clientSocket.isConnected()) {
                try {
                    String text2 = text + "\n";
                    Charset utf8 = Charset.forName("UTF-8");
                    byte[] b = text2.getBytes(utf8);
                    out.write(b, 0, b.length);
                    out.flush();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }
    public static void listener() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                boolean connected = awaitConnection();
                if (connected) {
                    while (clientSocket.isConnected()) {
                        String line = in.readLine();
                        if (line != null) {
                            System.out.println(line);
                            RemoteServerMessage remoteServerMessage = new RemoteServerMessage(line);
                            MinecraftForge.EVENT_BUS.post(remoteServerMessage);
                        }
                    }
                    clientSocket.close();
                    clientSocket = null;
                    Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.serverprefix + EnumChatFormatting.WHITE + "Remote Server closed"));
                }
            } catch (IOException e) {
                clientSocket = null;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.serverprefix + EnumChatFormatting.WHITE + "Remote Server closed"));
            }
        }
    }
}
