package org.mscsmc.moraycrossserverchat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.mscsmc.moraycrossserverchat.CrossServerChatPlugin;

public class ListCommand implements SimpleCommand {
    
    private final CrossServerChatPlugin plugin;
    
    public ListCommand(CrossServerChatPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation) {
        invocation.source().sendMessage(
            plugin.getChatManager().getFormattedPlayerList()
        );
    }
}