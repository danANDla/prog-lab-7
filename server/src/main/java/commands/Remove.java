package commands;

import collection.MusicBand;
import commands.types.ArgumentedCommand;
import udp.Response;
import utils.DBmanager;

public class Remove implements ArgumentedCommand {
    DBmanager dbmanager;
    private int bandId;

    public Remove(DBmanager dbmanager) {
        this.dbmanager = dbmanager;
    }

    @Override
    public boolean parseArgs(String[] command) {
        if(command.length  < 1){
            System.out.println("введено недостаточно аргументов");
            return false;
        }
        else{
            try {
                bandId = Integer.parseInt(command[0]);
            } catch (NumberFormatException e){
                System.out.println("неправильный формат аргумента");
                return false;
            }
        }
        return true;
    }

    @Override
    public Response execute() {
        CommandStatus res = CommandStatus.FAIL;
        String msg = "";
        if(dbmanager.removeById(bandId)) res = CommandStatus.OK;
        else msg = ": no such id";
        return new Response("remove_by_id", res.getDescription() + msg, null);
    }

    @Override
    public String getdescription() {
        String discr = "удалить элемент, id которого равен заданному";
        return discr;
    }

    @Override
    public String getArgsDescription() {
        String descr = "id";
        return descr;
    }
}
