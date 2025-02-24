package org.unict.dieei.dto;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int role; // 0 = Admin, 1 = Tecnico IT, 2 = Cliente

    public User(int id, String name, String email, String password, int role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String name, String email, String password, int role) {
        this(0, name, email, password, role);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }
}
