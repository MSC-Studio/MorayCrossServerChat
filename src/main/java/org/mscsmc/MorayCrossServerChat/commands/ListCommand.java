package org.mscsmc.MorayCrossServerChat.commands;

import org.mscsmc.MorayCrossServerChat.CrossServerChatPlugin;

import com.velocitypowered.api.command.SimpleCommand;

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