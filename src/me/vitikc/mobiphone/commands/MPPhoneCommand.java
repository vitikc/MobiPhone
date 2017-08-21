package me.vitikc.mobiphone.commands;

import me.vitikc.mobiphone.MPMain;
import me.vitikc.mobiphone.MPPhonesManager;
import me.vitikc.mobiphone.calls.MPCall;
import me.vitikc.mobiphone.calls.MPCallsManager;
import me.vitikc.mobiphone.contacts.MPContactsManager;
import me.vitikc.mobiphone.messages.MPMessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MPPhoneCommand implements CommandExecutor {

    private MPContactsManager cm = MPMain.getInstance().getContactsManager(); //Contacts manager
    private MPMessagesManager mm = MPMain.getInstance().getMessagesManager(); //Messages manager
    private MPPhonesManager pm = MPMain.getInstance().getPhonesManager(); //Phones manager
    private MPCallsManager callsManager = MPMain.getInstance().getCallsManager(); //Active calls

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //TODO: Console commands: phone help, phone change, phone pay
        if (!(sender instanceof Player)){ //This plugin player based, so console can't use phone command's
            sender.sendMessage("Only player can perform this command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0){ //show help message

        } else if (args.length == 1){
            switch (args[0].toLowerCase()){
                case "end": //end current call

                    break;
                case "reject": //reject's incoming call
                    rejectCall(player);
                    break;
                case "reply": //replying to incoming call
                    replyCall(player);
                    break;
                case "help": //show's help message
                    break;
                case "number": //show's player's number
                    player.sendMessage("My number is " + pm.getNumber(player.getName()));
                    break;
                default:
                    break;
            }
        } else if (args.length == 2){
            switch (args[0].toLowerCase()){
                case "contacts":
                    if(args[1].equalsIgnoreCase("list")){ //contacts list of player stored in phone
                        String list = cm.getContacts(player.getUniqueId());
                        player.sendMessage("List of your contacts: ");
                        player.sendMessage(list);
                    }
                    break;
                case "call":
                    call(player, args[1]);
                    break;
                case "number": //number <player> - Show player's phone number
                    break;
                case "sms": //sms list - Shows sent messages
                    if (args[1].equalsIgnoreCase("list"))
                        player.sendMessage(mm.getMessages(player.getUniqueId()));
                    break;
                case "register":
                    if (pm.isRegistered(player.getName())){
                        player.sendMessage("You already registered a number!");
                        return true;
                    }
                    if (!cm.isValidNumber(args[1])){
                        player.sendMessage("Not valid number!");
                        return true;
                    }
                    if (pm.isNumberExist(args[1])){
                        player.sendMessage("Number already taken!");
                        return true;
                    }
                    pm.addPlayer(args[1], player.getName());
                    player.sendMessage("Now your phone number is " + args[1]);
                default:
                    break;
            }
        } else if (args.length == 3){
            switch (args[0].toLowerCase()){
                case "contacts": //contacts get/remove
                    if(args[1].equalsIgnoreCase("get")){ //get <contact_name> shows contact number
                        String number = cm.getNumber(player.getUniqueId(),args[2].toLowerCase());
                        if (number == null || number.isEmpty()){
                            player.sendMessage("Not found contact " + args[2]);
                            return true;
                        }
                        player.sendMessage(args[2] + " : " + number);
                    } else if (args[1].equalsIgnoreCase("remove")){ //remove <contact_name> deletes contact
                        if (!cm.isContainsContactName(player.getUniqueId(), args[2].toLowerCase())){
                            player.sendMessage("Contact " + args[2] + " not found");
                            return true;
                        }
                        cm.removeContact(player.getUniqueId(), args[2]);
                        player.sendMessage("Contact " + args[2] + " has been removed");
                    }
                    break;
                default:
                    break;
            }
        } else if (args.length == 4){
            switch (args[0].toLowerCase()){
                case "contacts":
                    if (args[1].equalsIgnoreCase("add")){ //add <contact_name> <number> add's contact
                        if (cm.isContainsContactName(player.getUniqueId(), args[2].toLowerCase())){
                            player.sendMessage("Already have contact " + args[2]);
                            return true;
                        }
                        if (!cm.isValidNumber(args[3])){
                            player.sendMessage(args[3] + " not valid for number");
                            return true;
                        }
                        if (!pm.isNumberExist(args[3])){
                            player.sendMessage("Number not found!");
                            return true;
                        }
                        if (pm.getNumber(player.getName()).equals(args[3])){
                            player.sendMessage("Can't add my number to contacts");
                            return true;
                        }
                        if (cm.isContainsContactNumber(player.getUniqueId(), args[3])){
                            player.sendMessage("Already added contact with this number: "
                                    + cm.getContact(player.getUniqueId(), args[3]));
                            return true;
                        }
                        cm.addContact(player.getUniqueId(),args[2].toLowerCase(),args[3]);
                    }
                case "sms":
                    if (args[1].equalsIgnoreCase("send"))
                        sendSms(player, args);
                    break;
                default:
                    break;
            }
        } else if (args.length > 4){ //Starting from 4th, arguments transforms to sms text
            if (!args[0].equalsIgnoreCase("sms")){
                return true;
            }
            if (!args[1].equalsIgnoreCase("send")){
                return true;
            }
            sendSms(player, args);
        }
        return true;
    }

    //Performs call to contact or number
    private void call(Player player, String s){
        boolean isNumberCall = false;
        if (cm.isValidNumber(s))
            isNumberCall = true;
        if (!isNumberCall) {
            if (!cm.isContainsContactName(player.getUniqueId(), s)) {
                player.sendMessage(s + " contact not found!");
                return;
            }
        } else {
            if (!cm.isValidNumber(s)){
                player.sendMessage(s + " number not found!");
                return;
            }
        }
        Player receiver;
        String receiverNumber;
        String playerNumber = pm.getNumber(player.getName());
        if (isNumberCall){
            receiverNumber = s;
            receiver = Bukkit.getPlayer(pm.getPlayer(receiverNumber));
        } else {
            receiverNumber = cm.getNumber(player.getUniqueId(),s);
            receiver = Bukkit.getPlayer(pm.getPlayer(receiverNumber));
        }
        if (receiver == null || !receiver.isOnline()){
            player.sendMessage(s + " phone is offline!");
            return;
        }
        if (callsManager.hasIncoming(receiverNumber)||
                callsManager.getCaller(receiverNumber).equalsIgnoreCase(playerNumber)){
            player.sendMessage("Line is busy!");
            return;
        }
        if (callsManager.contains(player.getName())){
            player.sendMessage("You already talking");
            return;
        }
        if (callsManager.contains(receiver.getName())){
            player.sendMessage("Target already talking");
            return;
        }
        MPCall<String, String> call = new MPCall<>(playerNumber,receiverNumber);
        callsManager.getIncomingCalls().add(call);
        callsManager.waitToRemove(receiverNumber);
        receiver.sendMessage("Incoming call!"); //TODO: From contact or number
    }

    private void rejectCall(Player receiver){
        String receiverNumber = pm.getNumber(receiver.getName());
        if (!callsManager.hasIncoming(receiverNumber)){
            receiver.sendMessage("You don't have incoming calls!");
            return;
        }
        String playerNumber = callsManager.getIncomingFrom(receiverNumber);
        callsManager.removeIncoming(receiverNumber);
        Player player = Bukkit.getPlayer(pm.getPlayer(playerNumber));
        player.sendMessage(receiverNumber + " rejected call!");
    }

    private void replyCall(Player receiver){
        String receiverNumber = pm.getNumber(receiver.getName());
        if (!callsManager.hasIncoming(receiverNumber)){
            receiver.sendMessage("You don't have incoming calls!");
            return;
        }
        String playerNumber = callsManager.getIncomingFrom(receiverNumber);
        callsManager.removeIncoming(receiverNumber);
        MPCall<String, String> call = new MPCall<>(playerNumber,receiverNumber);
        callsManager.getActiveCalls().add(call);
    }

    private void endCall(Player p){
        String playerNumber = pm.getNumber(p.getName());
        if (!callsManager.contains(playerNumber)){
            p.sendMessage("You don't talking at this moment");
            return;
        }
        String receiverNumber = "";
        if (callsManager.containsKey(playerNumber)){
            receiverNumber = callsManager.getReceiver(playerNumber);
        } else if (callsManager.containsValue(playerNumber)){
            receiverNumber = callsManager.getCaller(playerNumber);
        }
        callsManager.remove(playerNumber);
        Player receiver = Bukkit.getPlayer(pm.getPlayer(receiverNumber));
        String endCallMsg = "Call was ended";
        p.sendMessage(endCallMsg);
        receiver.sendMessage(endCallMsg);
    }

    //Send's sms to number or contact
    //Sms has limit of 100 characters
    //Sms can be send only to online player
    private void sendSms(Player player, String args[]){
        if (!pm.isRegistered(player.getName())){
            player.sendMessage("Register your number! /phone register <number>");
            return;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 3; i < args.length; i++){
            message.append(args[i])
                    .append(" ");
        }
        message.replace(message.length()-1,message.length(),"");
        if (message.toString().length() > 100){
            player.sendMessage("Too many characters in sms!");
            return;
        }
        boolean isCheckByNumber = false;
        if (cm.isValidNumber(args[2]))
            isCheckByNumber = true;
        if (!isCheckByNumber) {
            if (!cm.isContainsContactName(player.getUniqueId(), args[2])) {
                player.sendMessage(args[2] + " contact not found!");
                return;
            }
        } else {
            if (!cm.isValidNumber(args[2])){
                player.sendMessage(args[2] + " number not found!");
                return;
            }
        }
        Player receiver;
        if (isCheckByNumber)
            receiver = Bukkit.getPlayer(pm.getPlayer(args[2]));
        else {
            receiver = Bukkit.getPlayer(pm.getPlayer(cm.getNumber(player.getUniqueId(), args[2])));
        }
        if (receiver == null || !receiver.isOnline()){
            player.sendMessage("Contact phone is offline!");
            return;
        }
        mm.sendMessage(player.getUniqueId(), receiver.getName(), message.toString());
        mm.receiveMessage(receiver.getUniqueId(), player.getName(), message.toString());
        player.sendMessage("Sms sent to " + args[2]);
        String senderNumber = pm.getNumber(player.getName());
        if (cm.isContainsContactNumber(receiver.getUniqueId(), senderNumber))
            receiver.sendMessage("New sms from " + cm.getContact(receiver.getUniqueId(), senderNumber));
        else
            receiver.sendMessage("New sms from " + senderNumber);
    }
}
