package commands;

import collection.MusicBand;
import commands.types.ExtendedCommand;
import udp.Request;
import udp.Response;
import utils.CollectionManager;

public class RemoveLower implements ExtendedCommand {
    private CollectionManager collectionManager;
    private MusicBand musicBand;
    private String login;

    public RemoveLower(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response extendedExecute(Request request) {
        this.login = request.getUser().getLogin();
        this.musicBand = request.getMusicBand();
        return execute();
    }

    @Override
    public Response execute() {
        CommandStatus res = collectionManager.removeLower(this.login, this.musicBand);
        return new Response("lower", res.getDescription(), null);
    }
}
