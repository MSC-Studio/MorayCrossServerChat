package org.MSCS.CrossServerChat.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;
import java.util.UUID;

import org.MSCS.CrossServerChat.CrossServerChatPlugin;

public class PlayerChatListener {
    
    private final CrossServerChatPlugin plugin;
    
    public PlayerChatListener(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        // 检查是否以特定前缀开头
        if (message.startsWith("!")) {
            // 使用 ! 前缀发送全局消息
            event.setResult(PlayerChatEvent.ChatResult.denied()); // 取消原始聊天
            
            String globalMessage = message.substring(1).trim();
            if (!globalMessage.isEmpty()) {
                plugin.getChatManager().sendGlobalMessage(player, globalMessage);
            }
        } else if (message.startsWith("@")) {
            // 私聊快捷方式：@玩家名 消息
            event.setResult(PlayerChatEvent.ChatResult.denied());
            
            String[] parts = message.substring(1).split("\\s+", 2);
            if (parts.length >= 2) {
                String targetName = parts[0];
                String privateMessage = parts[1];
                
                // 查找目标玩家
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
        // 玩家断开连接时清理数据
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        var chatManager = plugin.getChatManager();
        
        // 移除私聊模式
        chatManager.setPrivateChatMode(playerId, null);
        
        // 通知最后私聊对象
        var lastChatterOpt = chatManager.getLastPrivateChatter(playerId);
        lastChatterOpt.ifPresent(lastChatter -> {
            lastChatter.sendMessage(Component.text(
                player.getUsername() + " 已断开连接。", 
                NamedTextColor.GRAY
            ));
        });
    }
}