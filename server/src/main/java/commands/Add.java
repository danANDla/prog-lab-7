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

    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response extendedExecute(Request request) {
        this.band = request.getMusicBand();
        return execute();
    }

    @Override
    public Response execute() {
        // CommandStatus res = collecManager.insertBand(this.band);
        CommandStatus res = CommandStatus.FAIL;
        if (collectionManager.insertBand(band)) {
            res = CommandStatus.OK;
        }
        return new Response("add", res.name(), null);
    }
}
