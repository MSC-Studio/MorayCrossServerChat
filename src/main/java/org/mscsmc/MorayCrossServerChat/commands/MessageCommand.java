package org.mscsmc.MorayCrossServerChat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

import org.mscsmc.MorayCrossServerChat.CrossServerChatPlugin;

public class MessageCommand implements SimpleCommand {
    
    private final CrossServerChatPlugin plugin;
    
    public MessageCommand(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(
                Component.text("只有玩家可以使用此命令！", NamedTextColor.RED));
            return;
        }
        
        Player sender = (Player) invocation.source();
        String[] args = invocation.arguments();
        
        if (args.length < 2) {
            sender.sendMessage(Component.text("用法: /msg <玩家> <消息>", NamedTextColor.RED));
            sender.sendMessage(Component.text("快捷方式: @玩家 消息", NamedTextColor.GRAY));
            return;
        }
        
        String targetName = args[0];
        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        
        Optional<Player> targetOpt = plugin.getServer().getPlayer(targetName);
        if (targetOpt.isPresent()) {
            plugin.getChatManager().sendPrivateMessage(sender, targetOpt.get(), message);
        } else {
            sender.sendMessage(Component.text("找不到玩家: " + targetName, NamedTextColor.RED));
        }
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }
}