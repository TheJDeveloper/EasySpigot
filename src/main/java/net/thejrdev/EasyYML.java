package net.thejrdev;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * <h1>EasyYML</h1>
 * The EasyYML class is designed to simplify common tasks dealing with config files.
 * <p>
 * ItemStack yml format:
 *
 * (item):
 *     material: (material value)
 *     name: (display name)
 *     lore:
 *         -(lore line 1)
 *         -(lore line 2)
 *         (continue for each line of lore)
 *     enchantments:
 *         (enchantment enum name): (level)
 *
 * Example:
 * helmet:
 *     material: IRON_HELMET
 *     name: Custom Helmet
 *     lore:
 *         -Helmet made for testing purposes
 *     enchantments:
 *         PROTECTION_ENVIRONMENTAL: 2
 *
 *
 *
 */
public class EasyYML {

    private FileConfiguration config;

    /**
     * @param config The plugin configuration file
     */
    public EasyYML(@NotNull FileConfiguration config) {
        this.config = config;
    }

    /** Creates an ItemStack from the config file using specific formatting.
     *
     * @param path Path to the Item to create
     * @return new ItemStack
     * @throws IllegalArgumentException Invalid Enum name
     * @throws MissingConfigArgument Portions of the config are missing
     * @see MissingConfigArgument
     */
    public ItemStack createItemStack(@NotNull String path) throws IllegalArgumentException, MissingConfigArgument {

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

    /** Creates a HashMap of ItemStacks from config file.
     *
     * @param path Path to the collection of items
     * @param itemNames The collection of item names to create
     * @return HashMap of item names to corresponding ItemStack
     * @throws IllegalArgumentException Invalid Enum name; see createItemStack method
     * @throws MissingConfigArgument Portions of the config are missing; see createItemStack method
     * @see MissingConfigArgument
     */
    public HashMap<String, ItemStack> createItemMap(@NotNull String path, Collection<String> itemNames) throws IllegalArgumentException, MissingConfigArgument{
        HashMap<String, ItemStack> items = new HashMap<>();
        for(String s: itemNames){
            items.put(s, createItemStack(path + "." + s));
        }
        return items;
    }
}