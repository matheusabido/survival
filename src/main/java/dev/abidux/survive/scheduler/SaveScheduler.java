package dev.abidux.survive.scheduler;

import dev.abidux.survive.Main;
import dev.abidux.survive.manager.PlayerStats;

public class SaveScheduler extends Scheduler {

    public SaveScheduler() {
        super(20 * 60 * 15);
    }

    @Override
    public void start() {
        saveSkills();
        Main.getInstance().saveConfig();
    }

    @Override
    public void stop() {
        start();
    }

    private void saveSkills() {
        PlayerStats.PLAYER_STATS.forEach((player, stats) -> {
            Main.getInstance().getConfig().set("skills." + player, stats.getSkillSet().toString());
            Main.getInstance().getConfig().set("food." + player, stats.getFoodSystem().toString());
            Main.getInstance().getConfig().set("thirst." + player, stats.getThirstSystem().getThirst());
            Main.getInstance().getConfig().set("preferences." + player + ".showxp", stats.showXp);
        });
    }
}