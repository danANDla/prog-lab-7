package utils;


import commands.*;
import commands.types.*;
import udp.Request;
import udp.Response;
import users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class CommandsManager {
    private HashMap<String, Command> commandsList;
//    private HashMap<String, ArgumentedCommand> argumentedComandsList;
//    private HashMap<String, ArgumentedExtendedCommand> argumentedExtendedCommandList;
    private HashMap<String, ExtendedCommand> extendedCommandList;
    private HashMap<String, RichResponseCommand> richResponseCommandList;
    private IOutil io;
    private CollectionManager collectionManager;
    private DBmanager dbmanager;
    private UserManager userManager;

    public CommandsManager(IOutil io, CollectionManager collectionManager, DBmanager dbmanager) {
        this.io = io;
        this.collectionManager = collectionManager;
        this.dbmanager = dbmanager;
        this.userManager = new UserManager(dbmanager);
        fillLists();
    }

    public void fillLists() {
        commandsList = new HashMap<String, Command>();
        extendedCommandList = new HashMap<String, ExtendedCommand>();
        richResponseCommandList = new HashMap<String, RichResponseCommand>();

        commandsList.put("info", new Info(collectionManager));
        commandsList.put("sumshow", new AlbumsSum(collectionManager));
        commandsList.put("group", new Group(collectionManager));

        extendedCommandList.put("add", new Add(collectionManager));
        extendedCommandList.put("remove", new Remove(collectionManager));
        extendedCommandList.put("update", new Update(collectionManager));
        extendedCommandList.put("auth", new Auth(userManager));
        extendedCommandList.put("reg", new Register(userManager));
        extendedCommandList.put("lower", new RemoveLower(collectionManager));
        extendedCommandList.put("greater", new RemoveGreater(collectionManager));
        extendedCommandList.put("clear", new Clear(collectionManager));
        extendedCommandList.put("removebysum", new RemoveAlbums(collectionManager));

        richResponseCommandList.put("show", new Show(collectionManager));
    }

    public ArrayList<Response> executeRequest(Request request){
        Response resp = null;
        ArrayList<Response> respList = new ArrayList<>();
        String commandName = request.getCommand().toLowerCase(Locale.ROOT);
        if (extendedCommandList.containsKey(commandName)) {
            ExtendedCommand parsedCommand = extendedCommandList.get(commandName);
            resp = parsedCommand.extendedExecute(request);
        } else if (commandsList.containsKey(commandName)) {
            Command parsedCommand = commandsList.get(commandName);
            resp = parsedCommand.execute();
        } else if (richResponseCommandList.containsKey(commandName)) {
            RichResponseCommand parsedCommand = richResponseCommandList.get(commandName);
            respList = parsedCommand.richExecute();
            return respList;
        } else {
            io.printError("?????????? ?????????????? ???? ??????????????");
        }
        respList.add(resp);
        return respList;
    }

    public boolean next(Request request) {
        String commandName = request.getCommand().toLowerCase(Locale.ROOT);
        io.printText(commandName);
        return commandName.equals("next");
    }

    public boolean isRich(Request request){
        String commandName = request.getCommand().toLowerCase(Locale.ROOT);
        return richResponseCommandList.containsKey(commandName);
    }
}
