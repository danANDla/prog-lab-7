package application;

import udp.Request;
import udp.Response;
import udp.UDPserver;
import utils.CommandsManager;
import utils.IOutil;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;


public class RequestHandler implements Runnable {
    protected String serverText = null;
    private UDPserver udp;
    private IOutil io;
    private CommandsManager commandsManager;
    private ForkJoinPool forkJoinPool;

    public RequestHandler(String serverText, UDPserver udp, IOutil io, CommandsManager commandsManager,
                          ForkJoinPool forkPool) {
        this.serverText = serverText;
        this.udp = udp;
        this.io = io;
        this.commandsManager = commandsManager;
        this.forkJoinPool = forkPool;
    }

    public void run() {
        try {
            Request recieved = udp.recieveRequest();
            if (recieved == null) throw new Exception();
            RequestExecutor requestExecutor = new RequestExecutor(commandsManager, recieved);
            ArrayList<Response> respList = forkJoinPool.invoke(requestExecutor);

            ResponseSender responseSender = new ResponseSender(respList, commandsManager, recieved, udp, io);
            forkJoinPool.invoke(responseSender);
        } catch (Exception e) {
            io.printError("Exception while receiving package: " + e);
        }
    }
}