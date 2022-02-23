package commands;

import commands.types.RemoteCommand;
import udp.Request;

import java.net.SocketAddress;

public class Info implements RemoteCommand {
    @Override
    public Request makeRequest(SocketAddress sender) {
        return new Request("info", null, null, sender);
    }

    @Override
    public String getdescription() {
        String descr = "вывести информацию о коллекции";
        return descr;
    }
}
