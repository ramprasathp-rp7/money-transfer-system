-- Seed Data for Money Transfer System

USE money_transfer_db;

-- Clear existing data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE transaction_logs;
TRUNCATE TABLE accounts;
SET FOREIGN_KEY_CHECKS = 1;

-- Insert sample accounts
INSERT INTO accounts (id, holder_name, balance, status, version) VALUES
(1, 'John Doe', 10000.00, 'ACTIVE', 0),
(2, 'Jane Smith', 5000.00, 'ACTIVE', 0),
(3, 'Bob Johnson', 15000.00, 'ACTIVE', 0),
(4, 'Alice Williams', 8000.00, 'ACTIVE', 0),
(5, 'Charlie Brown', 12000.00, 'LOCKED', 0),
(6, 'Diana Prince', 20000.00, 'ACTIVE', 0),
(7, 'Eve Davis', 3000.00, 'CLOSED', 0),
(8, 'Frank Miller', 7500.00, 'ACTIVE', 0);

-- Verify data
SELECT * FROM accounts;