package ru.whitebeef.beefspfog.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.whitebeef.beefspfog.BeefSPFog;

import java.util.Arrays;
import java.util.List;

public class FogCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Неизвестный формат команды");
            return true;
        }
        if (!args[0].equals("reload")) {
            sender.sendMessage(ChatColor.RED + "Неизвестный формат команды");
            return true;
        }
        BeefSPFog.getInstance().getPluginSettings().reloadSettings();
        sender.sendMessage(ChatColor.GREEN + "Конфигурация перезагружена");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Arrays.asList("reload");
    }
}
