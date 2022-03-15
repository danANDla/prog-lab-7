package application;


import udp.UDPserver;
import utils.CommandsManager;
import utils.IOutil;

import java.io.IOException;
import java.util.concurrent.*;

public class ThreadPoolServer implements Runnable {
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    //    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private ExecutorService threadPool = new ThreadPoolExecutor(3, 6, 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(3),
            new RejectionHandler());
    private UDPserver udp;
    private IOutil io;
    private CommandsManager commandsManager;
    private ForkJoinPool forkJoinPool;

    public ThreadPoolServer(UDPserver udp, IOutil io, CommandsManager commandsManager, ForkJoinPool forkPool) {
        this.udp = udp;
        this.io = io;
        this.commandsManager = commandsManager;
        this.forkJoinPool = forkPool;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        if (udp.udpStart()) {
            while (!isStopped()) {
                this.threadPool.execute(
                        new RequestHandler("Thread Pooled Server", udp, io, commandsManager, forkJoinPool));
            }
            this.threadPool.shutdown();
            System.out.println("Server Stopped.");
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            udp.udpStop();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
}