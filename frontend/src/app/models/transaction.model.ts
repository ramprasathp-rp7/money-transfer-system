export interface Transaction {
    type: string;
    accountId: string;
    holderName: string;
    amount: number;
    status: string;
    createdOn: string;
}
