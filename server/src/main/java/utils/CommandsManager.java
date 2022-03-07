package utils;


import commands.Add;
import commands.Info;
import commands.types.ArgumentedCommand;
import commands.types.ArgumentedExtendedCommand;
import commands.types.Command;
import commands.types.ExtendedCommand;
import udp.Request;
import udp.Response;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Locale;

public class CommandsManager {
    private HashMap<String, Command> commandsList;
    private HashMap<String, ArgumentedCommand> argumentedComandsList;
    private HashMap<String, ArgumentedExtendedCommand> argumentedExtendedCommandList;
    private HashMap<String, ExtendedCommand> extendedCommandList;
    private IOutil io;
    CollectionManager collectionManager;
    DBmanager dbmanager;

    ArrayDeque<String> history;

    public CommandsManager(IOutil io, CollectionManager collectionManager, DBmanager dbmanager) {
        this.io = io;
        this.collectionManager = collectionManager;
        this.dbmanager = dbmanager;
        history = new ArrayDeque<String>();
        fillLists();
    }

    public void fillLists(){
        commandsList = new HashMap<String, Command>();
        argumentedComandsList = new HashMap<String, ArgumentedCommand>();
        extendedCommandList = new HashMap<String, ExtendedCommand>();
        argumentedExtendedCommandList = new HashMap<String, ArgumentedExtendedCommand>();

        commandsList.put("info", new Info(collectionManager, dbmanager));

        extendedCommandList.put("add", new Add(collectionManager, dbmanager));
    }

    public Response executeRequest(Request request){
        Response resp = null;
        String commandName = request.getCommand().toLowerCase(Locale.ROOT);
        if(extendedCommandList.containsKey(commandName)){
            ExtendedCommand parsedCommand = extendedCommandList.get(commandName);
            resp = parsedCommand.extendedExecute(request.getMusicBand());

            history.addFirst(commandName);
            if(history.size() > 14){
                history.removeLast();
            }
        }
        else if(argumentedExtendedCommandList.containsKey(commandName)){
            ArgumentedExtendedCommand parsedCommand = argumentedExtendedCommandList.get(commandName);
            String[] commandArgs = request.getArgs().trim().toLowerCase(Locale.ROOT).split("\\s+");
            if(parsedCommand.parseArgs(commandArgs)) resp = parsedCommand.extendedExecute(request.getMusicBand());

            history.addFirst(commandName);
            if(history.size() > 14){
                history.removeLast();
            }
        }
        else if(argumentedComandsList.containsKey(commandName)){
            ArgumentedCommand parsedCommand = argumentedComandsList.get(commandName);
            String[] commandArgs = request.getArgs().trim().toLowerCase(Locale.ROOT).split("\\s+");
            if (parsedCommand.parseArgs(commandArgs)) resp = parsedCommand.execute();

            history.addFirst(commandName);
            if(history.size() > 14){
                history.removeLast();
            }
        }
        else if(commandsList.containsKey(commandName)){
            Command parsedCommand = commandsList.get(commandName);
            resp = parsedCommand.execute();

            history.addFirst(commandName);
            if(history.size() > 14){
                history.removeLast();
            }
        }
        else{
            io.printError("Такой команды не найдено");
        }
        return resp;
    }
}
