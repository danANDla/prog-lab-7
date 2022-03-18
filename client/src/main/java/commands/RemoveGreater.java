package commands;

import collection.MusicBand;
import commands.types.RemoteCommand;
import udp.Request;
import users.User;
import utils.MusicBandFactory;

import java.net.SocketAddress;

public class RemoveGreater implements RemoteCommand {
    private MusicBandFactory musicBandFactory;
    private int bandId;

    public RemoveGreater(MusicBandFactory musicBandFactory) {
        this.musicBandFactory = musicBandFactory;
    }

    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        MusicBand musicBand = musicBandFactory.makeBand();
        return new Request(user, "greater", null, musicBand, sender);
    }

    @Override
    public String getdescription() {
        String descr = "удалить из коллекции все элементы, больше, чем заданный";
        return descr;
    }
}
