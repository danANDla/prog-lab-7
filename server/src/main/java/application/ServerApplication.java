package application;

import udp.Request;
import udp.Response;
import udp.UDPserver;
import utils.CollectionManager;
import utils.CommandsManager;
import utils.IOutil;

public class ServerApplication {
    private IOutil io;
    private CommandsManager commandsManager;
    private CollectionManager collectionManager;
    private UDPserver udp;

    public ServerApplication(){
        io = new IOutil();
        collectionManager = new CollectionManager(io);
        commandsManager = new CommandsManager(io, collectionManager);
        udp = new UDPserver(io);
    }

    public void start(){
        listening();
    }

    public void listening(){
        while(true){
            try{
                Request recieved = udp.recieveRequest();
                if(recieved == null) throw new Exception();
                Response resp = commandsManager.executeRequest(recieved);
                udp.sendReponse(resp, recieved.getSender());
            } catch (Exception e){
                io.printError("Exception while receiving package");
            }
        }
    }
}
