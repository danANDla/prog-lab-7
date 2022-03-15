package commands;

import commands.types.Command;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

public class Clear implements Command {
    private CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        CommandStatus res = CommandStatus.FAIL;
        if (collectionManager.clearList()) res = CommandStatus.OK;
        return new Response("clear", res.getDescription(), null);
    }
}
