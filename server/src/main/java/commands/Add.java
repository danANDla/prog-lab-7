package commands;

import collection.MusicBand;
import commands.types.ExtendedCommand;
import udp.Request;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

public class Add implements ExtendedCommand {
    private CollectionManager collectionManager;
    private MusicBand band;
    private String login;

    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response extendedExecute(Request request) {
        this.band = request.getMusicBand();
        this.login = request.getUser().getLogin();
        return execute();
    }

    @Override
    public Response execute() {
        // CommandStatus res = collecManager.insertBand(this.band);
        CommandStatus res = collectionManager.insertBand(login, band);
        return new Response("add", res.name(), null);
    }
}
