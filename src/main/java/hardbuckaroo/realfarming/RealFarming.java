package hardbuckaroo.realfarming;

import hardbuckaroo.realfarming.animals.*;
import hardbuckaroo.realfarming.plants.CheckCropFertility;
import hardbuckaroo.realfarming.plants.CropListener;
import hardbuckaroo.realfarming.plants.GetCropFertility;
import hardbuckaroo.realfarming.plants.SeedDropListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RealFarming extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();

        if(this.getConfig().getBoolean("plantModule")) {
            Bukkit.getPluginManager().registerEvents(new CropListener(this), this);
            Bukkit.getPluginManager().registerEvents(new GetCropFertility(this), this);
            Bukkit.getPluginManager().registerEvents(new SeedDropListener(this), this);
            CheckCropFertility checkCropFertility = new CheckCropFertility(this);
        }

        if(this.getConfig().getBoolean("animalModule")) {
            Bukkit.getPluginManager().registerEvents(new GetAnimalFertility(this), this);
            Bukkit.getPluginManager().registerEvents(new AnimalListener(this), this);
            Bukkit.getPluginManager().registerEvents(new PlayerBreedListener(this), this);
            CheckAnimalFertility checkAnimalFertility = new CheckAnimalFertility(this);

            if(this.getConfig().getBoolean("autoBreed")) {
                AnimalBreedLoop breedLoop = new AnimalBreedLoop(this);
                long timer = this.getConfig().getLong("autoBreedTimer") * 1200;
                Bukkit.getServer().getScheduler().runTaskTimer(this, breedLoop::animalBreedLoop, 1200, timer);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
