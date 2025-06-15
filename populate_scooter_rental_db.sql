-- Inserimento dati di test aggiornati per il database di noleggio scooter

-- Inserimento Titolari
INSERT INTO Titolari (username, password) VALUES
('mario_rossi', 'password123'),
('luigi_bianchi', 'mypass456'),
('anna_verdi', 'secure789'),
('franco_neri', 'admin2024'),
('carla_gialli', 'rental123');

-- Inserimento Agenzie
INSERT INTO agenzie (località, indirizzo, email, numeroDiTelefono, usernameTitolare) VALUES
('Roma', 'Via del Corso 45', 'roma@scooter.it', '06-12345678', 'mario_rossi'),
('Milano', 'Corso Buenos Aires 12', 'milano@scooter.it', '02-87654321', 'luigi_bianchi'),
('Napoli', 'Via Toledo 88', 'napoli@scooter.it', '081-11223344', 'anna_verdi'),
('Firenze', 'Piazza Duomo 7', 'firenze@scooter.it', '055-99887766', 'franco_neri'),
('Venezia', 'Calle Larga 23', 'venezia@scooter.it', '041-55443322', 'carla_gialli'),
('Bologna', 'Via Indipendenza 56', 'bologna@scooter.it', '051-77889900', 'mario_rossi'),
('Torino', 'Via Po 134', 'torino@scooter.it', '011-66554433', 'luigi_bianchi');

-- Inserimento Clienti Registrati
INSERT INTO ClientiRegistrati (nome, cognome, dataDiNascita, email, password) VALUES
('Luca', 'Ferrari', '1990-03-15', 'luca.ferrari@email.it', 'luca123'),
('Giulia', 'Romano', '1988-07-22', 'giulia.romano@email.it', 'giulia456'),
('Marco', 'Conti', '1992-11-08', 'marco.conti@email.it', 'marco789'),
('Sara', 'Ricci', '1985-01-30', 'sara.ricci@email.it', 'sara012'),
('Alessandro', 'Marino', '1995-09-12', 'alex.marino@email.it', 'alex345'),
('Francesca', 'Bruno', '1987-04-25', 'franca.bruno@email.it', 'franca678'),
('Davide', 'Galli', '1993-12-03', 'davide.galli@email.it', 'davide901'),
('Chiara', 'Lombardi', '1991-06-18', 'chiara.lombardi@email.it', 'chiara234'),
('Roberto', 'Esposito', '1989-10-14', 'roberto.esposito@email.it', 'roberto567'),
('Elena', 'Bosco', '1994-02-07', 'elena.Bosco@email.it', 'elena890'),
('Matteo', 'Santoro', '1986-08-29', 'matteo.santoro@email.it', 'matteo123'),
('Valentina', 'Costa', '1996-05-16', 'vale.costa@email.it', 'vale456'),
('Simone', 'Barbieri', '1990-09-23', 'simone.barbieri@email.it', 'simone789'),
('Laura', 'Pellegrini', '1988-12-11', 'laura.pellegrini@email.it', 'laura012'),
('Andrea', 'Moretti', '1992-03-28', 'andrea.moretti@email.it', 'andrea345'),
('Federica', 'Rizzo', '1993-05-14', 'fede.rizzo@email.it', 'fede678'),
('Giacomo', 'Vitale', '1987-09-02', 'giacomo.vitale@email.it', 'giacomo901'),
('Martina', 'De Luca', '1991-11-19', 'martina.deluca@email.it', 'martina234');

-- Inserimento Scooter (Prezzi fissi: Alta Stagione 50€, Bassa Stagione 20€)
INSERT INTO Scooter (targa, cilindrata, prezzoPerGiornoNoleggioAltaStagione, prezzoPerGiornoNoleggioBassaStagione, stato, tipologia, agenziaId) VALUES
-- Roma
('AB123CD', 125, 50.00, 20.00, 'disponibile', 'tradizionale', 1),
('EF456GH', 150, 50.00, 20.00, 'disponibile', 'elettrico', 1),
('IJ789KL', 125, 50.00, 20.00, 'noleggiato', 'tradizionale', 1),
('MN012OP', 300, 50.00, 20.00, 'disponibile', 'tradizionale', 1),
-- Milano
('QR345ST', 125, 50.00, 20.00, 'manutenzione', 'elettrico', 2),
('UV678WX', 150, 50.00, 20.00, 'disponibile', 'tradizionale', 2),
('YZ901AB', 50, 50.00, 20.00, 'disponibile', 'elettrico', 2),
('CD234EF', 125, 50.00, 20.00, 'disponibile', 'tradizionale', 2),
('GH567IJ', 300, 50.00, 20.00, 'noleggiato', 'tradizionale', 2),
-- Napoli
('KL890MN', 150, 50.00, 20.00, 'disponibile', 'elettrico', 3),
('OP123QR', 125, 50.00, 20.00, 'disponibile', 'tradizionale', 3),
('ST456UV', 50, 50.00, 20.00, 'disponibile', 'elettrico', 3),
('WX789YZ', 300, 50.00, 20.00, 'disponibile', 'tradizionale', 3),
-- Firenze
('AB012CD', 150, 50.00, 20.00, 'manutenzione', 'elettrico', 4),
('EF345GH', 125, 50.00, 20.00, 'disponibile', 'tradizionale', 4),
('IJ678KL', 50, 50.00, 20.00, 'noleggiato', 'elettrico', 4),
('MN901OP', 300, 50.00, 20.00, 'disponibile', 'tradizionale', 4),
-- Venezia
('QR234ST', 125, 50.00, 20.00, 'disponibile', 'elettrico', 5),
('UV567WX', 150, 50.00, 20.00, 'disponibile', 'tradizionale', 5),
('YZ890AB', 50, 50.00, 20.00, 'disponibile', 'elettrico', 5),
('CD123EF', 125, 50.00, 20.00, 'disponibile', 'tradizionale', 5),
-- Bologna
('GH456IJ', 150, 50.00, 20.00, 'disponibile', 'elettrico', 6),
('KL789MN', 300, 50.00, 20.00, 'disponibile', 'tradizionale', 6),
('OP012QR', 125, 50.00, 20.00, 'noleggiato', 'elettrico', 6),
-- Torino
('ST345UV', 150, 50.00, 20.00, 'disponibile', 'tradizionale', 7),
('WX678YZ', 50, 50.00, 20.00, 'disponibile', 'elettrico', 7),
('AB901CD', 300, 50.00, 20.00, 'manutenzione', 'tradizionale', 7);

-- Inserimento Accessori (divisi tra Optional e Servizi Assicurativi)

INSERT INTO Accessori (descrizione, prezzo, tipo) VALUES
-- Optional
('Casco integrale', 5.00, 'optional'),
('Bauletto posteriore', 10.00, 'optional'),
('Navigatore GPS', 18.00, 'optional'),
('Parabrezza antivento', 10.00, 'optional'),

-- Servizi Assicurativi
('Secondo guidatore', 15.00, 'servizio_assicurativo'),
('Assicurazione infortuni', 12.00, 'servizio_assicurativo'),
('Copertura danni accidentali', 20.00, 'servizio_assicurativo');

-- Inserimento Prenotazioni (mix di periodi alta/bassa stagione)
INSERT INTO Prenotazioni (dataRitiro, dataConsegna, costoTotale, clienteRegistratoId, scooterTarga) VALUES
-- Bassa stagione (prezzo 20€/giorno)
('2025-03-10', '2025-03-12', 57.00, 1, 'AB123CD'), -- 2 giorni + accessori
('2025-04-15', '2025-04-20', 127.00, 2, 'IJ789KL'), -- 5 giorni + accessori
('2025-05-08', '2025-05-10', 58.00, 3, 'GH567IJ'), -- 2 giorni + accessori
('2025-02-12', '2025-02-15', 78.00, 4, 'IJ678KL'), -- 3 giorni + accessori
-- Alta stagione (prezzo 50€/giorno)  
('2025-07-10', '2025-07-15', 285.00, 5, 'EF456GH'), -- 5 giorni + accessori
('2025-08-16', '2025-08-18', 132.00, 6, 'CD234EF'), -- 2 giorni + accessori
('2025-06-20', '2025-06-25', 295.00, 7, 'KL890MN'), -- 5 giorni + accessori
('2025-07-22', '2025-07-24', 142.00, 8, 'OP123QR'), -- 2 giorni + accessori
-- Mix stagioni
('2025-05-28', '2025-06-03', 210.00, 9, 'EF345GH'), -- 6 giorni (3 bassa + 3 alta) + accessori
('2025-08-25', '2025-09-02', 310.00, 10, 'UV567WX'), -- 8 giorni (4 alta + 4 bassa) + accessori
('2025-07-05', '2025-07-12', 395.00, 11, 'CD123EF'), -- 7 giorni alta + accessori
('2025-04-28', '2025-05-05', 167.00, 12, 'QR234ST'), -- 7 giorni bassa + accessori
('2025-06-12', '2025-06-19', 385.00, 13, 'GH456IJ'), -- 7 giorni alta + accessori
('2025-03-20', '2025-03-27', 152.00, 14, 'YZ890AB'), -- 7 giorni bassa + accessori
('2025-08-10', '2025-08-17', 398.00, 15, 'ST345UV'), -- 7 giorni alta + accessori
('2025-05-15', '2025-05-22', 168.00, 16, 'WX678YZ'), -- 7 giorni bassa + accessori
('2025-07-28', '2025-08-01', 248.00, 17, 'OP012QR'), -- 4 giorni alta + accessori
('2025-04-10', '2025-04-14', 98.00, 18, 'KL789MN'); -- 4 giorni bassa + accessori

-- Inserimento Prenotazioni_Accessori (mix di optional e servizi assicurativi)
IINSERT INTO Prenotazioni_Accessori (prenotazioneId, accessorioId) VALUES
-- Prenotazione 1 (Luca Ferrari - 2 giorni bassa stagione)
(1, 1), -- Casco integrale
(1, 2), -- Bauletto posteriore
(1, 5), -- Secondo guidatore

-- Prenotazione 2 (Giulia Romano - 5 giorni bassa stagione)
(2, 1), -- Casco integrale
(2, 2), -- Bauletto posteriore
(2, 3), -- Navigatore GPS
(2, 6), -- Assicurazione infortuni
(2, 7), -- Copertura danni accidentali

-- Prenotazione 3 (Marco Conti - 2 giorni bassa stagione)
(3, 1), -- Casco integrale
(3, 2), -- Bauletto posteriore
(3, 5), -- Secondo guidatore

-- Prenotazione 4 (Sara Ricci - 3 giorni bassa stagione)
(4, 1), -- Casco integrale
(4, 4), -- Parabrezza antivento
(4, 6), -- Assicurazione infortuni

-- Prenotazione 5 (Alessandro Marino - 5 giorni alta stagione)
(5, 1), -- Casco integrale
(5, 2), -- Bauletto posteriore
(5, 3), -- Navigatore GPS
(5, 7), -- Copertura danni accidentali

-- Prenotazione 6 (Francesca Bruno - 2 giorni alta stagione)
(6, 1), -- Casco integrale
(6, 2), -- Bauletto posteriore
(6, 6), -- Assicurazione infortuni

-- Prenotazione 7 (Davide Galli - 5 giorni alta stagione)
(7, 1), -- Casco integrale
(7, 3), -- Navigatore GPS
(7, 5), -- Secondo guidatore
(7, 7), -- Copertura danni accidentali

-- Prenotazione 8 (Chiara Lombardi - 2 giorni alta stagione)
(8, 1), -- Casco integrale
(8, 3), -- Navigatore GPS
(8, 6), -- Assicurazione infortuni

-- Prenotazione 9 (Roberto Esposito - 6 giorni mix)
(9, 1), -- Casco integrale
(9, 2), -- Bauletto posteriore
(9, 5), -- Secondo guidatore
(9, 7), -- Copertura danni accidentali

-- Prenotazione 10 (Elena Bosco - 8 giorni mix)
(10, 1), -- Casco integrale
(10, 3), -- Navigatore GPS
(10, 6), -- Assicurazione infortuni
(10, 7), -- Copertura danni accidentali

-- Prenotazione 11 (Matteo Santoro - 7 giorni alta)
(11, 1), -- Casco integrale
(11, 2), -- Bauletto posteriore
(11, 4), -- Parabrezza antivento
(11, 5), -- Secondo guidatore
(11, 7), -- Copertura danni accidentali

-- Prenotazione 12 (Valentina Costa - 7 giorni bassa)
(12, 1), -- Casco integrale
(12, 3), -- Navigatore GPS
(12, 6), -- Assicurazione infortuni

-- Prenotazione 13 (Simone Barbieri - 7 giorni alta)
(13, 1), -- Casco integrale
(13, 2), -- Bauletto posteriore
(13, 6), -- Assicurazione infortuni
(13, 7), -- Copertura danni accidentali

-- Prenotazione 14 (Laura Pellegrini - 7 giorni bassa)
(14, 1), -- Casco integrale
(14, 4), -- Parabrezza antivento
(14, 5), -- Secondo guidatore
(14, 6), -- Assicurazione infortuni

-- Prenotazione 15 (Andrea Moretti - 7 giorni alta)
(15, 1), -- Casco integrale
(15, 2), -- Bauletto posteriore
(15, 3), -- Navigatore GPS
(15, 7), -- Copertura danni accidentali

-- Prenotazione 16 (Federica Rizzo - 7 giorni bassa)
(16, 1), -- Casco integrale
(16, 4), -- Parabrezza antivento
(16, 6), -- Assicurazione infortuni

-- Prenotazione 17 (Giacomo Vitale - 4 giorni alta)
(17, 1), -- Casco integrale
(17, 3), -- Navigatore GPS
(17, 5), -- Secondo guidatore
(17, 7), -- Copertura danni accidentali

-- Prenotazione 18 (Martina De Luca - 4 giorni bassa)
(18, 1), -- Casco integrale
(18, 2), -- Bauletto posteriore
(18, 6); -- Assicurazione infortuni