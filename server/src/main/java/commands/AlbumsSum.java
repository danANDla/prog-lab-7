package commands;

import commands.types.Command;
import udp.Response;
import utils.CollectionManager;

public class AlbumsSum implements Command {
    private CollectionManager collectionManager;

    public AlbumsSum(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        String res = collectionManager.albumsCount();
        return new Response("sumshow", res, null);
    }
}
