package dev.abidux.survive.event.skills.ferraria;

import dev.abidux.survive.manager.PlayerSkill;
import dev.abidux.survive.manager.SkillManager;
import dev.abidux.survive.manager.SkillSet;
import dev.abidux.survive.manager.Skills;
import dev.abidux.survive.model.skills.skill.CappedSkill;
import dev.abidux.survive.model.skills.Tool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Arrays;
import java.util.Optional;

public class CraftEvent implements Listener {

    @EventHandler
    void craft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack result = inventory.getResult();
        Tool tool = new Tool(result);
        if (tool.isTool) {
            Player player = (Player) event.getViewers().get(0);
            SkillSet set = SkillManager.get(player);
            PlayerSkill skill = set.get(Skills.FERRARIA);

            damageItemByPlayerLevel(result, skill.getLevel());
        }
    }

    private void damageItemByPlayerLevel(ItemStack item, int level) {
        Damageable meta = (Damageable) item.getItemMeta();
        float percentage = (level + 1) * .1f;
        int totalDurability = item.getType().getMaxDurability() - meta.getDamage();
        float adjustedTotalDurability = totalDurability * percentage;
        int adjustedDamage = (int) (item.getType().getMaxDurability() - adjustedTotalDurability);
        meta.setDamage(adjustedDamage);
        item.setItemMeta(meta);
    }

    @EventHandler
    void crafted(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack result = inventory.getResult();
        Tool tool = new Tool(result);
        if (tool.isTool) {
            Player player = (Player) event.getWhoClicked();
            SkillSet set = SkillManager.get(player);
            CappedSkill type = Skills.FERRARIA;
            PlayerSkill skill = set.get(type);
            if (skill.isMaxed()) return;

            int space = calculateSpaceForItem(inventory, result);
            if (space == 0) return;

            int leastAmount = calculateLeastAmount(inventory.getMatrix());
            if (leastAmount == -1) return;

            int count = Math.min(space, leastAmount);
            int earned = tool.type.xp * count;
            skill.addXp(player, earned);
        }
    }

    private int calculateSpaceForItem(Inventory inventory, ItemStack result) {
        Optional<Integer> opt = Arrays.stream(inventory.getContents()).filter(item -> item == null || item.isSimilar(result))
                .map(item -> item == null ? result.getMaxStackSize() : result.getMaxStackSize() - item.getAmount())
                .reduce(Integer::sum);
        return opt.isEmpty() ? 0 : opt.get();
    }

    private int calculateLeastAmount(ItemStack[] items) {
        Optional<Integer> leastAmount = Arrays.stream(items).filter(i -> i != null).map(i -> i.getAmount()).min((a, b) -> a < b ? -1 : 1);
        return leastAmount.isEmpty() ? -1 : leastAmount.get();
    }
}