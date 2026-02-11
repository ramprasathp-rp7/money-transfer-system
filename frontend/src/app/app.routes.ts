import { Routes } from '@angular/router';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { TransferComponent } from './components/transfer/transfer.component';
import { LoginComponent } from './components/login/login.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'transactions', component: TransactionListComponent },
    { path: 'transfer', component: TransferComponent },
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
