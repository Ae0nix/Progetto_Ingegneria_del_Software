-- Script di inizializzazione per il database di noleggio scooter

CREATE TABLE Titolari (
    username VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20) NOT NULL
);

CREATE TABLE agenzie (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    località VARCHAR(20) NOT NULL,
    indirizzo VARCHAR(20) NOT NULL,
    email VARCHAR(20),
    numeroDiTelefono VARCHAR(20),
    usernameTitolare VARCHAR(20),
    FOREIGN KEY (usernameTitolare) REFERENCES Titolari(username)
);

CREATE TABLE ClientiRegistrati (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(20) NOT NULL,
    cognome VARCHAR(20) NOT NULL,
    dataDiNascita VARCHAR(10) NOT NULL,
    email VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(20) NOT NULL
);

CREATE TABLE Scooter (
    targa VARCHAR(10) PRIMARY KEY,
    cilindrata INT(10) NOT NULL,
    prezzoPerGiornoNoleggioAltaStagione FLOAT(8) NOT NULL,
    prezzoPerGiornoNoleggioBassaStagione FLOAT(8) NOT NULL,
    stato VARCHAR(20) NOT NULL,
    tipologia VARCHAR(20),
    agenziaId INT(10),
    FOREIGN KEY (agenziaId) REFERENCES agenzie(id)
);

CREATE TABLE Accessori (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    descrizione VARCHAR(30) NOT NULL,
    prezzo FLOAT(8) NOT NULL,
    tipo VARCHAR(30) NOT NULL
);

CREATE TABLE Prenotazioni (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    dataRitiro VARCHAR(10) NOT NULL,
    dataConsegna VARCHAR(10) NOT NULL,
    costoTotale FLOAT(8) NOT NULL,
    clienteRegistratoId INT(10),
    scooterTarga VARCHAR(10),
    FOREIGN KEY (clienteRegistratoId) REFERENCES ClientiRegistrati(id),
    FOREIGN KEY (scooterTarga) REFERENCES Scooter(targa)
);

CREATE TABLE Prenotazioni_Accessori (
    prenotazioneId INT(10),
    accessorioId INT(10),
    PRIMARY KEY (prenotazioneId, accessorioId),
    FOREIGN KEY (prenotazioneId) REFERENCES Prenotazioni(id) ON DELETE CASCADE,
    FOREIGN KEY (accessorioId) REFERENCES Accessori(id)
);