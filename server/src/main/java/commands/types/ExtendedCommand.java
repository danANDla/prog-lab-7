package commands.types;

import udp.Request;
import udp.Response;

public interface ExtendedCommand extends Command {
    Response extendedExecute(Request request);
}
