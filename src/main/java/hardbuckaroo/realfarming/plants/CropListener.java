package hardbuckaroo.realfarming.plants;

import hardbuckaroo.realfarming.RealFarming;
import hardbuckaroo.realfarming.plants.CheckCropFertility;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.Random;

public class CropListener implements Listener {
    private final RealFarming plugin;
    public CropListener(RealFarming plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onCropGrowEvent(BlockGrowEvent event){
        if(checkGrowth(event.getNewState().getBlock())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCropSpreadEvent(BlockSpreadEvent event){
        if(checkGrowth(event.getSource())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent event){
        if(checkGrowth(event.getLocation().getBlock())){
            event.setCancelled(true);
        }
    }

    public boolean checkGrowth(Block block){
        Random rand = new Random();
        CheckCropFertility checkCropFertility = new CheckCropFertility(plugin);

        return rand.nextInt(100) > (checkCropFertility.checkFertility(block, block.getType()));
    }
}
