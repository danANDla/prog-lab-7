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
        collectionManager.sync();
        io.printWarning("local collection synchronized with db");
        new Thread(poolServer).start();
    }
}
