package org.mscsmc.moraycrossserverchat.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.mscsmc.moraycrossserverchat.CrossServerChatPlugin;

public class GlobalChatCommand implements SimpleCommand {
    
    private final CrossServerChatPlugin plugin;
    
    public GlobalChatCommand(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        
        if (source instanceof Player) {
            Player player = (Player) source;
            
            if (args.length == 0) {
                player.sendMessage(Component.text("用法: /g <消息>", NamedTextColor.RED));
                player.sendMessage(Component.text("快捷方式: 在普通聊天前加 !", NamedTextColor.GRAY));
                return;
            }
            
            String message = String.join(" ", args);
            plugin.getChatManager().sendGlobalMessage(player, message);
            
        } else {

            if (args.length == 0) {
                source.sendMessage(Component.text("用法: /g <公告内容>", NamedTextColor.RED));
                return;
            }
            
            String message = String.join(" ", args);
            plugin.getChatManager().sendAnnouncement("[控制台] " + message);
        }
    }
}