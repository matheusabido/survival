package dev.abidux.survive.loader;

import com.google.common.reflect.ClassPath;
import dev.abidux.survive.Main;
import dev.abidux.survive.manager.PlayerStats;
import dev.abidux.survive.manager.food.FoodSystem;
import dev.abidux.survive.manager.food.ThirstSystem;
import dev.abidux.survive.manager.skills.SkillSet;
import dev.abidux.survive.scheduler.Scheduler;
import dev.abidux.survive.util.registry.CommandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;

import java.util.HashSet;

public class PluginLoader {

    private static final HashSet<Scheduler> SCHEDULERS = new HashSet<>();

    public static void loadPlugin() {
        Main.getInstance().saveDefaultConfig();
        try {
            registerClasses();
            loadSkills();
            Bukkit.getConsoleSender().sendMessage("§a[Survive] Plugin iniciado.");
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§c[Survive] Plugin não pôde ser iniciado.");
        }
    }

    public static void unloadPlugin() {
        for (Scheduler scheduler : SCHEDULERS) {
            scheduler.stop();
        }
    }

    private static void registerClasses() throws Exception {
        for (ClassPath.ClassInfo info : ClassPath.from(Main.class.getClassLoader()).getTopLevelClassesRecursive("dev.abidux.survive")) {
            Class<?> cls = Class.forName(info.getName());
            if (info.getName().endsWith(".Scheduler")) continue;

            if (Listener.class.isAssignableFrom(cls)) {
                Bukkit.getPluginManager().registerEvents((Listener) cls.newInstance(), Main.getInstance());
                Bukkit.getConsoleSender().sendMessage("§e[Survive] Evento '" + cls.getSimpleName() + "' registrado.");
            } else if (CommandExecutor.class.isAssignableFrom(cls) && cls.isAnnotationPresent(CommandRegistry.class)) {
                CommandRegistry registry = cls.getDeclaredAnnotation(CommandRegistry.class);
                Main.getInstance().getCommand(registry.value()).setExecutor((CommandExecutor) cls.newInstance());
                Bukkit.getConsoleSender().sendMessage("§e[Survive] Comando '" + cls.getSimpleName() + "' registrado.");
            } else if (Scheduler.class.isAssignableFrom(cls)) {
                Scheduler scheduler = (Scheduler) cls.newInstance();
                scheduler.start();
                SCHEDULERS.add(scheduler);
                Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), scheduler::start, 0, scheduler.ticks);
                Bukkit.getConsoleSender().sendMessage("§e[Survive] Scheduler '" + cls.getSimpleName() + "' registrado.");
            } else if (Recipe.class.isAssignableFrom(cls)) {
                Recipe recipe = (Recipe) cls.newInstance();
                Bukkit.getServer().addRecipe(recipe);
                Bukkit.getConsoleSender().sendMessage("§e[Survive] Receita '" + cls.getSimpleName() + "' registrada.");
            }
        }
    }

    private static void loadSkills() {
        if (!Main.getInstance().getConfig().isSet("skills")) return;
        Main.getInstance().getConfig().getConfigurationSection("skills").getKeys(true).forEach(player -> {
            String serializedSkills = Main.getInstance().getConfig().getString("skills." + player);
            SkillSet set = SkillSet.deserialize(serializedSkills);

            FoodSystem foodSystem;
            if (Main.getInstance().getConfig().isSet("food." + player)) {
                String serializedFoodSystem = Main.getInstance().getConfig().getString("food." + player);
                foodSystem = FoodSystem.deserialize(serializedFoodSystem);
            } else foodSystem = new FoodSystem();

            int thirst = Main.getInstance().getConfig().isSet("thirst." + player) ? Main.getInstance().getConfig().getInt("thirst." + player) : 20;
            ThirstSystem thirstSystem = new ThirstSystem(thirst);

            boolean showXp = Main.getInstance().getConfig().getBoolean("preferences." + player + ".showxp");
            PlayerStats.PLAYER_STATS.put(player, new PlayerStats(set, foodSystem, thirstSystem, showXp));
        });
    }
}