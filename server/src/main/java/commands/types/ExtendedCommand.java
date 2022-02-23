package commands.types;

import collection.MusicBand;
import udp.Response;

public interface ExtendedCommand extends Command {
    Response extendedExecute(MusicBand band);
}
