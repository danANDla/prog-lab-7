package commands;

import commands.types.RemoteCommand;
import udp.Request;

import java.net.SocketAddress;

public class Clear implements RemoteCommand {

    @Override
    public Request makeRequest(SocketAddress sender) {
        return new Request("clear", null, null, sender);
    }

    @Override
    public String getdescription() {
        String descr = "очистить коллекцию";
        return descr;
    }
}
