import { Component, OnInit, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionService } from '../../services/transaction.service';
import { Transaction } from '../../models/transaction.model';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-transaction-list',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './transaction-list.component.html',
    styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {
    private allTransactions = signal<Transaction[]>([]);
    errorMessage = signal<string>('');
    accountId: string = '';

    // Filter State
    isFilterMenuOpen = signal<boolean>(false);
    activeFilterCategory = signal<'Status' | 'Type'>('Status');
    selectedStatusFilters = signal<string[]>([]);
    selectedTypeFilters = signal<string[]>([]);

    // Computed signal for filtered and sorted transactions
    transactions = computed(() => {
        let data = [...this.allTransactions()];

        // Apply Status Filter
        if (this.selectedStatusFilters().length > 0) {
            data = data.filter(t => this.selectedStatusFilters().includes(t.status.toUpperCase()));
        }

        // Apply Type Filter
        if (this.selectedTypeFilters().length > 0) {
            data = data.filter(t => {
                const type = t.type.toUpperCase();
                return this.selectedTypeFilters().includes(type);
            });
        }

        return data;
    });

    constructor(
        private transactionService: TransactionService,
        private authService: AuthService
    ) { }

    ngOnInit(): void {
        this.accountId = this.authService.username || '';
        this.fetchTransactions();
    }

    fetchTransactions(): void {
        this.transactionService.getTransactions(this.accountId).subscribe({
            next: (data) => {
                this.allTransactions.set(data);
            },
            error: (error) => {
                this.errorMessage.set('Failed to load transactions. Please check your credentials or API connection.');
                console.error('Error fetching transactions:', error);
            }
        });
    }

    toggleFilterMenu(): void {
        this.isFilterMenuOpen.update(v => !v);
    }

    setFilterCategory(category: 'Status' | 'Type'): void {
        this.activeFilterCategory.set(category);
    }

    toggleStatusFilter(value: string): void {
        this.selectedStatusFilters.update(filters =>
            filters.includes(value) ? filters.filter(f => f !== value) : [...filters, value]
        );
    }

    toggleTypeFilter(value: string): void {
        this.selectedTypeFilters.update(filters =>
            filters.includes(value) ? filters.filter(f => f !== value) : [...filters, value]
        );
    }

    clearFilters(): void {
        this.selectedStatusFilters.set([]);
        this.selectedTypeFilters.set([]);
    }

    sort(property: keyof Transaction, order: 'asc' | 'desc'): void {
        const sorted = [...this.allTransactions()].sort((a, b) => {
            const valueA = a[property];
            const valueB = b[property];

            if (valueA < valueB) {
                return order === 'asc' ? -1 : 1;
            }
            if (valueA > valueB) {
                return order === 'asc' ? 1 : -1;
            }
            return 0;
        });
        this.allTransactions.set(sorted);
    }
}
