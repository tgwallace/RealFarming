package hardbuckaroo.realfarming.plants;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.Arrays;

public class CheckCropFertility {
    private final RealFarming plugin;
    public CheckCropFertility(RealFarming plugin){
        this.plugin = plugin;
    }
    public double checkFertility(Block block, Material material){
        double temperature = block.getTemperature();
        double rainfall = block.getHumidity();
        double altitude = block.getY();
        double idealTemp;
        double idealRain;
        double growChance;
        double idealAltitude;
        double sensitivity;
        double altitudeModifier = 0;

        if(!plugin.getConfig().contains("plants."+material.toString().toLowerCase())) return 100;

        if(plugin.getConfig().contains("plants." + material.toString().toLowerCase() + ".idealTemp")) idealTemp = plugin.getConfig().getDouble("plants." + material.toString().toLowerCase() + ".idealTemp");
        else idealTemp = temperature;
        if(plugin.getConfig().contains("plants." + material.toString().toLowerCase() + ".idealRain")) idealRain = plugin.getConfig().getDouble("plants." + material.toString().toLowerCase() + ".idealRain");
        else idealRain = rainfall;
        if(plugin.getConfig().contains("plants." + material.toString().toLowerCase() + ".idealAltitude")) idealAltitude = plugin.getConfig().getDouble("plants." + material.toString().toLowerCase() + ".idealAltitude");
        else idealAltitude = altitude;
        if(plugin.getConfig().contains("plants." + material.toString().toLowerCase() + ".sensitivity")) sensitivity = plugin.getConfig().getDouble("plants." + material.toString().toLowerCase() + ".sensitivity");
        else sensitivity = 1;

        if(Math.abs(altitude-idealAltitude) > 32)
            altitudeModifier = (Math.abs(altitude-idealAltitude) - 32)/100;

        if(Arrays.asList(Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.NETHER_WART, Material.CAVE_VINES_PLANT, Material.SEA_PICKLE, Material.KELP_PLANT).contains(material) || block.getBiome().equals(Biome.LUSH_CAVES))
            growChance = (1-((Math.abs(temperature-idealTemp)+Math.abs(rainfall-idealRain)+altitudeModifier)*sensitivity))*100;
        else
            growChance = (1-((Math.abs(temperature-idealTemp)+Math.abs(rainfall-idealRain)+Math.abs(1-(block.getRelative(0,1,0).getLightFromSky()/15))+altitudeModifier)*sensitivity))*100;

        if(growChance > 100) growChance = 100;

        return growChance;
    }
}
