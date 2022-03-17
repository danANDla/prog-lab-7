package commands.types;

import udp.Request;
import users.User;

import java.net.SocketAddress;

public interface RemoteCommand extends Command{
    Request makeRequest(User user, SocketAddress sender);
}
