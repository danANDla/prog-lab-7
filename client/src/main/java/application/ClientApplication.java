package application;

import utils.CommandsManager;
import utils.IOutil;

public class ClientApplication {
    private IOutil io;
    private CommandsManager executor;

    public ClientApplication(){
        io = new IOutil();
        executor = new CommandsManager(io);
    }

    public void start(){
        executeCommands();
    }

    public void executeCommands(){
        while(true){
            System.out.print("> ");
            String newCommand = io.readLine();
            executor.executeCommand(newCommand);
        }
    }
}
