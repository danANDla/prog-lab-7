package commands;

import commands.types.RemoteArgumentedCommand;
import udp.Request;
import users.User;
import utils.IOutil;

import java.net.SocketAddress;

public class RemoveAlbums implements RemoteArgumentedCommand {
    private IOutil io;
    private int albumsCount;

    public RemoveAlbums(IOutil io) {
        this.io = io;
    }

    @Override
    public boolean parseArgs(String[] command) {
        if(command.length - 1 < 1){
            io.printError("введено недостаточно аргументов");
            return false;
        }
        else{
            try {
                albumsCount = Integer.parseInt(command[1]);
            } catch (NumberFormatException e){
                io.printError("неправильный формат аргумента");
                return false;
            }
        }
        return true;
    }

    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        return new Request(user, "removebysum", Integer.toString(albumsCount), null, sender);
    }

    @Override
    public String getdescription() {
        String descr = "удалить из коллекции все элементы, значение поля albumsCount которого эквивалентно заданному";
        return descr;
    }

    @Override
    public String getArgsDescription() {
        String descr = "k";
        return descr;
    }
}
