package commands.types;

import udp.Response;

import java.util.ArrayList;

public interface RichResponseCommand{
    ArrayList<Response> richExecute();
}
