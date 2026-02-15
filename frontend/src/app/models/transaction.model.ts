export interface Transaction {
    type: string;
    accountId: string;
    holderName: string;
    amount: number;
    status: 'SUCCESS' | 'FAILED';
    failureReason: string;
    createdOn: string;
}
