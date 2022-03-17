package commands;

import collection.MusicBand;
import commands.types.ExtendedCommand;
import udp.Request;
import udp.Response;
import utils.CollectionManager;

import java.util.Locale;

public class Update implements ExtendedCommand {
    private CollectionManager collectionManager;
    private int bandId;
    private MusicBand band;
    private String login;

    public Update(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public boolean parseArgs(String[] command) {
        if (command.length < 1) {
            System.out.println("введено недостаточно аргументов");
            return false;
        } else {
            try {
                bandId = Integer.parseInt(command[0]);
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
            this.band = request.getMusicBand();
            return execute();
        }
        return null;
    }

    @Override
    public Response execute() {
        CommandStatus res = collectionManager.updateBand(login, bandId, band);
        return new Response("update", res.getDescription(), null);
    }
}
