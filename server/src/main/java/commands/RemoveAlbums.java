package commands;

import commands.types.ExtendedCommand;
import udp.Request;
import udp.Response;
import utils.CollectionManager;

import java.util.Locale;

public class RemoveAlbums implements ExtendedCommand {
    private int albumsCount;
    private String login;
    private CollectionManager collectionManager;

    public RemoveAlbums(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public boolean parseArgs(String[] command) {
        if (command.length < 1) {
            System.out.println("введено недостаточно аргументов");
            return false;
        } else {
            try {
                albumsCount = Integer.parseInt(command[0]);
            } catch (NumberFormatException e) {
                System.out.println("неправильный формат аргумента");
                return false;
            }
        }
        return true;
    }

    @Override
    public Response extendedExecute(Request request) {
        this.login = request.getUser().getLogin();
        String[] commandArgs = request.getArgs().trim().toLowerCase(Locale.ROOT).split("\\s+");
        if(parseArgs(commandArgs)){
            return execute();
        }
        return null;
    }

    @Override
    public Response execute() {
        CommandStatus res = collectionManager.removeByAlbumsCount(login, albumsCount);
        return new Response("update", res.getDescription(), null);
    }
}
