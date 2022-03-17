package utils;

import commands.UserStatus;
import users.User;

public class UserManager {
    private DBmanager dBmanager;
    private PasswordManager passwordManager;

    public UserManager(DBmanager dBmanager) {
        this.dBmanager = dBmanager;
        this.passwordManager = new PasswordManager();
    }

    public UserStatus newUser(User user){
        String encoded = passwordManager.shacode(user.getPassword());
        user.setPassword(encoded);
        return dBmanager.addUser(user);
    }

    public UserStatus authUser(User user){
        String encoded = passwordManager.shacode(user.getPassword());
        user.setPassword(encoded);
        return dBmanager.checkUser(user);
    }
}
