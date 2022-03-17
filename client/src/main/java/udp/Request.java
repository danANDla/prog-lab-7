package udp;


import collection.MusicBand;
import users.User;

import java.io.Serializable;
import java.net.SocketAddress;

public class Request implements Serializable {
    private User user;
    private String command;
    private String args;
    private MusicBand musicBand;
    private SocketAddress sender;

    public Request(User user, String command, String args, MusicBand musicBand, SocketAddress sender) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
