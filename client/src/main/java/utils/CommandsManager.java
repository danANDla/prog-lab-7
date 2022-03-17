package utils;

import commands.*;
import commands.types.LocalCommand;
import commands.types.RemoteArgumentedCommand;
import commands.types.RemoteCommand;
import udp.Request;
import udp.Response;
import udp.UDPclient;
import users.User;

import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Locale;

public class CommandsManager {
    private HashMap<String, RemoteCommand> commandsList;
    private HashMap<String, RemoteArgumentedCommand> argumentedComandsList;
    private HashMap<String, LocalCommand> localComandsList;
    private HashMap<String, RemoteCommand> richCommandsList;
    private Script script;
    private IOutil io;
    private MusicBandFactory musicBandFactory;
    private Asker asker;
    private UDPclient udp;

    ArrayDeque<String> history;

    public CommandsManager(IOutil ioutil, Asker asker, UDPclient udpclient) {
        io = ioutil;
        this.asker = asker;
        musicBandFactory = new MusicBandFactory(asker);
        history = new ArrayDeque<String>();
        udp = udpclient;
        this.script = new Script(io,this);
        fillLists();
    }

    public void fillLists() {
        commandsList = new HashMap<String, RemoteCommand>();
        argumentedComandsList = new HashMap<String, RemoteArgumentedCommand>();
        localComandsList = new HashMap<String, LocalCommand>();
        richCommandsList = new HashMap<String, RemoteCommand>();

        localComandsList.put("exit", new Exit(io));
        localComandsList.put("help", new Help(commandsList, localComandsList,
                argumentedComandsList, richCommandsList, script, io));
        localComandsList.put("history", new History(history, io));

        commandsList.put("info", new Info());
        commandsList.put("clear", new Clear());
        commandsList.put("add", new Add(musicBandFactory));
        commandsList.put("remove_lower", new RemoveLower(musicBandFactory));
        commandsList.put("group_counting_by_description", new Group());

        argumentedComandsList.put("update", new Update(io, musicBandFactory));
        argumentedComandsList.put("remove_by_id", new Remove(io));

        richCommandsList.put("show", new Show());
    }

    public void executeCommand(String newCommand, User user) {
        String[] command = newCommand.trim().toLowerCase(Locale.ROOT).split("\\s+");
        if(command[0].equals("execute_script")){
            script.execute(command, user);
        }
        else if (localComandsList.containsKey(command[0])) {
            LocalCommand parsedCommand = localComandsList.get(command[0]);
            parsedCommand.execute();
            history.addFirst(command[0]);
            if (history.size() > 14) {
                history.removeLast();
            }
        } else {
            SocketAddress sender = udp.getAddress();
            sendCommand(newCommand, user, sender);
        }
    }

    private void sendCommand(String newCommand, User user, SocketAddress sender) {
        String[] command = newCommand.trim().toLowerCase(Locale.ROOT).split("\\s+");
        Response resp = null;
        if (argumentedComandsList.containsKey(command[0])) {
            RemoteArgumentedCommand parsedCommand = argumentedComandsList.get(command[0]);

            //TODO add command package and send
            if (parsedCommand.parseArgs(command)) {
                Request newReq = parsedCommand.makeRequest(user, sender);
                udp.sendCommand(newReq);
                resp = udp.receiveResponse();
                if (resp != null) {
                    io.printText(resp.getMsg());
                }
            }
        } else if (commandsList.containsKey(command[0])) {
            RemoteCommand parsedCommand = commandsList.get(command[0]);

            Request newReq = parsedCommand.makeRequest(user, sender);
            udp.sendCommand(newReq);
            resp = udp.receiveResponse();
            if (resp != null) {
                io.printText(resp.getMsg());
            }
        } else if (richCommandsList.containsKey(command[0])) {
            io.printWarning("prepare for receiving multiple packets");
            RemoteCommand parsedCommand = richCommandsList.get(command[0]);

            Request req = parsedCommand.makeRequest(user, sender);
            udp.sendCommand(req);

            Response header = udp.receiveResponse();
            int rowsNumber = -1;
            if (header != null) {
                io.printText(header.getMsg());
                try {
                    rowsNumber = Integer.parseInt(header.getMsg());
                } catch (NumberFormatException e) {
                    io.printError("Неправильный формат принятого пакета");
                }
                if (rowsNumber > 0) {
                    for (int i = 0; i < rowsNumber; ++i) {
                        //udp.sendCommand(new Request("next", null, null, sender));
                        resp = udp.receiveResponse();
                        if (resp != null) io.printText(resp.getMsg());
                    }
                }
            }
        } else {
            io.printError("Такой команды не найдено");
            return;
        }
        history.addFirst(command[0]);
        if (history.size() > 14) {
            history.removeLast();
        }
    }
}
