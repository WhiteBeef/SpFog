package ru.whitebeef.beefspfog.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.whitebeef.beefspfog.BeefSPFog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FogCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Неизвестный формат команды");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            BeefSPFog.getInstance().getPluginSettings().reloadSettings();
            sender.sendMessage(ChatColor.GREEN + "Конфигурация перезагружена");
            return true;
        } else if (args[0].equalsIgnoreCase("presets")) {
            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "Неизвестный формат команды");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Команда доступна только для игроков");
                return true;
            }
            Player player = (Player) sender;
            String name = args[1];
            FogPresets preset = BeefSPFog.getInstance().getPluginSettings().getFogPresetsByName(name);
            if (preset == null) {
                sender.sendMessage(ChatColor.RED + "Неизвестный набор настроек тумана.");
                return true;
            }
            FogPresetsUtil.setPreset(player, name);
            FogPresets.setPlayerPreset(player, preset);
            sender.sendMessage(ChatColor.GREEN + "Настройка отображения тумана изменена на " + preset.getName());
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Неизвестный формат команды");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return Arrays.asList("reload", "presets");
        if (args.length == 2 && args[0].equalsIgnoreCase("presets"))
            return Arrays.asList("low", "medium", "high");
        return new ArrayList<>();

    }
}
