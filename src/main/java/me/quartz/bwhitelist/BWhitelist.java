package me.quartz.bwhitelist;

import me.quartz.bwhitelist.config.WhitelistFile;
import me.quartz.bwhitelist.discord.WhitelistCmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class BWhitelist extends JavaPlugin {

    private static BWhitelist instance;
    private WhitelistFile whitelistFile;

    public static BWhitelist getInstance() { return instance; }
    public WhitelistFile getWhitelistFile() { return whitelistFile; }

    @Override
    public void onEnable() {
        instance = this;
        createFiles();
        if(!getConfig().getString(("discord.token")).equals("")) runBot();
        else Bukkit.getLogger().log(Level.SEVERE, "Discord Token Not Found");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void createFiles() {
        saveDefaultConfig();
        whitelistFile = new WhitelistFile();
    }

    private void runBot() {
        JDA jda = JDABuilder.createDefault(getConfig().getString("discord.token")).build();
        jda.getPresence().setActivity(Activity.playing(getConfig().getString("discord.activity")));
        jda.updateCommands().addCommands(Commands.slash(getConfig().getString("command.command"), getConfig().getString("command.description")).addOption(OptionType.STRING, getConfig().getString("command.option.option"), getConfig().getString("command.option.description"), true, true)).queue();
        jda.addEventListener(new WhitelistCmd());
    }
}
