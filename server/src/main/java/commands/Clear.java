package commands;

import commands.types.Command;
import commands.types.ExtendedCommand;
import udp.Request;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

public class Clear implements ExtendedCommand {
    private CollectionManager collectionManager;
    private String login;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        CommandStatus res = collectionManager.clearList(login);
        return new Response("clear", res.getDescription(), null);
    }

    @Override
    public Response extendedExecute(Request request) {
        this.login = request.getUser().getLogin();
        return execute();
    }
}
