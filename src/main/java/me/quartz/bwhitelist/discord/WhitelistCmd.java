package me.quartz.bwhitelist.discord;

import me.quartz.bwhitelist.BWhitelist;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.List;

public class WhitelistCmd extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(BWhitelist.getInstance().getConfig().getString("command.command"))) {
            if(event.getChannel().getId().equals(BWhitelist.getInstance().getConfig().getString("discord.channel"))) {
                if(findRole(event.getMember())) {
                    if (!BWhitelist.getInstance().getWhitelistFile().getCustomConfig().getStringList("users").contains(event.getMember().getId())) {
                        String tagName = event.getOption(BWhitelist.getInstance().getConfig().getString("command.option.option")).getAsString();
                        BWhitelist.getInstance().getServer().getScheduler().runTask(BWhitelist.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + tagName));
                        addToConfig(event.getMember().getId());
                        event.reply(BWhitelist.getInstance().getConfig().getString("messages.added")).queue();
                    } else {
                        event.reply(BWhitelist.getInstance().getConfig().getString("messages.already-added")).queue();
                    }
                } else {
                    event.reply(BWhitelist.getInstance().getConfig().getString("messages.no-role")).queue();
                }
            } else {
                event.reply(BWhitelist.getInstance().getConfig().getString("messages.wrong-channel")).queue();
            }
        }
    }

    private void addToConfig(String discordId) {
        List<String> list = BWhitelist.getInstance().getWhitelistFile().getCustomConfig().getStringList("users");
        list.add(discordId);
        BWhitelist.getInstance().getWhitelistFile().getCustomConfig().set("users", list);
        try {
            BWhitelist.getInstance().getWhitelistFile().getCustomConfig().save(BWhitelist.getInstance().getWhitelistFile().getCustomConfigFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean findRole(Member member) {
        List<Role> roles = member.getRoles();
        return roles.stream()
                .filter(role -> role.getId().equals(BWhitelist.getInstance().getConfig().getString("discord.role")))
                .count() > 0;
    }
}