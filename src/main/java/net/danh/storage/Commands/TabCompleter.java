package net.danh.storage.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.danh.storage.Manager.Files.getconfigfile;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Nullable
    public static List<String> players = new ArrayList<>();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> none = new ArrayList<>();
        List<String> aarg0 = Arrays.asList("storage", "max_storage", "reload", "add", "take", "sell");
        List<String> aarg1 = Arrays.asList("set", "add", "remove");
        List<String> arg0 = Arrays.asList("add", "take", "sell");
        List<String> amount = Arrays.asList("all", "<positive number>");
        List<String> items = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            items.add(item);
        }
        if (label.equalsIgnoreCase("Storage") || label.equalsIgnoreCase("kho") || label.equalsIgnoreCase("store")) {
            if (args.length == 1) {
                if (sender.hasPermission("Storage.admin")) {
                    for (String r : aarg0) {
                        if (r.toUpperCase().startsWith(args[0].toUpperCase())) {
                            result.add(r);
                        }
                    }
                    return result;
                } else {
                    for (String r : arg0) {
                        if (r.toUpperCase().startsWith(args[0].toUpperCase())) {
                            result.add(r);
                        }
                    }
                    return result;
                }
            }
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("sell")) {
                if (args.length == 2) {
                    for (String r : items) {
                        if (r.toUpperCase().startsWith(args[1].toUpperCase())) {
                            result.add(r);
                        }
                    }
                    return result;
                }
                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("")) {
                        return amount;
                    }
                    if ("ALL".startsWith(args[2].toUpperCase())) {
                        result.add("all");
                        return result;
                    }
                    if (args[2].matches("\\d+")) {
                        result.add("<positive number>");
                        return result;
                    }
                }
            }
            if (args[0].equalsIgnoreCase("storage") || args[0].equalsIgnoreCase("max_storage")) {
                if (sender.hasPermission("Storage.admin")) {
                    if (args.length == 2) {
                        for (String r : aarg1) {
                            if (r.toUpperCase().startsWith(args[1].toUpperCase())) {
                                result.add(r);
                            }
                        }
                        return result;
                    }
                    if (args.length == 3) {
                        for (String r : players) {
                            if (r.toUpperCase().startsWith(args[2].toUpperCase())) {
                                result.add(r);
                            }
                        }
                        return result;
                    }
                    if (args.length == 4) {
                        for (String r : items) {
                            if (r.toUpperCase().startsWith(args[3].toUpperCase())) {
                                result.add(r);
                            }
                        }
                        return result;
                    }
                    if (args.length == 5) {
                        if (args[4].equalsIgnoreCase("")) {
                            result.add("<positive number>");
                            return result;
                        }
                        if (args[4].matches("\\d+")) {
                            result.add("<positive number>");
                            return result;
                        }
                    }
                }
            }
        }
        return none;
    }
}
