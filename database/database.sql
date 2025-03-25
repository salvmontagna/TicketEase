CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role INTEGER NOT NULL CHECK(role IN (0,1,2))  -- 0 = Admin, 1 = Tecnico, 2 = Cliente
);

CREATE TABLE IF NOT EXISTS tickets (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK(status IN ('open', 'in_progress', 'closed')),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_user_id INTEGER NOT NULL,
    assigned_user_id INTEGER DEFAULT NULL,
    product_id INTEGER NOT NULL,
    FOREIGN KEY (created_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ticket_status (
    id SERIAL PRIMARY KEY,
    ticket_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL CHECK(status IN ('open', 'in_progress', 'closed')),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by INTEGER NOT NULL,
    status_description VARCHAR NOT NULL DEFAULT '',
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notifications (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    ticket_id INTEGER NOT NULL,
    recipient_id INTEGER NOT NULL,
    sent_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS authorizations (
    id SERIAL PRIMARY KEY,
    tax_code VARCHAR(16) UNIQUE NOT NULL,
    secret_key VARCHAR(255) NOT NULL,
    role INTEGER NOT NULL CHECK(role IN (0,1))
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(30) UNIQUE NOT NULL
);