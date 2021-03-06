package commands;

import commands.types.LocalCommand;
import commands.types.RemoteArgumentedCommand;
import commands.types.RemoteCommand;
import utils.IOutil;

import java.util.HashMap;
import java.util.Map;

public class Help implements LocalCommand {
    private HashMap<String, RemoteCommand> commandsList;
    private HashMap<String, RemoteArgumentedCommand> argumentedCommandsList;
    private HashMap<String, LocalCommand> localCommandsList;
    private HashMap<String, RemoteCommand> richCommandsList;
    private Script script;
    private IOutil io;

    public Help(HashMap<String, RemoteCommand> commandsList,
                HashMap<String, LocalCommand> localCommandsList,
                HashMap<String, RemoteArgumentedCommand> argumentedCommandsList,
                HashMap<String, RemoteCommand> richCommandsList,
                Script script, IOutil io) {
        this.commandsList = commandsList;
        this.argumentedCommandsList = argumentedCommandsList;
        this.localCommandsList = localCommandsList;
        this.richCommandsList = richCommandsList;
        this.script = script;
        this.io = io;
    }

    public void execute() {
        io.printText("Локальные команды");
        for (Map.Entry<String, LocalCommand> command : localCommandsList.entrySet()) {
            io.printCommand(command.getKey(), command.getValue().getdescription());
        }
        io.printText("Команды без аргументов");
        for (Map.Entry<String, RemoteCommand> command : commandsList.entrySet()) {
            io.printCommand(command.getKey(), command.getValue().getdescription());
        }
        for (Map.Entry<String, RemoteCommand> command : richCommandsList.entrySet()) {
            io.printCommand(command.getKey(), command.getValue().getdescription());
        }
        io.printText("Команды с аргументами");
        for (Map.Entry<String, RemoteArgumentedCommand> command : argumentedCommandsList.entrySet()) {
            io.printArgumentedCommand(command.getKey(), command.getValue().getArgsDescription(), command.getValue().getdescription());
        }
        io.printArgumentedCommand("execute_script", script.getArgsDescription(), script.getdescription());
    }

    @Override
    public String getdescription() {
        String descr = "вывести справку по доступным командам";
        return descr;
    }
}

