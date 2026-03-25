package org.mscsmc.MorayCrossServerChat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import org.mscsmc.MorayCrossServerChat.CrossServerChatPlugin;

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