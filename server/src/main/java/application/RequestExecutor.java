package application;

import udp.Request;
import udp.Response;
import utils.CommandsManager;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class RequestExecutor extends RecursiveTask<ArrayList<Response>> {
    private CommandsManager commandsManager;
    private Request recieved;

    public RequestExecutor(CommandsManager commandsManager, Request recieved) {
        this.commandsManager = commandsManager;
        this.recieved = recieved;
    }
    @Override
    protected ArrayList<Response> compute() {
        ArrayList<Response> respList = commandsManager.executeRequest(recieved);
        return  respList;
    }
}
