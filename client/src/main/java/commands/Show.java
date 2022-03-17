package commands;


import commands.types.RemoteCommand;
import udp.Request;
import users.User;

import java.net.SocketAddress;

public class Show implements RemoteCommand {
    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        return new Request(user, "show", null, null, sender);
    }

    @Override
    public String getdescription() {
        String description = "вывести все элементы коллекции";
        return description;
    }
}
