package entity;

public class ClienteRegistrato {
    private int id;
    private String nome;
    private String cognome;
    private String dataDiNascita;
    private String email;
    private String password;

    public ClienteRegistrato(String nome, String cognome, String dataDiNascita, String email, String password) {
        this.id = null;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }
}
