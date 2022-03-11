package application;

import udp.Request;
import udp.Response;
import udp.ResponseError;
import udp.UDPserver;
import utils.CollectionManager;
import utils.CommandsManager;
import utils.DBmanager;
import utils.IOutil;

import java.util.ArrayList;

public class ServerApplication {
    private IOutil io;
    private CommandsManager commandsManager;
    private CollectionManager collectionManager;
    private DBmanager dbmanager;
    private UDPserver udp;

    public ServerApplication() {
        io = new IOutil();
        collectionManager = new CollectionManager(io);
        dbmanager = new DBmanager(io);
        commandsManager = new CommandsManager(io, collectionManager, dbmanager);
        udp = new UDPserver(io);
    }

    public void start() {
        listening();
    }

    public void listening() {
        while (true) {
            try {
                Request recieved = udp.recieveRequest();
                if (recieved == null) throw new Exception();
                ArrayList<Response> respList = commandsManager.executeRequest(recieved);
                if (respList == null) {
                    udp.sendError(ResponseError.INVALID_COMMAND, recieved);
                } else {
                    if (respList.size() == 1) {
                        udp.sendReponse(respList.get(0), recieved.getSender());
                    } else {
                        io.printWarning("rich command");
                        udp.sendReponse(
                                new Response("showheader", Integer.toString(respList.size()), null),
                                recieved.getSender()
                        );
                        for (Response resp : respList) {
                            System.out.println(resp);
                        }
                        for (Response resp : respList) {
                            Request next = udp.recieveRequest();
                            if (next == null) throw new Exception();
                            if (commandsManager.next(next)) {
                                udp.sendReponse(resp, recieved.getSender());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                io.printError("Exception while receiving package: " + e);
            }
        }
    }
}
