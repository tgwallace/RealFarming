package hardbuckaroo.realfarming.animals;

import hardbuckaroo.realfarming.RealFarming;
import hardbuckaroo.realfarming.animals.CheckAnimalFertility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.HorseInventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.round;

public class GetAnimalFertility implements Listener {
    private final RealFarming plugin;
    public GetAnimalFertility(RealFarming plugin){
        this.plugin = plugin;
    }

    ArrayList<Player> players = new ArrayList<>();
    @EventHandler
    public void onEntityClickEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(players.contains(player)) return;

        String animalString = event.getRightClicked().toString().toLowerCase().replace("craft","");
        if(animalString.contains("{")) {
            animalString = animalString.substring(0, animalString.indexOf("{"));
        }
        if(!(event.getRightClicked() instanceof Animals) || !plugin.getConfig().contains("animals."+animalString)) return;

        Animals animal = (Animals) event.getRightClicked();

        CheckAnimalFertility checkAnimalFertility = new CheckAnimalFertility(plugin);
        double growChance = checkAnimalFertility.checkFertility(animal);

        if(player.isSneaking()) {
            double[] fertility = checkAnimalFertility.checkFertilityVerbose(animal);
            DecimalFormat format = new DecimalFormat("#0.00");
            player.sendRawMessage("Raw breed rate: " + format.format(fertility[0]) + "%");
            player.sendRawMessage("Temperature Penalty: " + format.format(fertility[1]*100) + "%");
            player.sendRawMessage("Humidity Penalty: " + format.format(fertility[2]*100) + "%");
            player.sendRawMessage("Altitude Penalty: " + format.format(fertility[3]*100) + "%");
            player.sendRawMessage("Density Penalty: "+format.format(fertility[4]*100) + "%");
            player.sendRawMessage("Penalty Modifier: "+format.format(fertility[5]*100) + "%");
            player.sendRawMessage("Universal Modifier: "+plugin.getConfig().getInt("animalUniversalModifier") + "%");
        } else {
            if (growChance <=-100) player.sendRawMessage(animal.getType().toString() + " cannot survive here!");
            else if(growChance<=0) player.sendRawMessage(animal.getType().toString()+" will not breed here.");
            else player.sendRawMessage("Breeding rate for "+animal.getType().toString()+" at this location is "+ round(growChance) +"%.");
        }

        players.add(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> players.remove(player),20);
    }
}
