package dev.abidux.survive.commands;

import dev.abidux.survive.manager.PlayerStats;
import dev.abidux.survive.manager.skills.SkillSet;
import dev.abidux.survive.manager.skills.Skills;
import dev.abidux.survive.util.registry.CommandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandRegistry("skills")
public class SkillsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Inventory inventory = args.length == 0 ? createForPlayer(player) : createForThird(args[0]);
        player.openInventory(inventory);
        return false;
    }

    private Inventory createForPlayer(Player player) {
        return buildInventory(PlayerStats.get(player).getSkillSet());
    }

    private Inventory createForThird(String player) {
        SkillSet set = PlayerStats.PLAYER_STATS.getOrDefault(player.toLowerCase(), new PlayerStats()).getSkillSet();
        return buildInventory(set);
    }

    private Inventory buildInventory(SkillSet set) {
        Inventory inventory = Bukkit.createInventory(null, 3*9, "Skills");
        inventory.setItem(10, Skills.FERRARIA.buildIcon(set));
        inventory.setItem(11, Skills.MINERAR.buildIcon(set));
        inventory.setItem(12, Skills.PLANTAR.buildIcon(set));
        inventory.setItem(13, Skills.SERRARIA.buildIcon(set));

        inventory.setItem(15, Skills.OFENSIVA.buildIcon(set));
        inventory.setItem(16, Skills.DEFENSIVA.buildIcon(set));
        return inventory;
    }
}