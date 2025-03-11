package org.unict.dieei.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 20)
    private String status = "open";

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_user_id", nullable = false)
    private User createdUser;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    // Costruttore senza argomenti per Hibernate
    public Ticket() {}

    // Costruttore con parametri
    public Ticket(String title, String description, User createdUser) {
        this.title = title;
        this.description = description;
        this.createdUser = createdUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    /*
    @Override
    public String toString() {
        return "Ticket ID: " + id + "\n"
                + "Titolo: " + title + "\n"
                + "Descrizione: " + description + "\n"
                + "Stato: " + status + "\n"
                + "Data Creazione: " + creationDate + "\n"
                + "Creato da User ID: " + createdUserId ;
    }*/

    @Override
    public String toString() {
        return "Ticket ID: " + id + " - "
                + "Titolo: " + title + " - "
                + "Descrizione: " + description + " - "
                + "Stato: " + status + " - "
                + "Data Creazione: " + creationDate + " - "
                + "Creato da User ID: " + createdUser.getId();
    }


}

