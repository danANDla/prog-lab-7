package udp;

import java.io.Serializable;
import java.net.SocketAddress;

public class Response implements Serializable {
    String command;
    String msg;
    SocketAddress receiver;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SocketAddress getReceiver() {
        return receiver;
    }

    public void setReceiver(SocketAddress receiver) {
        this.receiver = receiver;
    }

    public Response(String command, String msg, SocketAddress receiver) {
        this.command = command;
        this.msg = msg;
        this.receiver = receiver;
    }

    public Response() {
    }

    @Override
    public String toString() {
        return "Response{" +
                "command='" + command + '\'' +
                ", msg='" + msg + '\'' +
                ", receiver=" + receiver +
                '}';
    }
}
