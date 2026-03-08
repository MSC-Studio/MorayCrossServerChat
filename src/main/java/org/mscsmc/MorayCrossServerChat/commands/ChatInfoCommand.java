package org.mscsmc.moraycrossserverchat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.mscsmc.moraycrossserverchat.CrossServerChatPlugin;

public class ChatInfoCommand implements SimpleCommand {
    
    private final CrossServerChatPlugin plugin;
    
    public ChatInfoCommand(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(
                Component.text("只有玩家可以使用此命令！", NamedTextColor.RED));
            return;
        }
        
        Player player = (Player) invocation.source();
        var chatManager = plugin.getChatManager();
        

        Component message = Component.text()
            .append(Component.text("=== 聊天状态 ===\n", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("全局聊天: ", NamedTextColor.GRAY))
            .append(chatManager.isGlobalChatDisabled(player.getUniqueId()) ?
                Component.text("已禁用", NamedTextColor.RED) :
                Component.text("已启用", NamedTextColor.GREEN))
            .append(Component.text("\n\n=== 可用命令 ===\n", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("/g <消息>     发送全局消息\n", NamedTextColor.YELLOW))
            .append(Component.text("/togglechat   切换聊天开关\n", NamedTextColor.YELLOW))
            .append(Component.text("/msg <玩家> <消息>  发送私聊\n", NamedTextColor.YELLOW))
            .append(Component.text("/r <消息>     回复最后私聊\n", NamedTextColor.YELLOW))
            .append(Component.text("/list         查看在线玩家\n", NamedTextColor.YELLOW))
            .append(Component.text("/chatinfo     显示此帮助\n", NamedTextColor.YELLOW))
            .append(Component.text("\n=== 快捷方式 ===\n", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("!消息         快捷全局消息\n", NamedTextColor.AQUA))
            .append(Component.text("@玩家 消息    快捷私聊", NamedTextColor.AQUA))
            .build();
        
        player.sendMessage(message);
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }
}