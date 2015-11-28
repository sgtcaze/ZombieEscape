package net.cosmosmc.mcze.commands;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class Ztele implements CommandExecutor {

    private ZombieEscape plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        GameArena gameArena = plugin.getGameArena();

        if (!gameArena.isGameRunning()) {
            Messages.GAME_NOT_RUNNING.send(player);
            return false;
        }

        if (gameArena.isHuman(player)) {
            Messages.ZOMBIE_ONLY_COMMAND.send(player);
            return false;
        }

        gameArena.teleportCheckpoint(player);
        return false;
    }

}