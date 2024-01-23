package hardbuckaroo.realfarming.animals;

import hardbuckaroo.realfarming.RealFarming;
import org.bukkit.Location;
import org.bukkit.entity.Animals;

public class CheckAnimalFertility {
    private final RealFarming plugin;
    public CheckAnimalFertility(RealFarming plugin){
        this.plugin = plugin;
    }
    public double checkFertility(Animals animal){
        Location location = animal.getLocation();
        double temperature = location.getBlock().getTemperature();
        double rainfall = location.getBlock().getHumidity();
        double altitude = location.getBlock().getY();
        double density = location.getWorld().getNearbyEntities(location, 30, 30, 30).size();
        double idealTemp;
        double idealRain;
        double maxDensity;
        double growChance;
        double idealAltitude;
        double sensitivity;
        double altitudeModifier = 0;
        double densityModifier = 0;

        String animalString = animal.toString().toLowerCase().replace("craft","");

        if(!plugin.getConfig().contains("animals."+animalString)) return 100;

        if(plugin.getConfig().contains("animals." +animalString + ".idealTemp")) idealTemp = plugin.getConfig().getDouble("animals." +animalString + ".idealTemp");
        else idealTemp = temperature;
        if(plugin.getConfig().contains("animals." +animalString + ".idealRain")) idealRain = plugin.getConfig().getDouble("animals." +animalString + ".idealRain");
        else idealRain = rainfall;
        if(plugin.getConfig().contains("animals." +animalString + ".idealAltitude")) idealAltitude = plugin.getConfig().getDouble("animals." +animalString + ".idealAltitude");
        else idealAltitude = altitude;
        if(plugin.getConfig().contains("animals." +animalString + ".maxDensity")) maxDensity = plugin.getConfig().getDouble("animals." +animalString + ".maxDensity");
        else maxDensity = density;
        if(plugin.getConfig().contains("animals." +animalString + ".sensitivity")) sensitivity = plugin.getConfig().getDouble("animals." +animalString + ".sensitivity");
        else sensitivity = 1;

        if(Math.abs(altitude-idealAltitude) > 32) {
            altitudeModifier = (Math.abs(altitude - idealAltitude) - 32) / 100;
        }

        if(density > maxDensity){
            densityModifier = (density-maxDensity)*0.05;
        }

        growChance = (1-((Math.abs(temperature-idealTemp)+Math.abs(rainfall-idealRain)+altitudeModifier+densityModifier)*sensitivity))*25;

        if(growChance > 100) growChance = 100;

        return growChance;
    }
}
