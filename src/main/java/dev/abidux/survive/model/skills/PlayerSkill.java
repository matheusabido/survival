package dev.abidux.survive.model.skills;

import dev.abidux.survive.manager.ActionbarManager;
import dev.abidux.survive.manager.PlayerStats;
import dev.abidux.survive.manager.skills.SkillXpAnnouncement;
import dev.abidux.survive.model.skills.skill.CappedSkill;
import dev.abidux.survive.model.skills.skill.Skill;
import dev.abidux.survive.model.skills.skill.UncappedSkill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerSkill {

    public final Skill type;
    private int level;
    private long xp;
    public PlayerSkill(Skill type, int level, long xp) {
        this.type = type;
        this.level = level;
        this.xp = xp;
    }

    public void addXp(Player player, int amount) {
        long evolve = type instanceof CappedSkill cap ? cap.levels[level] : ((UncappedSkill) type).calculateXpByLevelFunction.apply(level);
        if (evolve == -1) return;
        xp += amount;

        if (xp >= evolve) {
            xp = 0;
            level++;
            player.sendMessage("§6+1 LEVEL §7- " + type.name.legacyText);

            TextComponent component = new TextComponent(new TextComponent("§6+1 LEVEL §7- "), type.name.component);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }
        PlayerStats stats = PlayerStats.get(player);
        if (!stats.showXp) return;
        stats.skillXpAnnouncement = new SkillXpAnnouncement(type, xp, evolve);
        ActionbarManager.update(player, true);
    }

    public boolean isMaxed() {
        return type instanceof CappedSkill capped && capped.levels[level] == -1;
    }

    public long getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }
}