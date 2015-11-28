package net.cosmosmc.mcze.commands;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.Configuration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class SetLobbySpawn implements CommandExecutor {

    private ZombieEscape plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            Messages.PLAYER_ONLY_COMMAND.send(commandSender);
            return false;
        }

        Configuration configuration = plugin.getConfiguration();
        configuration.setSpawn((Player) commandSender);
        return false;
    }

}