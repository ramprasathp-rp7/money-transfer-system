import { Routes } from '@angular/router';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { TransferComponent } from './components/transfer/transfer.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'transactions', component: TransactionListComponent, canActivate: [authGuard] },
    { path: 'transfer', component: TransferComponent, canActivate: [authGuard] },
    { path: '**', redirectTo: '/login' }
];