package hardbuckaroo.realfarming.plants;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SeedDropListener implements Listener {
    private final RealFarming plugin;
    public SeedDropListener(RealFarming plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onGrassBreakEvent(BlockBreakEvent event){
        if(!plugin.getConfig().getBoolean("seedSwitch")) return;

        Block block = event.getBlock();
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();

        if(Arrays.asList(Material.GRASS, Material.TALL_GRASS, Material.FERN, Material.LARGE_FERN).contains(block.getType()) && !tool.getType().equals(Material.SHEARS) && !tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
            Random rand = new Random();
            event.setDropItems(false);
            if(rand.nextInt(8) == 1) {
                Map<String, Double> map = new LinkedHashMap<>();
                CheckCropFertility checkCropFertility = new CheckCropFertility(plugin);
                double completeWeight = 0;
                for(String key : plugin.getConfig().getConfigurationSection(".plants").getKeys(false)) {
                    double fertility = checkCropFertility.checkFertility(block, Material.valueOf(key.toUpperCase()));
                    if(fertility > 0 && !Arrays.asList(Material.MELON_STEM, Material.PUMPKIN_STEM, Material.TORCHFLOWER_CROP, Material.PITCHER_CROP, Material.AZALEA, Material.FLOWERING_AZALEA).contains(Material.valueOf(key.toUpperCase()))) {
                        map.put(key, fertility);
                        completeWeight += fertility;
                    }
                }
                double avgWeight = completeWeight/(double) map.size();

                map.entrySet().removeIf(entry -> entry.getValue() < avgWeight);

                int dropAmount = 1;
                double check = Math.random() * completeWeight;
                double countWeight = 0;

                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    double value = entry.getValue();
                    countWeight += value;
                    if (countWeight >= check) {
                        int fortuneLevel = tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                        dropAmount += rand.nextInt(fortuneLevel + 1) * 2;
                        Material material = Material.valueOf(entry.getKey().toUpperCase());
                        if (material == Material.POTATOES) material = Material.POTATO;
                        else if (material == Material.SWEET_BERRY_BUSH) material = Material.SWEET_BERRIES;
                        else if (material == Material.CAVE_VINES_PLANT) material = Material.GLOW_BERRIES;
                        else if (material == Material.BAMBOO_SAPLING) material = Material.BAMBOO;
                        else if (material == Material.BEETROOTS) material = Material.BEETROOT_SEEDS;
                        else if (material == Material.WHEAT) material = Material.WHEAT_SEEDS;
                        else if (material == Material.CARROTS) material = Material.CARROT;
                        else if (material == Material.PUMPKIN) material = Material.PUMPKIN_SEEDS;
                        else if (material == Material.MELON) material = Material.MELON_SEEDS;
                        ItemStack newDrop = new ItemStack(material, dropAmount);
                        block.getWorld().dropItemNaturally(block.getLocation(), newDrop);
                        return;
                    }
                }
            }
        }
    }
}
