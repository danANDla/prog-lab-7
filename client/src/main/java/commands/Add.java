package commands;

import collection.MusicBand;
import utils.MusicBandFactory;
import commands.types.RemoteCommand;
import udp.Request;

import java.net.SocketAddress;

public class Add implements RemoteCommand {
    private MusicBandFactory musicBandFactory;

    public Add(MusicBandFactory musicBandFactory) {
        this.musicBandFactory = musicBandFactory;
    }

    @Override
    public Request makeRequest(SocketAddress sender) {
        MusicBand musicBand = musicBandFactory.makeBand();
        return new Request("add", null, musicBand, sender);
    }

    @Override
    public String getdescription() {
        String descr = "добавить новый элемент в коллекцию";
        return descr;
    }
}
