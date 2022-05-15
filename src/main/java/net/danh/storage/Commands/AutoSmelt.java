package net.danh.storage.Commands;

import net.danh.dcore.Commands.CMDBase;
import net.danh.dcore.Utils.Chat;
import net.danh.storage.Manager.Files;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static net.danh.dcore.Utils.Player.sendPlayerMessageType;
import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.getconfigfile;
import static net.danh.storage.Manager.Files.getlanguagefile;

public class AutoSmelt extends CMDBase {

    public AutoSmelt(JavaPlugin core) {
        super(core, "AutoSmelt");
    }

    @Override
    public void playerexecute(Player p, String[] args) {
        if (args.length == 0) {
            if (p.hasPermission("storage.asmelt")) {
                setautoSmelt(p, !autoSmelt(p));
                if (Files.getconfigfile().getBoolean("Message.TOGGLE.STATUS")) {
                    if (autoSmelt(p)) {
                        sendPlayerMessageType(p, Files.getconfigfile().getString("Message.TOGGLE.TYPE"), getlanguagefile().getString("Toggle_Status")
                                .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autosmelt")))
                                .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.true"))));
                    }
                    if (!autoSmelt(p)) {
                        sendPlayerMessageType(p, Files.getconfigfile().getString("Message.TOGGLE.TYPE"), getlanguagefile().getString("Toggle_Status")
                                .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autosmelt")))
                                .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.false"))));
                    }
                }
            }
        }
    }

    @Override
    public void consoleexecute(ConsoleCommandSender c, String[] args) {
        if (args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                c.sendMessage(Chat.colorize("&c" + args[0] + " is null"));
                return;
            }
            if (Files.getconfigfile().getBoolean("Message.TOGGLE.STATUS")) {
                if (autoSmelt(player)) {
                    sendPlayerMessageType(player, Files.getconfigfile().getString("Message.TOGGLE.TYPE"), getlanguagefile().getString("Toggle_Status")
                            .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autosmelt")))
                            .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.true"))));
                }
                if (!autoSmelt(player)) {
                    sendPlayerMessageType(player, Files.getconfigfile().getString("Message.TOGGLE.TYPE"), getlanguagefile().getString("Toggle_Status")
                            .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autosmelt")))
                            .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.false"))));
                }
            }
        }
    }
}
