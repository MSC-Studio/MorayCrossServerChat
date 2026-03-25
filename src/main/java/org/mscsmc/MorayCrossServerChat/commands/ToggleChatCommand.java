package org.mscsmc.MorayCrossServerChat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.mscsmc.MorayCrossServerChat.CrossServerChatPlugin;

public class ToggleChatCommand implements SimpleCommand {
    
    private final CrossServerChatPlugin plugin;
    
    public ToggleChatCommand(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(
                Component.text("只有玩家可以使用此命令！", NamedTextColor.RED));
            return;
        }

        boolean isNowEnabled = plugin.getChatManager().toggleGlobalChat(player.getUniqueId());
        
        if (isNowEnabled) {
            player.sendMessage(Component.text("已启用全局聊天！", NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("已禁用全局聊天！", NamedTextColor.YELLOW));
        }
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }
}