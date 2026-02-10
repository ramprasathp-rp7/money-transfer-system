import { Routes } from '@angular/router';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { TransferComponent } from './components/transfer/transfer.component';

export const routes: Routes = [
    { path: 'transactions', component: TransactionListComponent },
    { path: 'transfer', component: TransferComponent },
    { path: '', redirectTo: '/transactions', pathMatch: 'full' }
];
