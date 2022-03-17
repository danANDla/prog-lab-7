package commands;

import commands.types.RemoteCommand;
import udp.Request;
import users.User;

import java.net.SocketAddress;

public class Info implements RemoteCommand {
    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        return new Request(user, "info", null, null, sender);
    }

    @Override
    public String getdescription() {
        String descr = "вывести информацию о коллекции";
        return descr;
    }
}
