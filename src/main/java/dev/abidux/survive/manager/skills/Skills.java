package dev.abidux.survive.manager.skills;

import dev.abidux.survive.model.skills.skill.CappedSkill;
import dev.abidux.survive.model.skills.skill.Skill;
import dev.abidux.survive.model.skills.skill.UncappedSkill;
import dev.abidux.survive.util.chat.ColoredText;
import org.bukkit.Material;

public class Skills {

    public static final CappedSkill FERRARIA = new CappedSkill(new ColoredText("{#f26418}Fe{#f3712b}rr{#f47e3e}ar{#f5915b}ia"),
            Material.IRON_INGOT, 50, 150, 250, 500, 750, 1000, 1250, 1500, -1);
    public static final CappedSkill MINERAR = new CappedSkill(new ColoredText("{#5bd7f5}Mi{#73ddf7}ne{#87e1f8}ra{#9ae6f9}r"),
            Material.IRON_PICKAXE, 100, 500, 1000, 1500, 2000, 2500, 3500, 4500, 5000, -1);
    public static final CappedSkill PLANTAR = new CappedSkill(new ColoredText("{#73350c}Pl{#7d3a0d}an{#69923a}ta{#6a9e2e}r"),
            Material.IRON_HOE, 100, 500, 1000, 1500, 2000, 2500, 3500, 4500, 5000, -1);
    public static final CappedSkill SERRARIA = new CappedSkill(new ColoredText("{#c04821}Se{#da5225}rr{#de673f}ar{#e37c59}ia"),
            Material.IRON_AXE, 100, 500, 1000, 1500, 2000, 2500, -1);
    public static final UncappedSkill OFENSIVA = new UncappedSkill(new ColoredText("{#ab0000}Of{#c2280a}en{#d91a1a}si{#db3616}va"),
            Material.IRON_SWORD, (level) -> level * 200 + 300);
    public static final UncappedSkill DEFENSIVA = new UncappedSkill(new ColoredText("{#0b439e}De{#0f52bd}fe{#1d63d1}ns{#3075e3}iv{#397eed}a"),
            Material.IRON_CHESTPLATE, (level) -> level * 200 + 300);

    public static Skill getSkillByName(String name) {
        return switch(name.toUpperCase()) {
            case "FERRARIA" -> FERRARIA;
            case "MINERAR" -> MINERAR;
            case "PLANTAR" -> PLANTAR;
            case "SERRARIA" -> SERRARIA;
            case "OFENSIVA" -> OFENSIVA;
            case "DEFENSIVA" -> DEFENSIVA;
            default -> null;
        };
    }

}