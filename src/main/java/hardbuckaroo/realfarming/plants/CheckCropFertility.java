package hardbuckaroo.realfarming.plants;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.Material;
import org.bukkit.World;
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
        String materialString = material.toString().toLowerCase();

        if(materialString.contains("_stem")) materialString = materialString.replace("_stem","");
        if(materialString.contains("attached_")) materialString = materialString.replace("attached_","");

        if(!plugin.getConfig().contains("plants."+materialString)) return 100;

        if(plugin.getConfig().contains("plants." + materialString + ".idealTemp")) idealTemp = plugin.getConfig().getDouble("plants." + materialString + ".idealTemp");
        else idealTemp = temperature;
        if(plugin.getConfig().contains("plants." + materialString + ".idealRain")) idealRain = plugin.getConfig().getDouble("plants." + materialString + ".idealRain");
        else idealRain = rainfall;
        if(plugin.getConfig().contains("plants." + materialString + ".idealAltitude")) idealAltitude = plugin.getConfig().getDouble("plants." + materialString + ".idealAltitude");
        else idealAltitude = altitude;
        if(plugin.getConfig().contains("plants." + materialString + ".sensitivity")) sensitivity = plugin.getConfig().getDouble("plants." + materialString + ".sensitivity");
        else sensitivity = 1;

        if(Math.abs(altitude-idealAltitude) > 32)
            altitudeModifier = (Math.abs(altitude-idealAltitude) - 32)/100;

        int mod = plugin.getConfig().getInt("plantUniversalModifier");

        if(Arrays.asList(Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.NETHER_WART, Material.CAVE_VINES_PLANT, Material.SEA_PICKLE, Material.KELP_PLANT).contains(material) || block.getBiome().equals(Biome.LUSH_CAVES) || block.getWorld().getEnvironment() == World.Environment.NETHER || block.getWorld().getEnvironment() == World.Environment.THE_END)
            growChance = (1-((Math.abs(temperature-idealTemp)+Math.abs(rainfall-idealRain)+altitudeModifier)*sensitivity))*mod;
        else
            growChance = (1-((Math.abs(temperature-idealTemp)+Math.abs(rainfall-idealRain)+Math.abs(1-(block.getRelative(0,1,0).getLightFromSky()/15))+altitudeModifier)*sensitivity))*mod;

        if(growChance > mod) growChance = mod;
        if(growChance > 100) growChance = 100;

        return growChance;
    }

    public double[] checkFertilityVerbose(Block block, Material material){
        double temperature = block.getTemperature();
        double rainfall = block.getHumidity();
        double altitude = block.getY();
        double idealTemp;
        double idealRain;
        double growChance;
        double idealAltitude;
        double sensitivity;
        double altitudeModifier = 0;
        String materialString = material.toString().toLowerCase();

        if(materialString.contains("_stem")) materialString = materialString.replace("_stem","");
        if(materialString.contains("attached_")) materialString = materialString.replace("attached_","");

        if(!plugin.getConfig().contains("plants."+materialString)) return new double[]{100, 0, 0, 0, 0, 100};

        if(plugin.getConfig().contains("plants." + materialString + ".idealTemp")) idealTemp = plugin.getConfig().getDouble("plants." + materialString + ".idealTemp");
        else idealTemp = temperature;
        if(plugin.getConfig().contains("plants." + materialString + ".idealRain")) idealRain = plugin.getConfig().getDouble("plants." + materialString + ".idealRain");
        else idealRain = rainfall;
        if(plugin.getConfig().contains("plants." + materialString + ".idealAltitude")) idealAltitude = plugin.getConfig().getDouble("plants." + materialString + ".idealAltitude");
        else idealAltitude = altitude;
        if(plugin.getConfig().contains("plants." + materialString + ".sensitivity")) sensitivity = plugin.getConfig().getDouble("plants." + materialString + ".sensitivity");
        else sensitivity = 1;

        if(Math.abs(altitude-idealAltitude) > 32)
            altitudeModifier = (Math.abs(altitude-idealAltitude) - 32)/100;

        int mod = plugin.getConfig().getInt("plantUniversalModifier");

        if(Arrays.asList(Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.NETHER_WART, Material.CAVE_VINES_PLANT, Material.SEA_PICKLE, Material.KELP_PLANT).contains(material) || block.getBiome().equals(Biome.LUSH_CAVES) || block.getWorld().getEnvironment() == World.Environment.NETHER || block.getWorld().getEnvironment() == World.Environment.THE_END)
            growChance = (1-((Math.abs(temperature-idealTemp)+Math.abs(rainfall-idealRain)+altitudeModifier)*sensitivity))*mod;
        else
            growChance = (1-((Math.abs(temperature-idealTemp)+Math.abs(rainfall-idealRain)+Math.abs(1-(block.getRelative(0,1,0).getLightFromSky()/15))+altitudeModifier)*sensitivity))*mod;

        if(growChance > mod) growChance = mod;
        if(growChance > 100) growChance = 100;

        return new double[]{growChance,Math.abs(temperature-idealTemp),Math.abs(rainfall-idealRain),Math.abs(1-(block.getRelative(0,1,0).getLightFromSky()/15)),altitudeModifier,sensitivity};
    }
}
