package commands;


import commands.types.RemoteCommand;
import udp.Request;

import java.net.SocketAddress;

public class Show implements RemoteCommand {
    @Override
    public Request makeRequest(SocketAddress sender) {
        return new Request("show", null, null, sender);
    }

    @Override
    public String getdescription() {
        String description = "вывести все элементы коллекции";
        return description;
    }
}
