package user;

public class UserData {
    public static User defaultUser() {
        return new User("Harrier", "tequilasunset123@yandex.ru", "doloresdei");
    }


    public static User userWithoutName() {
        return new User(null, "tequilasunset123@yandex.ru", "doloresdei");
    }

    public static User userWithoutEmail() {
        return new User("Harrier", null, "doloresdei");
    }

    public static User userWithoutPassword() {
        return new User("Harrier", "tequilasunset123@yandex.ru", null);
    }


}