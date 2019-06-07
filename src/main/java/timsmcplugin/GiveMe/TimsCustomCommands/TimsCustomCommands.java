package timsmcplugin.GiveMe.TimsCustomCommands;

import org.bukkit.Bukkit;
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
public class TimsCustomCommands implements CommandExecutor {
    /*
        Heals player back to full health.
        Usage: /heal <player-name>
     */
    public void heal(CommandSender sender, String[] args) {
        double healthTillFull = 0.0;
        Player player = (Player) sender;
        if(sender instanceof Player) {
            try {
                player = Bukkit.getServer().getPlayer(args[0]);
                if(player != null) {
                    if (args.length == 1) {
                        //If player can be healed...
                        if (player.getHealth() < 20.0) {
                            //Calculate the amounnt health to add in order to restore to full HP.
                            healthTillFull = 20.0 - player.getHealth();
                            player.setHealth(player.getHealth() + healthTillFull);
                            player.sendMessage(ChatColor.GREEN + "You have been restored back to full HP.");
                        } else {
                            player.sendMessage(ChatColor.RED + player.getName() + " is already at full HP.");
                        }
                    } else {
                        //If player enters too many arguements...
                        throw new ArrayIndexOutOfBoundsException();
                    }
                } else {
                    //If player does not exist....
                    player = (Player) sender;
                    player.sendMessage(ChatColor.RED + "Player must exist or be online.");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                player.sendMessage(ChatColor.RED + "usage: /heal <player-name>");
            }
        }
    }

    /*
        Simple plugin that grants user item they request.
        Use: /giveme <player-name> <item-name> [amount]; where amount is optional
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
    public void giveMeItem(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {
            try {
                //Get player and only grant item if they exist or are online
                player = Bukkit.getServer().getPlayer(args[0]);
                if(player != null) {
                    //Save the item user requested for
                    Material mat = Material.valueOf(args[1].toUpperCase());
                    //Forbid TNT from being requested.
                    if (mat.equals(Material.TNT)) throw new ForbiddenPlayerRequestItem();
                    //If player provides <player-name> <item-name>
                    if (args.length == 2) {
                        //Give player 1 block by default.
                        player.getInventory().addItem(new ItemStack(mat, 1));
                        player.sendMessage(ChatColor.GREEN + "You have been 1 granted " + mat);
                        //If the player provides <player-name> <item-name> [amount].
                    } else if (args.length == 3) {
                        //Get the amount and store it.
                        int amount = Integer.parseInt(args[2]);
                        if (amount >= 0 && amount <= 5) {
                            player.getInventory().addItem(new ItemStack(mat, amount));
                            player.sendMessage(ChatColor.GREEN + "You have been granted " + amount + " " + mat);
                        } else {
                            player.sendMessage(ChatColor.RED + "You are only able to request for 5 items at a time.");
                        }
                    } else {
                        player = (Player) sender;
                        throw new ArrayIndexOutOfBoundsException();
                    }
                } else {
                    player = (Player) sender;
                    player.sendMessage(ChatColor.RED + "Player must exist or be online.");
                }
                //If the player provides an invalid arguement (i.e: a string for a int)
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "You must provide VALID arguments!");
                //If player requests for an item that doesn't exist.
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "PROVIDE VALID ITEM");
                //If the player doesn't provide ANY or an invalid number of arguements...
            } catch (ArrayIndexOutOfBoundsException e) {
                player.sendMessage(ChatColor.RED + "Invalid Use: use /giveme <player-name> <item-name> [amount]");
                //If the player tries to request for a forbidden item...
            } catch (ForbiddenPlayerRequestItem e) {
                player.sendMessage(ChatColor.RED + "You are unable to request for TNT");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You must be a Player to send commands");
        }
    }

    //This is where you define your commands.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       if(command.getName().equalsIgnoreCase("give"))
           giveMeItem(sender, args);
       else if(command.getName().equalsIgnoreCase("heal"))
           heal(sender,args);
       return true;
    }
}
