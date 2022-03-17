package commands;

import collection.MusicBand;
import users.User;
import utils.MusicBandFactory;
import commands.types.RemoteArgumentedCommand;
import udp.Request;
import utils.IOutil;

import java.net.SocketAddress;

public class Update implements RemoteArgumentedCommand {
    private IOutil io;
    private MusicBandFactory musicBandFactory;
    private int bandId;

    public Update(IOutil io, MusicBandFactory musicBandFactory) {
        this.io = io;
        this.musicBandFactory = musicBandFactory;
    }

    @Override
    public boolean parseArgs(String[] command) {
        if(command.length - 1 < 1){
            io.printError("введено недостаточно аргументов");
            return false;
        }
        else{
            try {
                bandId = Integer.parseInt(command[1]);
            } catch (NumberFormatException e){
                io.printError("неправильный формат аргумента");
                return false;
            }
        }
        return true;
    }

    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        MusicBand musicBand = musicBandFactory.makeBand();
        return new Request(user, "update", Integer.toString(bandId), musicBand, sender);
    }

    @Override
    public String getdescription() {
        String descr = "обновить значение элемента, id которого равен заданному";
        return descr;
    }

    @Override
    public String getArgsDescription() {
        String descr = "id";
        return descr;
    }
}
