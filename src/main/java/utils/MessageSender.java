package utils;

import me.fabsi.betterhappyghast.config.DefaultConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageSender {

    private final DefaultConfig config;

    public MessageSender(DefaultConfig config) {
        this.config = config;
    }

    public void sendIfNotVoid(CommandSender sender, DefaultConfig.TextKey messageKey) {
        String message = config.getMessage(messageKey);
        if (!(message.isBlank())) {
            message = message.replace("%PREFIX%", config.getPluginPrefix());
            sender.sendMessage(translateColors(message));
        }
    }

    private String translateColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
