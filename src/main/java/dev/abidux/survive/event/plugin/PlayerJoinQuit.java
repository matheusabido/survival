package dev.abidux.survive.event.plugin;

import dev.abidux.survive.Main;
import dev.abidux.survive.util.ColoredText;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuit implements Listener {

    @EventHandler
    void join(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        String message = "§8[§a+§8] §a" + event.getPlayer().getName();
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
        event.getPlayer().sendMessage(new ColoredText("{#e07726}Bem-vindo a {#c72412}Survive: Combat Update{#e07726}!").legacyText);
    }

    @EventHandler
    void quit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        String message = "§8[§c-§8] §c" + event.getPlayer().getName();
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }
}