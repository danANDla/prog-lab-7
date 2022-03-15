package commands;

import commands.types.ArgumentedCommand;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

public class Remove implements ArgumentedCommand {
    private CollectionManager collectionManager;
    private int bandId;

    public Remove(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean parseArgs(String[] command) {
        if (command.length < 1) {
            System.out.println("введено недостаточно аргументов");
            return false;
        } else {
            try {
                bandId = Integer.parseInt(command[0]);
            } catch (NumberFormatException e) {
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
        if (collectionManager.removeBand(bandId)) res = CommandStatus.OK;
        else msg = ": no such id";
        return new Response("remove_by_id", res.getDescription() + msg, null);
    }
}
