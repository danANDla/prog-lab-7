package utils;

import commands.*;
import commands.types.LocalCommand;
import commands.types.RemoteArgumentedCommand;
import commands.types.RemoteCommand;
import udp.Request;
import udp.UDPclient;

import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Locale;

public class CommandsManager {
    private HashMap<String, RemoteCommand> commandsList;
    private HashMap<String, RemoteArgumentedCommand> argumentedComandsList;
    private HashMap<String, LocalCommand> localComandsList;
    private IOutil io;
    private MusicBandFactory musicBandFactory;
    private Asker asker;
    private UDPclient udp;

    ArrayDeque<String> history;

    public CommandsManager(IOutil ioutil){
        io = ioutil;
        asker = new Asker(io);
        musicBandFactory = new MusicBandFactory(asker);
        history = new ArrayDeque<String>();
        udp = new UDPclient(io);
        fillLists();
    }

    public void fillLists(){
        commandsList = new HashMap<String, RemoteCommand>();
        argumentedComandsList = new HashMap<String, RemoteArgumentedCommand>();
        localComandsList = new HashMap<String, LocalCommand>();

        localComandsList.put("exit", new Exit(io));
        localComandsList.put("help", new Help(commandsList, localComandsList, argumentedComandsList, io));
        localComandsList.put("history", new History(history, io));

        commandsList.put("info", new Info());
        commandsList.put("add", new Add(musicBandFactory));

        argumentedComandsList.put("update", new Update(io, musicBandFactory));
    }

    public void executeCommand(String newCommand){
        String[] command = newCommand.trim().toLowerCase(Locale.ROOT).split("\\s+");
        if(localComandsList.containsKey(command[0])){
            LocalCommand parsedCommand = localComandsList.get(command[0]);
            parsedCommand.execute();
            history.addFirst(command[0]);
            if(history.size() > 14){
                history.removeLast();
            }
        }
        else{
            SocketAddress sender = udp.getAddress();
            sendCommand(newCommand, sender);
        }
    }

    private void sendCommand(String newCommand, SocketAddress sender){
        String[] command = newCommand.trim().toLowerCase(Locale.ROOT).split("\\s+");
        if(argumentedComandsList.containsKey(command[0])){
            RemoteArgumentedCommand parsedCommand = argumentedComandsList.get(command[0]);

            //TODO add command package and send
            if(parsedCommand.parseArgs(command)){
                Request newReq = parsedCommand.makeRequest(sender);
                udp.sendCommand(newReq);
                udp.receiveResponse();
            }

            history.addFirst(command[0]);
            if(history.size() > 14){
                history.removeLast();
            }
        }
        else if(commandsList.containsKey(command[0])){
            RemoteCommand parsedCommand = commandsList.get(command[0]);

            Request newReq = parsedCommand.makeRequest(sender);
            udp.sendCommand(newReq);

            history.addFirst(command[0]);
            if(history.size() > 14){
                history.removeLast();
            }
            udp.receiveResponse();
        }
        else{
            io.printError("Такой команды не найдено");
        }
    }
}
