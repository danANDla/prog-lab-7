package utils;

import udp.Request;
import udp.Response;
import udp.UDPclient;
import users.User;

import java.net.SocketAddress;
import java.util.Locale;

public class UserManager {
    private User user;
    private Asker asker;
    private IOutil io;
    private UDPclient udp;

    public UserManager(User user, Asker asker, IOutil io, UDPclient udp) {
        this.user = user;
        this.asker = asker;
        this.io = io;
        this.udp = udp;
    }

    public void auth(){
        this.user.setLogin(asker.askLogin());
        this.user.setPassword(asker.askPassword());
    }

    public void reg(){
        this.user.setLogin(asker.newLogin());
        this.user.setPassword(asker.newPassword());
    }

    public User userInit(SocketAddress sender){
        io.printText("введите log in, чтобы авторизоваться");
        io.printText("введите sign up, чтобы зарегистрироваться");
        String str = asker.askMode();
        if(str.toLowerCase(Locale.ROOT).equals("log in")){
            boolean isauth = false;
            while(!isauth){
                auth();
                udp.sendCommand(new Request(this.user, "auth", null, null, sender));
                Response resp = udp.receiveResponse();
                if(resp == null) return null;
                String msg = resp.getMsg();
                if(msg.equals("ok")){
                    isauth = true;
                }
                else{
                    io.printError("неверный логин или пароль");
                }
            }
            return this.user;
        }
        else{
            boolean isreg = false;
            while(!isreg){
                reg();
                udp.sendCommand(new Request(this.user, "reg", null, null, sender));
                Response resp = udp.receiveResponse();
                if(resp == null) return null;
                String msg = resp.getMsg();
                if(msg.equals("ok")){
                    isreg = true;
                }
                else if(resp.equals("badlogin")){
                    io.printError("пользователь с таким именем уже существует");
                }
            }
            return this.user;
        }
    }
}
