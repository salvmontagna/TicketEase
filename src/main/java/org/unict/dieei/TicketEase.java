package org.unict.dieei;

import org.unict.dieei.dto.Ticket;
import org.unict.dieei.dto.User;
import org.unict.dieei.persistence.TicketDAO;
import org.unict.dieei.persistence.UserDAO;

import java.util.List;
import java.util.Scanner;

public class TicketEase {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== TicketEase - Sistema di Ticketing =====");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            try{
                switch (scelta) {
                    case 1:
                        login();
                        break;
                    case 2:
                        registerUser();
                        break;
                    case 0:
                        System.out.println("Chiusura del sistema...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opzione non valida.");
                }
            } catch (Exception e) {
                System.out.println("Inserisci uno dei numeri elencati qui sopra.");
                scanner.nextLine();
            }
        }
    }

    private static void login() {
        System.out.print("\nInserisci email: ");
        String email = scanner.nextLine();
        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        User user = UserDAO.loginUser(email, password);
        if (user != null) {
            System.out.println("\nLogin riuscito! Benvenuto, " + user.getName());
            switch (user.getRole()) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    clientMenu(user);
                    break;
                default:
                    System.out.println("Ruolo non riconosciuto.");
            }
        } else {
            System.out.println("Credenziali errate.");
        }
    }

    private static void registerUser() {
        System.out.print("\nInserisci nome: ");
        String name = scanner.nextLine();
        System.out.print("Inserisci email: ");
        String email = scanner.nextLine();
        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        System.out.println("Seleziona ruolo: 0 = Amministratore, 1 = Tecnico IT, 2 = Cliente");
        int role = scanner.nextInt();
        scanner.nextLine();

        String taxCode = null;
        String secretKey = null;

        // Se il ruolo è Admin o Tecnico IT, chiedi il codice fiscale e la secret key
        if (role == 0 || role == 1) {
            System.out.print("Inserisci il codice fiscale: ");
            taxCode = scanner.nextLine();
            System.out.print("Inserisci la secret key: ");
            secretKey = scanner.nextLine();
        }

        User user = UserDAO.registerUser(name, email, password, role, taxCode, secretKey);
        if (user != null) {
            System.out.println("Registrazione avvenuta con successo!");
        } else {
            System.out.println("Errore durante la registrazione.");
        }
    }

    private static void clientMenu(User user) {
        while (true) {
            System.out.println("\n===== Menu Cliente =====");
            System.out.println("1. Crea un nuovo ticket");
            System.out.println("2. Visualizza i ticket");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    createTicket(user);
                    break;
                case 2:
                    viewTickets(user);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opzione non valida.");
            }
        }
    }

    private static void createTicket(User user) {
        System.out.print("Inserisci il titolo del ticket: ");
        String title = scanner.nextLine();
        System.out.print("Inserisci la descrizione del problema: ");
        String description = scanner.nextLine();

        Ticket ticket = TicketDAO.createTicket(title, description, user.getId());
        if (ticket != null) {
            System.out.println("Ticket " + ticket.getId() + " creato con successo!");
        } else {
            System.out.println("Errore nella creazione del ticket.");
        }
    }

    private static void viewTickets(User user) {
        System.out.println("\n1. Visualizza tutti i ticket");
        System.out.println("2. Visualizza solo i ticket aperti");
        System.out.println("3. Visualizza solo i ticket chiusi");
        System.out.print("Scelta: ");

        int filtro = scanner.nextInt();
        scanner.nextLine();

        List<Ticket> tickets = TicketDAO.getTicketsByUser(user.getId(), filtro);
        for (int i = 0; i < tickets.size(); i++) {
            System.out.println("=========== Ticket n. " + (i + 1) + " ===========");
            System.out.println(tickets.get(i));
            System.out.println("==================================");
        }
    }

}
