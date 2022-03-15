package commands;

import commands.types.Command;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

public class Info implements Command {
    private static String dbpath;
    private CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute() {
        Response resp = new Response();
        resp.setReceiver(null);
        resp.setCommand("info");
        resp.setMsg(collectionManager.info());
        return resp;
    }
}
