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

//            RequestExecutor requestExecutor = new RequestExecutor(commandsManager, recieved);
//            ArrayList<Response> respList =
//            ArrayList<Response> respList = commandsManager.executeRequest(recieved);
//            if (respList == null) {
//                udp.sendError(ResponseError.INVALID_COMMAND, recieved);
//            } else {
//                if (!commandsManager.isRich(recieved)) {
//                    udp.sendReponse(respList.get(0), recieved.getSender());
//                } else {
//                    io.printWarning("rich command");
//                    udp.sendReponse(
//                            new Response("showheader", Integer.toString(respList.size()), null),
//                            recieved.getSender()
//                    );
//                    for (Response resp : respList) {
//                        System.out.println(resp);
//                    }
//                    for (Response resp : respList) {
//                        Request next = udp.recieveRequest();
//                        if (next == null) throw new Exception();
//                        if (commandsManager.next(next)) {
//                            udp.sendReponse(resp, recieved.getSender());
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            io.printError("Exception while receiving package: " + e);
        }
    }
}