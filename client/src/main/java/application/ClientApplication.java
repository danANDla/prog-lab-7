package application;

import udp.UDPclient;
import users.User;
import utils.Asker;
import utils.CommandsManager;
import utils.IOutil;
import utils.UserManager;

public class ClientApplication {
    private IOutil io;
    private UDPclient udp;
    private CommandsManager executor;
    private UserManager userManager;
    private Asker asker;
    private User user;

    public ClientApplication(){
        io = new IOutil();
        asker = new Asker(io);
        udp = new UDPclient(io);
        executor = new CommandsManager(io, asker, udp);
        userManager = new UserManager(new User(), asker, io, udp);
    }

    public void start(){
        this.user = userManager.userInit(udp.getAddress());
        if(this.user != null) executeCommands();
    }

    public void executeCommands(){
        while(true){
            System.out.print("> ");
            String newCommand = io.readLine();
            executor.executeCommand(newCommand, this.user);
        }
    }
}
