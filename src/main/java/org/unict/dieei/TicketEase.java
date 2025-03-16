package org.unict.dieei;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.unict.dieei.domain.Products;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.*;
import org.unict.dieei.service.*;

import java.util.List;
import java.util.Scanner;

public class TicketEase {

    private static Scanner scanner = new Scanner(System.in);
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticket-ease");
    private static EntityManager em = emf.createEntityManager();

    private static AuthorizationService authService = new AuthorizationService(new AuthorizationDAO(em));
    private static ProductsService productsService = new ProductsService(new ProductsDAO(em));
    private static UserService userService = new UserService(new UserDAO(em), authService);
    private static TicketService ticketService = new TicketService(em, new TicketDAO(em), productsService, new UserDAO(em));
    private static TicketStatusService ticketStatusService = new TicketStatusService(new TicketStatusDAO(em), new TicketDAO(em) );

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== TicketEase - Sistema di Ticketing =====");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    signinPage();
                    break;
                case 2:
                    signupPage();
                    break;
                case 0:
                    System.out.println("Chiusura del sistema...");
                    emf.close();
                    System.exit(0);
                default:
                    System.out.println("Opzione non valida.");
            }
        }
    }

    private static void signinPage() {
        System.out.print("\nInserisci email: ");
        String email = scanner.nextLine();
        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        User user = userService.loginUser(email, password);
        if (user != null) {
            System.out.println("\nLogin riuscito! Benvenuto, " + user.getName());
            if(user.getRole() == 0) adminMenu();
            else if(user.getRole() == 1) technicianMenu(user);
            else if(user.getRole() == 2) clientMenu(user);
        } else {
            System.out.println("Credenziali errate.");
        }
    }

    private static void signupPage() {
        System.out.print("\nInserisci nome utente: ");
        String username = scanner.nextLine();
        System.out.print("Inserisci email: ");
        String email = scanner.nextLine();
        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        System.out.println("Seleziona ruolo: 0 = Amministratore, 1 = Tecnico IT, 2 = Cliente");
        int role = scanner.nextInt();
        scanner.nextLine(); // Consuma il newline rimasto nel buffer

        String taxCode = null;
        String secretKey = null;

        // Se il ruolo è Admin (0) o Tecnico IT (1), chiedi codice fiscale e chiave segreta
        if (role == 0 || role == 1) {
            System.out.print("Inserisci il Codice Fiscale: ");
            taxCode = scanner.nextLine();
            System.out.print("Inserisci la Chiave Segreta: ");
            secretKey = scanner.nextLine();
        }

        userService.registerUser(username, email, password, role, taxCode, secretKey);
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

    private static void adminMenu() {
        while (true) {
            System.out.println("\n===== Menu Amministratore =====");
            System.out.println("1. Assegna un ticket");
            System.out.println("2. Crea un ticket e assegnalo");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    assignTicket();
                    break;
                case 2:
                    createAndAssignTicket();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opzione non valida.");
            }
        }
    }

    private static void assignTicket() {

        List<Ticket> tickets = ticketService.getAllOpenedTickets();
        for (Ticket ticket : tickets) {
            System.out.println(ticket + "\n");
        }

        System.out.print("Inserisci l'ID del ticket da assegnare: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine();

        List<User> technicians = userService.findAllTechnicians();
        System.out.println("Seleziona un tecnico IT:");
        for (User tech : technicians) {
            System.out.println(tech.getId() + ". " + tech.getName());
        }
        System.out.print("ID tecnico: ");
        int techId = scanner.nextInt();
        scanner.nextLine();

        ticketService.assignTicket(ticketId, techId);
        System.out.println("Ticket assignato con successo!");
    }

    private static void viewTickets(User user) {
        System.out.println("\n1. Visualizza tutti i ticket");
        System.out.println("2. Visualizza solo i ticket aperti");
        System.out.println("3. Visualizza solo i ticket chiusi");
        System.out.print("Scelta: ");

        int filtro = scanner.nextInt();
        scanner.nextLine();

        List<Ticket> tickets = ticketService.getTicketsByUser(user.getId(), filtro);
        for (int i = 0; i < tickets.size(); i++) {
            System.out.println("=========== Ticket n. " + (i + 1) + " ===========");
            System.out.println(tickets.get(i));
            System.out.println("==================================");
        }
    }

    private static void createAndAssignTicket() {

        showAvailableProducts();

        System.out.println("Digitare il numero del prodotto in cui si è riscontrato il problema: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Inserisci il titolo del ticket: ");
        String title = scanner.nextLine();
        System.out.print("Inserisci la descrizione del problema: ");
        String description = scanner.nextLine();

        System.out.println("\nSeleziona un cliente per cui creare il ticket:");
        List<User> clients = userService.findAllCustomers(); // Ottieni tutti gli utenti, filtreremo i clienti
        for (User client : clients) {
            System.out.println(client.getId() + ". " + client.getName());
        }
        System.out.print("ID Cliente: ");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nSeleziona un tecnico IT per assegnare il ticket:");
        List<User> technicians = userService.findAllTechnicians();
        for (User tech : technicians) {
            System.out.println(tech.getId() + ". " + tech.getName());
        }
        System.out.print("ID Tecnico: ");
        int technicianId = scanner.nextInt();
        scanner.nextLine();

        User client = userService.findById(clientId);

        ticketService.createAndAssignTicket(title, description, client, technicianId, productId);
        System.out.println("Ticket assignato con successo!");
    }

    private static void technicianMenu(User user) {
        while (true) {
            System.out.println("\n===== Menu Tecnico IT =====");
            System.out.println("1. Visualizza i ticket assegnati");
            System.out.println("2. Aggiorna stato di un ticket");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    showAssignedTickets(user.getId());
                    break;
                case 2:
                    updateTicketStatus(user);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opzione non valida.");
            }
        }
    }

    private static void createTicket(User user){
        showAvailableProducts();

        System.out.println("Digitare il numero del prodotto in cui si è riscontrato il problema: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Inserisci il titolo del ticket: ");
        String title = scanner.nextLine();
        System.out.print("Inserisci la descrizione del problema: ");
        String description = scanner.nextLine();
        ticketService.createTicket(title, description, user, productId);
        System.out.println("Ticket creato con successo!");
    }

    private static void updateTicketStatus(User user) {

        showAssignedTickets(user.getId());

        System.out.print("Inserisci l'ID del ticket da aggiornare: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("ID del nuovo stato (1. Aperto, 2. In corso, 3. Chiuso): ");
        int statusNumber = scanner.nextInt();
        scanner.nextLine();

        String status = getRealStatus(statusNumber);

        ticketStatusService.updateTicketStatus(ticketId, status, user.getId());
    }

    private static void showAssignedTickets(int technicianId) {
        List<Ticket> tickets = ticketService.getAssignedTickets(technicianId);
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    private static void showAvailableProducts() {
        List<Products> products = productsService.getAllProducts();
        System.out.println("Lista Prodotti Disponibili:");
        products.forEach(product -> System.out.println(product.getId() + ": " + product.getProductName()));
    }

    private static String getRealStatus(int statusNumber){
        String status = "";
        switch (statusNumber) {
            case 1:
                status = "open";
                break;
            case 2:
                status = "in_progress";
                break;
            case 3:
                status = "closed";
                break;
            default:
                System.out.println("Stato non riconosciuto.");
        }

        return status;
    }

}

