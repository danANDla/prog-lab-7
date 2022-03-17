package commands;

import commands.types.RemoteCommand;
import udp.Request;
import users.User;

import java.net.SocketAddress;

public class Group implements RemoteCommand {
    @Override
    public Request makeRequest(User user, SocketAddress sender) {
        return new Request(user, "group", null, null, sender);
    }

    @Override
    public String getdescription() {
        String descr = "сгруппировать элементы коллекции по значению поля description, вывести количество элементов в каждой группе";
        return descr;
    }
}
