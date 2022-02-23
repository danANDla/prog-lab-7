package commands.types;

import udp.Request;

import java.net.SocketAddress;

public interface RemoteCommand extends Command{
    Request makeRequest(SocketAddress sender);
}
