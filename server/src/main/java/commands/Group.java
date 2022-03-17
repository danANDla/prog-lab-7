package commands;

import commands.types.Command;
import udp.Response;
import utils.CollectionManager;

public class Group implements Command {
    private CollectionManager collectionManager;

    public Group(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        return new Response("group", collectionManager.groupByDescription(), null);
    }
}
