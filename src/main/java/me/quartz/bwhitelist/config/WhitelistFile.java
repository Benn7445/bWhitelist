package me.quartz.bwhitelist.config;

import me.quartz.bwhitelist.BWhitelist;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WhitelistFile {

    private File customConfigFile;
    private FileConfiguration customConfig;

    public WhitelistFile() {
        createCustomConfig();
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    public File getCustomConfigFile() {
        return this.customConfigFile;
    }

    private void createCustomConfig() {
        customConfigFile = new File(BWhitelist.getInstance().getDataFolder(), "whitelisted.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            BWhitelist.getInstance().saveResource("whitelisted.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
