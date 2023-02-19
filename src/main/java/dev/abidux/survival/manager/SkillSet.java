package dev.abidux.survival.manager;

import dev.abidux.survival.model.Skill;

import java.util.HashMap;
import java.util.stream.Collectors;

public class SkillSet extends HashMap<Skill, PlayerSkill> {

    @Override
    public PlayerSkill get(Object key) {
        if (!(key instanceof Skill)) return null;
        Skill type = (Skill) key;
        PlayerSkill skill = super.get(type);
        if (skill == null) put(type, (skill = new PlayerSkill(0, 0)));
        return skill;
    }

    @Override
    public String toString() {
        return entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue().level + "/" + entry.getValue().xp).collect(Collectors.joining(";"));
    }

    public static SkillSet deserialize(String serialized) {
        SkillSet set = new SkillSet();
        for (String part : serialized.split(";")) {
            String[] s = part.split(":");
            String[] l = s[1].split("/");
            Skill type = Skills.getSkillByName(s[0]);
            int level = Integer.parseInt(l[0]);
            int xp = Integer.parseInt(l[1]);
            set.put(type, new PlayerSkill(level, xp));
        }
        return set;
    }
}