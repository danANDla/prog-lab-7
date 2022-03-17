package commands;

import collection.MusicBand;
import commands.types.RemoteCommand;
import udp.Request;
import users.User;
import utils.MusicBandFactory;

import java.net.SocketAddress;

public class RemoveLower implements RemoteCommand {
    private MusicBandFactory musicBandFactory;
    private int bandId;

    public RemoveLower(MusicBandFactory musicBandFactory) {
        this.musicBandFactory = musicBandFactory;
    }

    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        MusicBand musicBand = musicBandFactory.makeBand();
        return new Request(user, "lower", null, musicBand, sender);
    }

    @Override
    public String getdescription() {
        String descr = "удалить из коллекции все элементы, меньшие, чем заданный";
        return descr;
    }
}
