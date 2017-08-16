package me.vitikc.mobiphone.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MPPhoneCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only player can perform this command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0){

        } else if (args.length == 1){
            switch (args[0].toLowerCase()){
                case "reject": //reject's incoming call
                    break;
                case "reply": //replying to incoming call
                    break;
                case "help": //show's help message
                    break;
                case "number": //show's player's number
                    break;
                default:
                    break;
            }
        } else if (args.length == 2){
            switch (args[0].toLowerCase()){
                case "contacts":
                    if(args[1].equalsIgnoreCase("list")){ //contacts list of player stored in phone

                    }
                    break;
                case "number": //number <player> - Show player's phone number
                    break;
                case "sms": //sms list - Shows sent messages
                    break;
                default:
                    break;
            }
        } else if (args.length == 3){
            switch (args[0].toLowerCase()){
                case "contacts": //contacts get/remove
                    if(args[1].equalsIgnoreCase("get")){ //get <contact_name> shows contact number

                    } else if (args[1].equalsIgnoreCase("remove")){ //remove <contact_name> deletes contact

                    }
                    break;
                default:
                    break;
            }
        } else if (args.length == 4){
            switch (args[0].toLowerCase()){
                case "contacts":
                    if (args[1].equalsIgnoreCase("add")){ //add <contact_name> <number> add's contact

                    }
                    break;
                case "sms":
                    if (args[1].equalsIgnoreCase("send")){ //send <contact_name> <text> send sms to contact

                    }
                default:
                    break;
            }
        }
        return true;
    }
}
