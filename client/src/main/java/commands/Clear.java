package commands;

import commands.types.RemoteCommand;
import udp.Request;
import users.User;

import java.net.SocketAddress;

public class Clear implements RemoteCommand {

    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        return new Request(user, "clear", null, null, sender);
    }

    @Override
    public String getdescription() {
        String descr = "очистить коллекцию";
        return descr;
    }
}
