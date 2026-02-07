-- Seed Data for Money Transfer System

CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    holder_name VARCHAR(255) NOT NULL,
    balance DECIMAL(18,2) NOT NULL,
    status ENUM('ACTIVE', 'CLOSED', 'LOCKED') NOT NULL,
    version INT DEFAULT 0 NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Transaction Logs Table
CREATE TABLE IF NOT EXISTS transaction_logs (
    id BINARY(16) PRIMARY KEY,
    from_account BIGINT NOT NULL,
    to_account BIGINT NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    status ENUM('SUCCESS', 'FAILED') NOT NULL,
    failure_reason VARCHAR(255),
    idempotency_key VARCHAR(36) NOT NULL UNIQUE,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (from_account) REFERENCES accounts(id),
    FOREIGN KEY (to_account) REFERENCES accounts(id)
);

-- Clear existing data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE transaction_logs;
TRUNCATE TABLE accounts;
SET FOREIGN_KEY_CHECKS = 1;

-- Insert sample accounts
INSERT INTO accounts (id, holder_name, balance, status, version, last_updated) VALUES
(101, 'John Doe', 10000.00, 'ACTIVE', 0, NOW()),
(102, 'Jane Smith', 5000.00, 'ACTIVE', 0, NOW()),
(103, 'Bob Johnson', 15000.00, 'ACTIVE', 0, NOW()),
(104, 'Alice Williams', 8000.00, 'ACTIVE', 0, NOW()),
(105, 'Charlie Brown', 12000.00, 'LOCKED', 0, NOW()),
(106, 'Diana Prince', 20000.00, 'ACTIVE', 0, NOW()),
(107, 'Eve Davis', 3000.00, 'CLOSED', 0, NOW()),
(108, 'Frank Miller', 7500.00, 'ACTIVE', 0, NOW());