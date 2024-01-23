package hardbuckaroo.realfarming.animals;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Breedable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.Random;

public class AnimalListener implements Listener {
    private final RealFarming plugin;

    public AnimalListener(RealFarming plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnimalBreedEvent(EntityBreedEvent event){
        if(event.getMother() instanceof Animals && checkGrowth((Animals) event.getMother())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event){
        if((event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DISPENSE_EGG) && checkGrowth((Animals) event.getEntity())){
            event.setCancelled(true);
        }
        else if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
            if(event.getEntity() instanceof Animals) {
                Animals animal = (Animals) event.getEntity();
                String animalString = animal.getType().toString().toLowerCase();
                if (plugin.getConfig().contains("animals."+animalString+".daysToAdult")) {
                    double age = -(plugin.getConfig().getDouble("animals."+animalString+".daysToAdult") * 1728000);
                    animal.setAge((int) Math.round(age));
                }
            }
        }
    }

    public boolean checkGrowth(Animals animal){
        Random rand = new Random();
        CheckAnimalFertility checkAnimalFertility = new CheckAnimalFertility(plugin);

        return rand.nextInt(100) > (checkAnimalFertility.checkFertility(animal));
    }
}
