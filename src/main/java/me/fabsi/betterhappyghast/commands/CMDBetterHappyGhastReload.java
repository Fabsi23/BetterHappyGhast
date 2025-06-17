package me.fabsi.betterhappyghast.commands;

import lombok.AllArgsConstructor;
import me.fabsi.betterhappyghast.config.DefaultConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import utils.MessageSender;

@AllArgsConstructor
public class CMDBetterHappyGhastReload implements CommandExecutor {

	private final MessageSender messageSender;
	private final DefaultConfig config;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		config.reload();
		messageSender.sendIfNotVoid(sender, DefaultConfig.TextKey.RELOADED_CONFIG);
		return true;
	}
}