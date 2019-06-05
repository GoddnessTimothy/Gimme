package timsmcplugin.GiveMe.TimsCustomCommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import timsmcplugin.GiveMe.GimmeExceptions.ForbiddenPlayerRequestItem;

//ONLY the Main class needs to extends JavaPlugin! and ONLY ONE class can extend JavaPlugin.
//https://bukkit.org/threads/pluginalreadyinitialized-exception.214917/

/*
    Simple plugin that grants user item they request.
    Use: /giveme <item-name> [amount]; where amount is optional
    Limits each request to 5 (allow flexibility for more common/lower value items like dirt)
    Handles all edge cases
        To Do:
            Create a class system
                Increase lvl of grants per level
            Ban Rare items
            Ban destructive items
            Grant frequent players rewards for relogging in
            Give new players a basic starter package
 */
public class TimsCustomCommands implements CommandExecutor {
    public void heal(CommandSender sender, String[] args) {
        //Implement this

    }
    public void giveMeItem(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (sender != null ) {
            try {
                //Save the item user requested for
                Material mat = Material.valueOf(args[0].toUpperCase());
                //Forbid TNT from being requested.
                if (mat.equals(Material.TNT)) throw new ForbiddenPlayerRequestItem();
                if (args.length == 1) {
                    //Give player 1 block by default.
                    player.getInventory().addItem(new ItemStack(mat, 1));
                    player.sendMessage(ChatColor.GREEN + "You have been 1 granted " + mat);
                    //If the player provides item_name and amount.
                } else if (args.length == 2) {
                    int amount = Integer.parseInt(args[1]);
                    if(amount >= 0 && amount <= 5) {
                        player.getInventory().addItem(new ItemStack(mat, amount));
                        player.sendMessage(ChatColor.GREEN + "You have been granted " + amount + " " + mat);
                    } else {
                        player.sendMessage(ChatColor.RED + "You are only able to request for 5 items at a time.");
                    }
                } else {
                    throw new ArrayIndexOutOfBoundsException();
                }
                //If the player provides an invalid arguement (i.e: a string for a int)
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "You must provide VALID arguments!");
                //If player requests for an item that doesn't exist.
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "PROVIDE VALID ITEM");
                //If the player doesn't provide ANY or an invalid number of arguements...
            } catch (ArrayIndexOutOfBoundsException e) {
                player.sendMessage(ChatColor.RED + "Invalid Use: use /giveme <item-name> [amount]");
                //If the player tries to request for a forbidden item...
            } catch (ForbiddenPlayerRequestItem e) {
                player.sendMessage(ChatColor.RED + "You are unable to request for TNT");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You must be a Player to send commands");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        giveMeItem(sender, args);
        return true;
    }
}
