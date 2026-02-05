-- Database Schema for Money Transfer System

CREATE DATABASE IF NOT EXISTS money_transfer_db;
USE money_transfer_db;

-- Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    holder_name VARCHAR(255) NOT NULL,
    balance DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    version INT DEFAULT 0 NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status)
);

-- Transaction Logs Table
CREATE TABLE IF NOT EXISTS transaction_logs (
    id VARCHAR(36) PRIMARY KEY,
    from_account BIGINT NOT NULL,
    to_account BIGINT NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    failure_reason VARCHAR(255),
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (from_account) REFERENCES accounts(id),
    FOREIGN KEY (to_account) REFERENCES accounts(id),
    INDEX idx_from_account (from_account),
    INDEX idx_to_account (to_account),
    INDEX idx_idempotency_key (idempotency_key),
    INDEX idx_created_on (created_on)
);