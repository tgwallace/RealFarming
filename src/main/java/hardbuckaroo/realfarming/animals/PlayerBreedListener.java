package hardbuckaroo.realfarming.animals;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;

import java.util.Random;

public class PlayerBreedListener implements Listener {
    private final RealFarming plugin;
    public PlayerBreedListener(RealFarming plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBreedEvent(EntityEnterLoveModeEvent event){
        if(!plugin.getConfig().getBoolean("allowPlayerBreeding") || (event.getEntity() instanceof Animals && checkGrowth(event.getEntity()))){
            event.setCancelled(true);
        }
    }

    public boolean checkGrowth(Animals animal){
        Random rand = new Random();
        CheckAnimalFertility checkAnimalFertility = new CheckAnimalFertility(plugin);

        return rand.nextInt(100) > (checkAnimalFertility.checkFertility(animal));
    }
}
