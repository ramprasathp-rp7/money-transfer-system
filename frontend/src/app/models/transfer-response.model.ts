export interface TransferResponse {
    transactionId: string;
    status: string;
    message: string;
    debitedFrom: string;
    creditedTo: string;
    amount: number;
}
