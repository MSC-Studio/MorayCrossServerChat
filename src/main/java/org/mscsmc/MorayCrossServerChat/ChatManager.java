package org.mscsmc.MorayCrossServerChat;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {

    private final ProxyServer server;
    

    private final Map<UUID, UUID> lastPrivateChatters = new ConcurrentHashMap<>();
    

    private final Set<UUID> disabledGlobalChat = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    // 私聊模式开关
    private final Map<UUID, UUID> privateChatMode = new ConcurrentHashMap<>();
    
    public ChatManager(CrossServerChatPlugin plugin) {
        this.server = plugin.getServer();
    }
    

    public void sendGlobalMessage(Player sender, String message) {

        if (disabledGlobalChat.contains(sender.getUniqueId())) {
            sender.sendMessage(Component.text("你已禁用全局聊天！使用 /togglechat 重新启用。", NamedTextColor.RED));
            return;
        }
        

        String serverName = "未知";
        if (sender.getCurrentServer().isPresent()) {
            serverName = sender.getCurrentServer().get().getServerInfo().getName();
        }
        

        Component formattedMessage = Component.text()
            .append(Component.text("[全局] ", NamedTextColor.DARK_GREEN))
            .append(Component.text(sender.getUsername(), NamedTextColor.GOLD))
            .append(Component.text(" @", NamedTextColor.GRAY))
            .append(Component.text(serverName, NamedTextColor.GREEN))
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append(Component.text(message, NamedTextColor.WHITE))
            .build();
        

        for (Player player : server.getAllPlayers()) {
            if (!disabledGlobalChat.contains(player.getUniqueId())) {
                player.sendMessage(formattedMessage);
            }
        }
        
        server.getConsoleCommandSource().sendMessage(formattedMessage);
    }
    

    public void sendPrivateMessage(Player sender, Player target, String message) {
        Component senderMessage = Component.text()
            .append(Component.text("[私聊] ", NamedTextColor.LIGHT_PURPLE))
            .append(Component.text("你 -> " + target.getUsername(), NamedTextColor.YELLOW))
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append(Component.text(message, NamedTextColor.WHITE))
            .build();
        

        Component targetMessage = Component.text()
            .append(Component.text("[私聊] ", NamedTextColor.LIGHT_PURPLE))
            .append(Component.text(sender.getUsername() + " -> 你", NamedTextColor.YELLOW))
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append(Component.text(message, NamedTextColor.WHITE))
            .build();
        
        // 发送消息
        sender.sendMessage(senderMessage);
        target.sendMessage(targetMessage);
        

        lastPrivateChatters.put(sender.getUniqueId(), target.getUniqueId());
        lastPrivateChatters.put(target.getUniqueId(), sender.getUniqueId());
    }
    

    public void sendAnnouncement(String message) {
        Component announcement = Component.text()
            .append(Component.text("【公告】", NamedTextColor.RED, TextDecoration.BOLD))
            .append(Component.text(" " + message, NamedTextColor.YELLOW))
            .build();
        
        broadcastToAll(announcement);
    }
    

    public void sendServerMessage(String serverName, String message) {
        Component serverMsg = Component.text()
            .append(Component.text("[" + serverName + "]", NamedTextColor.BLUE))
            .append(Component.text(" " + message, NamedTextColor.WHITE))
            .build();
        

        Optional<RegisteredServer> serverOpt = server.getServer(serverName);
        if (serverOpt.isPresent()) {
            RegisteredServer targetServer = serverOpt.get();
            for (Player player : targetServer.getPlayersConnected()) {
                player.sendMessage(serverMsg);
            }
        }
    }
    

    public boolean toggleGlobalChat(UUID playerId) {
        if (disabledGlobalChat.contains(playerId)) {
            disabledGlobalChat.remove(playerId);
            return true; // 已启用
        } else {
            disabledGlobalChat.add(playerId);
            return false; // 已禁用
        }
    }
    

    public void setPrivateChatMode(UUID playerId, UUID targetId) {
        if (targetId == null) {
            privateChatMode.remove(playerId);
        } else {
            privateChatMode.put(playerId, targetId);
        }
    }
    

    public UUID getPrivateChatTarget(UUID playerId) {
        return privateChatMode.get(playerId);
    }
    

    public Optional<Player> getLastPrivateChatter(UUID playerId) {
        UUID lastChatterId = lastPrivateChatters.get(playerId);
        if (lastChatterId != null) {
            return server.getPlayer(lastChatterId);
        }
        return Optional.empty();
    }
    

    public boolean isGlobalChatDisabled(UUID playerId) {
        return disabledGlobalChat.contains(playerId);
    }
    

    private void broadcastToAll(Component message) {
        for (Player player : server.getAllPlayers()) {
            player.sendMessage(message);
        }
        server.getConsoleCommandSource().sendMessage(message);
    }
    

    public Component getFormattedPlayerList() {
        List<Player> players = new ArrayList<>(server.getAllPlayers());
        
        if (players.isEmpty()) {
            return Component.text("当前没有在线玩家。", NamedTextColor.RED);
        }
        

        var builder = Component.text();
        builder.append(Component.text("在线玩家 (" + players.size() + "):\n", NamedTextColor.GREEN));
        

        Map<String, List<String>> playersByServer = new TreeMap<>();
        
        for (Player player : players) {
            String serverName = "未知";
            if (player.getCurrentServer().isPresent()) {
                serverName = player.getCurrentServer().get().getServerInfo().getName();
            }
            
            playersByServer.computeIfAbsent(serverName, k -> new ArrayList<>())
                .add(player.getUsername());
        }
        

        for (Map.Entry<String, List<String>> entry : playersByServer.entrySet()) {
            builder.append(Component.text("\n[" + entry.getKey() + "] ", NamedTextColor.AQUA))
                   .append(Component.text(String.join(", ", entry.getValue()), NamedTextColor.WHITE));
        }
        
        return builder.build();
    }
}