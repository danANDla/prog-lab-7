package commands;

import collection.MusicBand;
import commands.types.Command;
import commands.types.ExtendedCommand;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

public class Add implements ExtendedCommand {
    private CollectionManager collecManager;
    private DBmanager dbmanager;
    private MusicBand band;

    public Add(CollectionManager collecManager, DBmanager dbmanager) {
        this.collecManager = collecManager;
        this.dbmanager = dbmanager;
    }

    @Override
    public Response extendedExecute(MusicBand band) {
        this.band = band;
        return execute();
    }

    @Override
    public Response execute() {
        // CommandStatus res = collecManager.insertBand(this.band);
        CommandStatus res = CommandStatus.FAIL;
        if(dbmanager.add(band)) res = CommandStatus.OK;
        return new Response("add", res.name(), null);
    }

    @Override
    public String getdescription() {
        String descr = "добавить новый элемент в коллекцию";
        return descr;
    }
}
