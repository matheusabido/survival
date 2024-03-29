package dev.abidux.survive.event.skills.combate.defensiva;

import dev.abidux.survive.model.skills.PlayerSkill;
import dev.abidux.survive.manager.PlayerStats;
import dev.abidux.survive.manager.skills.Skills;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class TakeDamageEvent implements Listener {

    @EventHandler
    void takeDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || event.getCause() == EntityDamageEvent.DamageCause.DROWNING
                || event.getCause() == EntityDamageEvent.DamageCause.VOID
                || event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) return;
        PlayerStats stats = PlayerStats.get(player);
        PlayerSkill skill = stats.getSkillSet().get(Skills.DEFENSIVA);
        double damage = Math.max(event.getDamage() - skill.getLevel(), 0);
        event.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.LOW)
    void takeDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.getDamager() instanceof Player) return;
        PlayerStats stats = PlayerStats.get(player);
        PlayerSkill skill = stats.getSkillSet().get(Skills.DEFENSIVA);

        if (event.getDamager().getType() == EntityType.ENDER_DRAGON) event.setDamage(200);
        else if (event.getDamager().getType() == EntityType.AREA_EFFECT_CLOUD) event.setDamage(50);

        double damage = Math.max(event.getFinalDamage() - skill.getLevel(), 0);
        int receivedXp = (int) (damage - 4);
        if (receivedXp > 0 && damage < player.getHealth()) {
            skill.addXp(player, receivedXp);
        }
    }

}