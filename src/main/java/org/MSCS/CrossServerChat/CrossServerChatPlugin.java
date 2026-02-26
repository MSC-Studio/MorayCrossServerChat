package org.MSCS.CrossServerChat;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "mscs-crossserverchat",
    name = "MSCS CrossServerChat",
    version = "2.0.1",
    authors = {"MSCS(Mc_ictuc)"}
)
public class CrossServerChatPlugin {
    
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private ChatManager chatManager;
    
    @Inject
    public CrossServerChatPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("MSCS CrossServerChat 插件正在加载...");
        

        if (!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdirs();
        }
        

        chatManager = new ChatManager(this);
        

        server.getEventManager().register(this, new org.MSCS.CrossServerChat.listeners.PlayerChatListener(this));
        

        registerCommands();
        
        logger.info("MSCS CrossServerChat 插件加载完成！");
    }
    
private void registerCommands() {
    CommandManager commandManager = server.getCommandManager();
    

    CommandMeta globalChatMeta = commandManager.metaBuilder("g")
        .aliases("global", "全局")
        .plugin(this)
        .build();
    commandManager.register(globalChatMeta, new org.MSCS.CrossServerChat.commands.GlobalChatCommand(this));
    

    CommandMeta toggleMeta = commandManager.metaBuilder("togglechat")
        .aliases("toggleglobal", "切换聊天", "chat")
        .plugin(this)
        .build();
    commandManager.register(toggleMeta, new org.MSCS.CrossServerChat.commands.ToggleChatCommand(this));
    

    CommandMeta msgMeta = commandManager.metaBuilder("msg")
        .aliases("tell", "whisper", "w", "私聊", "tell")
        .plugin(this)
        .build();
    commandManager.register(msgMeta, new org.MSCS.CrossServerChat.commands.MessageCommand(this));
    

    CommandMeta replyMeta = commandManager.metaBuilder("r")
        .aliases("reply", "回复")
        .plugin(this)
        .build();
    commandManager.register(replyMeta, new org.MSCS.CrossServerChat.commands.ReplyCommand(this));
    

    CommandMeta infoMeta = commandManager.metaBuilder("chatinfo")
        .aliases("chathelp", "聊天帮助", "帮助")
        .plugin(this)
        .build();
    commandManager.register(infoMeta, new org.MSCS.CrossServerChat.commands.ChatInfoCommand(this));
    

    CommandMeta listMeta = commandManager.metaBuilder("list")
        .aliases("online", "players", "玩家列表")
        .plugin(this)
        .build();
    commandManager.register(listMeta, new org.MSCS.CrossServerChat.commands.ListCommand(this));
}
    
    public ProxyServer getServer() {
        return server;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public Path getDataDirectory() {
        return dataDirectory;
    }
    
    public ChatManager getChatManager() {
        return chatManager;
    }
}