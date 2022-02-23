package commands;

import collection.MusicBand;
import commands.types.ExtendedCommand;
import udp.Response;
import utils.CollectionManager;

public class Add implements ExtendedCommand {
    private CollectionManager collecManager;
    private MusicBand band;

    public Add(CollectionManager collMan){
        collecManager = collMan;
    }

    @Override
    public Response extendedExecute(MusicBand band) {
        this.band = band;
        return execute();
    }

    @Override
    public Response execute() {
        CommandStatus res = collecManager.insertBand(this.band);
        return new Response("add", res.name(), null);
    }

    @Override
    public String getdescription() {
        String descr = "добавить новый элемент в коллекцию";
        return descr;
    }
}
