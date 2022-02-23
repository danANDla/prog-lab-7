package udp;


import collection.MusicBand;

import java.io.Serializable;
import java.net.SocketAddress;

public class Request implements Serializable {
    private String command;
    private String args;
    private MusicBand musicBand;
    private SocketAddress sender;

    public Request() {
    }

    public Request(String command, String args, MusicBand musicBand, SocketAddress sender) {
        this.command = command;
        this.args = args;
        this.musicBand = musicBand;
        this.sender = sender;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }

    public void setMusicBand(MusicBand musicBand) {
        this.musicBand = musicBand;
    }

    public SocketAddress getSender() {
        return sender;
    }

    public void setSender(SocketAddress sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", args='" + args + '\'' +
                ", musicBand=" + musicBand +
                ", sender=" + sender +
                '}';
    }
}
