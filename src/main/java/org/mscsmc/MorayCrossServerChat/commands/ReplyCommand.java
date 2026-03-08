package org.mscsmc.moraycrossserverchat.commands;

import org.mscsmc.moraycrossserverchat.CrossServerChatPlugin;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class ReplyCommand implements SimpleCommand {
    
    private final CrossServerChatPlugin plugin;
    
    public ReplyCommand(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        
        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("只有玩家可以使用此命令！", NamedTextColor.RED));
            return;
        }
        
        Player sender = (Player) source;
        
        if (args.length == 0) {
            sender.sendMessage(Component.text("用法: /r <消息>", NamedTextColor.RED));
            return;
        }
        

        Optional<Player> lastChatter = plugin.getChatManager().getLastPrivateChatter(sender.getUniqueId());
        
        if (!lastChatter.isPresent()) {
            sender.sendMessage(Component.text("你没有最近的私聊对象！", NamedTextColor.RED));
            return;
        }
        
        Player target = lastChatter.get();
        

        if (!target.isActive()) {
            sender.sendMessage(Component.text("玩家 " + target.getUsername() + " 已离线！", NamedTextColor.RED));
            return;
        }
        
        String message = String.join(" ", args);
        plugin.getChatManager().sendPrivateMessage(sender, target, message);
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }
}