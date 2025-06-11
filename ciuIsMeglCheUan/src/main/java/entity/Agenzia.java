package entity;

public class Agenzia {
    private int id;
    private String localita;
    private String indirizzo;
    private String email;
    private String numeroDiTelefono;
    private Titolare titolare;
    private String usernameTitolare;

    public Agenzia(int id, String localita, String indirizzo, String email, String numeroDiTelefono, String usernameTitolare, String passwordTitolare) {
        this.id = id;
        this.localita = localita;
        this.indirizzo = indirizzo;
        this.email = email;
        this.numeroDiTelefono = numeroDiTelefono;
        this.titolare = new Titolare(usernameTitolare, passwordTitolare);
        this.usernameTitolare=usernameTitolare;
    }

    public int getId() {
        return id;
    }

    public String getLocalita() {
        return localita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getEmail() {
        return email;
    }

    public String getNumeroDiTelefono() {
        return numeroDiTelefono;
    }

    public String getUsernameTitolare() {
        return usernameTitolare;
    }
}
