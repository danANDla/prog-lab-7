package commands;

import users.User;
import utils.CommandsManager;
import utils.IOutil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Script {
    private IOutil io;
    private String path;
    private ArrayList<String> pathHistory;
    private CommandsManager commandsManager;
    private static final String folder = "/home/danandla/BOTAY/programming/labs/prog-7/prog-lab-7/";

    public Script(IOutil io, CommandsManager commandsManager) {
        this.io = io;
        this.commandsManager = commandsManager;
        pathHistory = new ArrayList<>();
    }

    public boolean parseArgs(String[] command) {
        if (command.length - 1 < 1) {
            io.printError("введено недостаточно аргументов");
            return false;
        } else {
            path = command[1];
        }
        return true;
    }

    public void execute(String[] command, User user) {
        if (!parseArgs(command)) return;
        try {
            pathHistory.add(path);
//            File ioFile = new File(path);
//            if (!ioFile.canWrite() || ioFile.isDirectory() || !ioFile.isFile()) throw new IOException();
            FileInputStream fileInputStream = new FileInputStream(folder + path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            Scanner scanner = new Scanner(inputStreamReader);
            while (scanner.hasNext()) {
                String str = scanner.nextLine();
                io.printWarning(str);
                String[] scrCom = str.trim().toLowerCase(Locale.ROOT).split("\\s+");
                if (scrCom[0].equals("execute_script") && scrCom.length != 0) {
                    if (parseArgs(scrCom)) {
                        if (pathHistory.contains(path)) {
                            io.printError("рекурсивный скрипт");
                            return;
                        } else {
                            this.execute(scrCom, user);
                        }
                    }
                }
                commandsManager.executeCommand(str, user);
            }
        } catch (IOException e) {
            io.printError("Нет доступа к файлу " + e.getMessage());
        }
    }

    public String getdescription() {
        String descr = "считать и исполнить скрипт из указанного файла";
        return descr;
    }

    public String getArgsDescription() {
        String descr = "file_name";
        return descr;
    }
}
