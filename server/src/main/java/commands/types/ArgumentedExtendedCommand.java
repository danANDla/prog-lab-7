package commands.types;

import collection.MusicBand;
import udp.Response;

public interface ArgumentedExtendedCommand extends ArgumentedCommand{
    Response extendedExecute(MusicBand band);
}
