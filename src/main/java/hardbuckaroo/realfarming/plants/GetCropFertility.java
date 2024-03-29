package hardbuckaroo.realfarming.plants;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.text.DecimalFormat;

import static java.lang.Math.round;

public class GetCropFertility implements Listener {
    private final RealFarming plugin;
    public GetCropFertility(RealFarming plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock().getRelative(0,1,0);
        Material inHand = player.getInventory().getItemInMainHand().getType();
        if(event.getAction() == Action.LEFT_CLICK_BLOCK && player.isSneaking()){
            if(inHand == Material.WHEAT_SEEDS) inHand=Material.WHEAT;
            else if(inHand == Material.BEETROOT_SEEDS | inHand==Material.BEETROOT) inHand=Material.BEETROOTS;
            else if(inHand == Material.CARROT) inHand=Material.CARROTS;
            else if(inHand == Material.POTATO | inHand == Material.BAKED_POTATO | inHand == Material.POISONOUS_POTATO) inHand=Material.POTATOES;
            else if(inHand == Material.MELON_SEEDS | inHand == Material.MELON_SLICE) inHand=Material.MELON;
            else if(inHand == Material.PUMPKIN_SEEDS | inHand == Material.CARVED_PUMPKIN) inHand=Material.PUMPKIN;
            else if(inHand == Material.BAMBOO) inHand=Material.BAMBOO_SAPLING;
            else if(inHand == Material.COCOA_BEANS) inHand=Material.COCOA;
            else if(inHand == Material.SUGAR) inHand=Material.SUGAR_CANE;
            else if(inHand == Material.SWEET_BERRIES) inHand=Material.SWEET_BERRY_BUSH;
            else if(inHand == Material.KELP | inHand == Material.DRIED_KELP | inHand == Material.DRIED_KELP_BLOCK) inHand=Material.KELP_PLANT;
            else if(inHand == Material.CAVE_VINES | inHand == Material.GLOW_BERRIES) inHand=Material.CAVE_VINES_PLANT;
            else if(inHand == Material.RED_MUSHROOM_BLOCK) inHand=Material.RED_MUSHROOM;
            else if(inHand == Material.BROWN_MUSHROOM_BLOCK) inHand=Material.BROWN_MUSHROOM;
            else if(inHand == Material.TORCHFLOWER | inHand == Material.TORCHFLOWER_SEEDS) inHand = Material.TORCHFLOWER_CROP;
            else if(inHand == Material.PITCHER_POD | inHand == Material.PITCHER_PLANT) inHand = Material.PITCHER_CROP;

            if(plugin.getConfig().get("plants."+inHand.toString().toLowerCase()) == null) return;

            CheckCropFertility checkCropFertility = new CheckCropFertility(plugin);
            double growChance = checkCropFertility.checkFertility(block, inHand);

            if(growChance<=0) player.sendRawMessage(inHand+" will not grow here.");
            else player.sendRawMessage("Growth rate for "+inHand+" at this location is "+ round(growChance) +"%.");
        }
    }

    @EventHandler
    public void onPlayerInteractWithCropEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.OFF_HAND){
            String materialString = block.getType().toString().toLowerCase();
            if(materialString.contains("_stem")) materialString = materialString.replace("_stem","");
            if(materialString.contains("attached_")) materialString = materialString.replace("attached_","");

            if (!plugin.getConfig().contains("plants." + materialString)) return;
            CheckCropFertility checkCropFertility = new CheckCropFertility(plugin);
            if(!player.isSneaking()) {
                double growChance = checkCropFertility.checkFertility(block, block.getType());
                if (growChance <= 0) player.sendRawMessage(block.getType() + " will not grow here.");
                else player.sendRawMessage("Growth rate for " + block.getType() + " at this location is " + round(growChance) + "%.");
            } else {
                double[] fertility = checkCropFertility.checkFertilityVerbose(block,block.getType());
                DecimalFormat format = new DecimalFormat("#0.00");
                player.sendRawMessage("Growth Rate: " + format.format(fertility[0]) + "%");
                player.sendRawMessage("Temperature Penalty: " + format.format(fertility[1]*100) + "%");
                player.sendRawMessage("Humidity Penalty: " + format.format(fertility[2]*100) + "%");
                player.sendRawMessage("Sunlight Penalty: " + format.format(fertility[3]*100) + "%");
                player.sendRawMessage("Altitude Penalty: "+format.format(fertility[4]*100) + "%");
                player.sendRawMessage("Sensitivity: "+fertility[5]);
                player.sendRawMessage("Universal Modifier: "+plugin.getConfig().getInt("plantUniversalModifier") + "%");
            }
        }
    }
}
