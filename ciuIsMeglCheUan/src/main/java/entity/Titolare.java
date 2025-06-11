package entity;

public class Titolare {
    private String username;
    private String password;

    public Titolare(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
}
