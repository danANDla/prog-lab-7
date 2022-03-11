package utils;


import commands.*;
import commands.types.*;
import udp.Request;
import udp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CommandsManager {
    private HashMap<String, Command> commandsList;
    private HashMap<String, ArgumentedCommand> argumentedComandsList;
    private HashMap<String, ArgumentedExtendedCommand> argumentedExtendedCommandList;
    private HashMap<String, ExtendedCommand> extendedCommandList;
    private HashMap<String, RichResponseCommand> richResponseCommandList;
    private IOutil io;
    CollectionManager collectionManager;
    DBmanager dbmanager;

    public CommandsManager(IOutil io, CollectionManager collectionManager, DBmanager dbmanager) {
        this.io = io;
        this.collectionManager = collectionManager;
        this.dbmanager = dbmanager;
        fillLists();
    }

    public void fillLists() {
        commandsList = new HashMap<String, Command>();
        argumentedComandsList = new HashMap<String, ArgumentedCommand>();
        extendedCommandList = new HashMap<String, ExtendedCommand>();
        argumentedExtendedCommandList = new HashMap<String, ArgumentedExtendedCommand>();
        richResponseCommandList = new HashMap<String, RichResponseCommand>();

        commandsList.put("info", new Info(dbmanager));
        commandsList.put("clear", new Clear(dbmanager));

        argumentedComandsList.put("remove", new Remove(dbmanager));

        extendedCommandList.put("add", new Add(dbmanager));

        argumentedExtendedCommandList.put("update", new Update(dbmanager));

        richResponseCommandList.put("show", new Show(dbmanager));
    }

    public ArrayList<Response> executeRequest(Request request) {
        Response resp = null;
        ArrayList<Response> respList = new ArrayList<>();
        String commandName = request.getCommand().toLowerCase(Locale.ROOT);
        if (extendedCommandList.containsKey(commandName)) {
            ExtendedCommand parsedCommand = extendedCommandList.get(commandName);
            resp = parsedCommand.extendedExecute(request.getMusicBand());
        } else if (argumentedExtendedCommandList.containsKey(commandName)) {
            ArgumentedExtendedCommand parsedCommand = argumentedExtendedCommandList.get(commandName);
            String[] commandArgs = request.getArgs().trim().toLowerCase(Locale.ROOT).split("\\s+");
            if (parsedCommand.parseArgs(commandArgs)) resp = parsedCommand.extendedExecute(request.getMusicBand());
        } else if (argumentedComandsList.containsKey(commandName)) {
            ArgumentedCommand parsedCommand = argumentedComandsList.get(commandName);
            String[] commandArgs = request.getArgs().trim().toLowerCase(Locale.ROOT).split("\\s+");
            if (parsedCommand.parseArgs(commandArgs)) resp = parsedCommand.execute();
        } else if (commandsList.containsKey(commandName)) {
            Command parsedCommand = commandsList.get(commandName);
            resp = parsedCommand.execute();
        } else if (richResponseCommandList.containsKey(commandName)) {
            RichResponseCommand parsedCommand = richResponseCommandList.get(commandName);
            respList = parsedCommand.richExecute();
            return respList;
        } else {
            io.printError("Такой команды не найдено");
        }
        respList.add(resp);
        return respList;
    }

    public boolean next(Request request) {
        String commandName = request.getCommand().toLowerCase(Locale.ROOT);
        io.printText(commandName);
        return commandName.equals("next");
    }
}
