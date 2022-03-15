package application;

import udp.Request;
import udp.Response;
import udp.ResponseError;
import udp.UDPserver;
import utils.CommandsManager;
import utils.IOutil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class ResponseSender extends RecursiveTask<Void> {
    private ArrayList<Response> respList;
    private CommandsManager commandsManager;
    private Request recieved;
    private UDPserver udp;
    private IOutil io;

    public ResponseSender(ArrayList<Response> respList, CommandsManager commandsManager, Request recieved, UDPserver udp, IOutil io) {
        this.respList = respList;
        this.commandsManager = commandsManager;
        this.recieved = recieved;
        this.udp = udp;
        this.io = io;
    }

    @Override
    protected Void compute() {
        if (respList == null) {
            udp.sendError(ResponseError.INVALID_COMMAND, recieved);
        } else {
            if (!commandsManager.isRich(recieved)) {
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
                    udp.sendReponse(resp, recieved.getSender());
                }
            }
        }
        return null;
    }
}
