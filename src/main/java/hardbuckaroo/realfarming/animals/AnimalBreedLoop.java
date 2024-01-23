package hardbuckaroo.realfarming.animals;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class AnimalBreedLoop implements Listener {
    private final RealFarming plugin;
    public AnimalBreedLoop(RealFarming plugin){
        this.plugin = plugin;
    }

    public void animalBreedLoop(){
        Random rand = new Random();
        CheckAnimalFertility checkAnimalFertility = new CheckAnimalFertility(plugin);

        for(World world : Bukkit.getWorlds()) {
            for(Animals animal : world.getEntitiesByClass(Animals.class)) {
                String animalString = animal.getType().toString().toLowerCase();
                if (plugin.getConfig().contains("animals."+animalString)) {
                    double fertility = checkAnimalFertility.checkFertility(animal);
                    if(rand.nextInt(100) <= fertility){
                        Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("RealFarming"), () -> animal.setLoveModeTicks(1200), rand.nextInt(1200));
                    }
                    if (fertility <= -100) {
                        double deathChance = (fertility + 100)*-1;
                        if(rand.nextInt(100) <= deathChance) {
                            Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("RealFarming"), () -> animal.damage(animal.getHealth()), rand.nextInt(1200));
                        }
                    }
                    if (plugin.getConfig().contains("animals."+animalString+".lifespan")) {
                        double maxAge = plugin.getConfig().getDouble("animals."+animalString+".lifespan") * 1728000;

                        if(animal.getAge() > maxAge) {
                            if(rand.nextInt(100)+1 <= (100-fertility)) {
                                Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("RealFarming"), () -> animal.damage(animal.getHealth()), rand.nextInt(1200));
                            }
                        }
                        else if(animal.getAge() > maxAge * 0.80) {
                            PotionEffect potionPoison = new PotionEffect(PotionEffectType.POISON,PotionEffect.INFINITE_DURATION,1);
                            PotionEffect potionSlow = new PotionEffect(PotionEffectType.SLOW,PotionEffect.INFINITE_DURATION,1);
                            int random = rand.nextInt(1200);
                            Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("RealFarming"), () -> animal.addPotionEffect(potionPoison), random);
                            Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("RealFarming"), () -> animal.addPotionEffect(potionSlow), random);
                        }
                    }
                }
            }
        }
    }
}
