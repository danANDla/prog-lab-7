package commands;

import collection.MusicBand;
import commands.types.ArgumentedExtendedCommand;
import udp.Response;
import utils.CollectionManager;
import utils.DBmanager;

public class Update implements ArgumentedExtendedCommand {
    private CollectionManager collectionManager;
    private int bandId;
    private MusicBand band;

    public Update(CollectionManager collectionManager) {
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
    public Response extendedExecute(MusicBand band) {
        this.band = band;
        return execute();
    }

    @Override
    public Response execute() {
        CommandStatus res = CommandStatus.FAIL;
        String msg = "";
        if (collectionManager.updateBand(bandId, band)) res = CommandStatus.OK;
        else msg = ": no such id";
        return new Response("update", res.getDescription() + msg, null);
    }
}
