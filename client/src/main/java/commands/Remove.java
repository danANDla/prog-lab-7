package commands;

import commands.types.RemoteArgumentedCommand;
import udp.Request;
import users.User;
import utils.IOutil;

import java.net.SocketAddress;

public class Remove implements RemoteArgumentedCommand {
    private IOutil io;
    private int bandId;

    public Remove(IOutil io) {
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
        return new Request(user, "remove", Integer.toString(bandId), null, sender);
    }

    @Override
    public String getdescription() {
        String discr = "удалить элемент, id которого равен заданному";
        return discr;
    }

    @Override
    public String getArgsDescription() {
        String descr = "id";
        return descr;
    }
}
