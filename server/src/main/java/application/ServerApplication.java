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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerApplication {
    private IOutil io;
    private CommandsManager commandsManager;
    private CollectionManager collectionManager;
    private DBmanager dbmanager;
    private UDPserver udp;
    private ExecutorService pool = Executors.newFixedThreadPool(5);
    private ThreadPoolServer poolServer;
    private ForkJoinPool forkJoinPool;

    public ServerApplication() {
        io = new IOutil();
        dbmanager = new DBmanager(io);
        collectionManager = new CollectionManager(io, dbmanager);
        commandsManager = new CommandsManager(io, collectionManager, dbmanager);
        udp = new UDPserver(io);
        forkJoinPool = new ForkJoinPool();
        poolServer = new ThreadPoolServer(udp, io, commandsManager, forkJoinPool);
    }

    public void start() {
//        listening();
        collectionManager.sync();
        io.printWarning("local collection synchronized with db");
        new Thread(poolServer).start();
    }

    public void listening() {
        collectionManager.sync();
        io.printWarning("local collection synchronized with db");
        while (true) {
            try {
                Request recieved = udp.recieveRequest();
                if (recieved == null) throw new Exception();
                ArrayList<Response> respList = commandsManager.executeRequest(recieved);
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
