package commands;

import commands.types.Command;
import commands.types.ExtendedCommand;
import udp.Request;
import udp.Response;
import users.User;
import utils.UserManager;

public class Register implements ExtendedCommand {
    private User user;
    private UserManager userManager;

    public Register(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public Response execute() {
        UserStatus res = userManager.newUser(this.user);
        return new Response("reg", res.getDescription(), null);
    }

    @Override
    public Response extendedExecute(Request request) {
        this.user = request.getUser();
        return execute();
    }
}
