package org.mscsmc.moraycrossserverchat.listeners;

import org.mscsmc.moraycrossserverchat.CrossServerChatPlugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;
import java.util.UUID;

public class PlayerChatListener {
    
    private final CrossServerChatPlugin plugin;
    
    public PlayerChatListener(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        if (message.startsWith("!")) {
            event.setResult(PlayerChatEvent.ChatResult.denied()); 
            
            String globalMessage = message.substring(1).trim();
            if (!globalMessage.isEmpty()) {
                plugin.getChatManager().sendGlobalMessage(player, globalMessage);
            }
        } else if (message.startsWith("@")) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            
            String[] parts = message.substring(1).split("\\s+", 2);
            if (parts.length >= 2) {
                String targetName = parts[0];
                String privateMessage = parts[1];
                
                Optional<Player> targetOpt = plugin.getServer().getPlayer(targetName);
                if (targetOpt.isPresent()) {
                    plugin.getChatManager().sendPrivateMessage(player, targetOpt.get(), privateMessage);
                } else {
                    player.sendMessage(Component.text("玩家 " + targetName + " 未找到或不在线！", NamedTextColor.RED));
                }
            } else {
                player.sendMessage(Component.text("用法: @玩家名 消息", NamedTextColor.RED));
            }
        }
    }
    
    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        var chatManager = plugin.getChatManager();
        
        chatManager.setPrivateChatMode(playerId, null);
        

        var lastChatterOpt = chatManager.getLastPrivateChatter(playerId);
        lastChatterOpt.ifPresent(lastChatter -> {
            lastChatter.sendMessage(Component.text(
                player.getUsername() + " 已断开连接。", 
                NamedTextColor.GRAY
            ));
        });
    }
}