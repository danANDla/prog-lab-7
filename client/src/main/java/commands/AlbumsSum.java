package commands;

import commands.types.RemoteCommand;
import udp.Request;
import users.User;

import java.net.SocketAddress;

public class AlbumsSum implements RemoteCommand {
    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        return new Request(user, "sumshow", null, null, sender);
    }

    @Override
    public String getdescription() {
        String discr = "вывести сумму значений поля albumsCount для всех элементов коллекции";
        return discr;
    }

}
