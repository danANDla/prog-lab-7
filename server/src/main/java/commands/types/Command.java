package commands.types;

import udp.Response;

public interface Command {
    Response execute();
    String getdescription();
}
