package commands;

import commands.types.Command;
import udp.Response;
import utils.CollectionManager;

public class Info implements Command {
    CollectionManager collectionManager;

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

    @Override
    public String getdescription() {
        String descr = "вывести информацию о коллекции";
        return descr;
    }
}
