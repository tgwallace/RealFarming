package hardbuckaroo.realfarming.animals;

import hardbuckaroo.realfarming.RealFarming;
import hardbuckaroo.realfarming.animals.CheckAnimalFertility;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.HorseInventory;

import java.util.Arrays;

import static java.lang.Math.round;

public class GetAnimalFertility implements Listener {
    private final RealFarming plugin;
    public GetAnimalFertility(RealFarming plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityClickEvent(PlayerInteractEntityEvent event) {
        if(event.getHand() != EquipmentSlot.OFF_HAND) return;

        String animalString = event.getRightClicked().toString().toLowerCase().replace("craft","");
        if(!(event.getRightClicked() instanceof Animals) || !plugin.getConfig().contains("animals."+animalString)) return;

        Player player = event.getPlayer();
        Animals animal = (Animals) event.getRightClicked();

        CheckAnimalFertility checkAnimalFertility = new CheckAnimalFertility(plugin);
        double growChance = checkAnimalFertility.checkFertility(animal);

        if (growChance <=-100) player.sendRawMessage(animal.getType().toString() + " cannot survive here!");
        else if(growChance<=0) player.sendRawMessage(animal.getType().toString()+" will not breed here.");
        else player.sendRawMessage("Breeding rate for "+animal.getType().toString()+" at this location is "+ round(growChance)*4 +"%.");
    }

    @EventHandler
    public void onChestOpenEvent(InventoryOpenEvent event){
        if(event.getInventory().getHolder() instanceof Animals){
            Player player = (Player) event.getPlayer();
            Animals horse = (Animals) event.getInventory().getHolder();

            CheckAnimalFertility checkAnimalFertility = new CheckAnimalFertility(plugin);
            double growChance = checkAnimalFertility.checkFertility(horse);

            if (growChance <=-100) player.sendRawMessage(horse.getType().toString() + " cannot survive here!");
            else if(growChance<=0) player.sendRawMessage(horse.getType().toString()+" will not breed here.");
            else player.sendRawMessage("Breeding rate for "+horse.getType().toString()+" at this location is "+ round(growChance) +"%.");
        }
    }
}
