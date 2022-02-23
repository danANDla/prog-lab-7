package commands;

import commands.types.LocalCommand;
import utils.IOutil;

import java.util.ArrayDeque;
import java.util.Iterator;

public class History implements LocalCommand {
    ArrayDeque<String> hist;
    IOutil io;

    public History(ArrayDeque<String> hist, IOutil io) {
        this.hist = hist;
        this.io = io;
    }

    public void execute() {
        Iterator<String> it = hist.descendingIterator();
        while(it.hasNext()) {
            io.printText(it.next());
        }
    }

    @Override
    public String getdescription() {
        String descr = "вывести последние 14 команд";
        return descr;
    }
}
