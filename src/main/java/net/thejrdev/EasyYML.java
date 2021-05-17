package net.thejrdev;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class EasyYML {

    private FileConfiguration config;

    public EasyYML(@NotNull FileConfiguration config) {
        this.config = config;
    }

    /** Creates an ItemStack from the config file
     *
     * @param path Path to the ItemStack
     * @return new ItemStack
     * @throws IllegalArgumentException
     * @throws NullPointerException
     * @throws MissingConfigArgument
     */
    public ItemStack createItemStack(@NotNull String path) throws IllegalArgumentException, NullPointerException, MissingConfigArgument {

        ConfigurationSection section = config.getConfigurationSection(path);

        Set<String> keys = section.getKeys(false);
        if(!keys.contains("material")){
            throw new MissingConfigArgument("\"material\" is missing from the configuration file");
        }
        Material material = Material.valueOf(section.getString("material"));
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();

        if(keys.contains("name")){
            meta.setDisplayName(section.getString("name"));
        }

        if(keys.contains("count")){
            item.setAmount(section.getInt("amount"));
        }


        if(keys.contains("lore")){
            meta.setLore(section.getStringList("lore"));
        }

        if(keys.contains("enchantments")){
            ConfigurationSection enchantSection = section.getConfigurationSection("enchantments");
            Set<String> enchants = enchantSection.getKeys(false);
            for(String s: enchants){
                Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(s));
                meta.addEnchant(ench, enchantSection.getInt(s), false);


            }
        }

        item.setItemMeta(meta);
        return item;
    }




}

/*

item:
    material: <material value>
    name: <display name>
    lore:
        -<lore line 1>
        -<lore line 2>
        (continue for each line of lore)
    enchantments:
        <enchantment enum name>: <level>


 */