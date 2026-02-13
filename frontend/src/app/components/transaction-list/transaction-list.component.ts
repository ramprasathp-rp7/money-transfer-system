import { Component, OnInit, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionService } from '../../services/transaction.service';
import { Transaction } from '../../models/transaction.model';
import { Account } from '../../models/account.model';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-transaction-list',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './transaction-list.component.html',
    styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {
    protected Math = Math;
    private allTransactions = signal<Transaction[]>([]);
    senderAccount = signal<Account | null>(null);
    errorMessage = signal<string>('');
    accountId: string = '';

    // Pagination State
    currentPage = signal<number>(1);
    pageSize = signal<number>(6); // Show only 6 records per page to ensure no-scroll

    // Filter State
    searchTerm = signal<string>('');
    isFilterMenuOpen = signal<boolean>(false);
    activeFilterCategory = signal<'Status' | 'Type' | 'Date'>('Status');
    selectedStatusFilters = signal<string[]>([]);
    selectedTypeFilters = signal<string[]>([]);
    dateSortOrder = signal<'asc' | 'desc'>('desc');

    // Computed signal for filtered and sorted transactions with running balance
    filteredTransactions = computed(() => {
        let data = [...this.allTransactions()];

        // Apply Date Sort
        data.sort((a, b) => {
            const dateA = new Date(a.createdOn).getTime();
            const dateB = new Date(b.createdOn).getTime();
            return this.dateSortOrder() === 'desc' ? dateB - dateA : dateA - dateB;
        });

        // Apply Search Filter
        if (this.searchTerm()) {
            const term = this.searchTerm().toLowerCase();
            data = data.filter(t =>
                t.holderName.toLowerCase().includes(term) ||
                t.status.toLowerCase().includes(term) ||
                t.type.toLowerCase().includes(term)
            );
        }

        // Apply Status Filter
        if (this.selectedStatusFilters().length > 0) {
            data = data.filter(t => this.selectedStatusFilters().includes(t.status.toUpperCase()));
        }

        // Apply Type Filter
        if (this.selectedTypeFilters().length > 0) {
            this.currentPage.set(1); // Reset to page 1 on filter
            data = data.filter(t => {
                const type = t.type.toUpperCase();
                return this.selectedTypeFilters().includes(type);
            });
        }

        // Calculate running balance for ALL filtered transactions
        // We start from current balance and go backwards if the list is sorted latest-first.
        // But running balance usually means balance *after* that transaction at that point in time.
        // If current balance is B, and latest transaction was T1 (amount A1),
        // then balance after T1 is B.
        // Balance after T2 (the one before T1) was B - (amount of T1 if credit, + if debit).

        const currentBalance = this.senderAccount()?.balance || 0;
        let running = currentBalance;

        return data.map(t => {
            const balAfter = running;
            // Update running for the NEXT row (which is a PREVIOUS transaction in time)
            if (t.type === 'RECEIVE') {
                running -= t.amount;
            } else {
                running += t.amount;
            }
            return { ...t, runningBalance: balAfter };
        });
    });

    totalPages = computed(() => Math.ceil(this.filteredTransactions().length / this.pageSize()) || 1);

    transactions = computed(() => {
        const start = (this.currentPage() - 1) * this.pageSize();
        return this.filteredTransactions().slice(start, start + this.pageSize());
    });

    nextPage(): void {
        if (this.currentPage() < this.totalPages()) {
            this.currentPage.update(p => p + 1);
        }
    }

    prevPage(): void {
        if (this.currentPage() > 1) {
            this.currentPage.update(p => p - 1);
        }
    }

    goToPage(page: number): void {
        this.currentPage.set(page);
    }

    get username(): string {
        const name = this.authService.username;
        return name ? name.charAt(0).toUpperCase() + name.slice(1) : 'User';
    }

    get greeting(): string {
        const hour = new Date().getHours();
        if (hour < 12) return 'Good Morning';
        if (hour < 17) return 'Good Afternoon';
        return 'Good Evening';
    }

    constructor(
        private transactionService: TransactionService,
        private authService: AuthService
    ) { }

    ngOnInit(): void {
        this.accountId = this.authService.username || '';
        this.fetchTransactions();
        this.fetchAccountDetails();
    }

    fetchAccountDetails(): void {
        if (!this.accountId) return;
        this.transactionService.getAccountDetails(this.accountId).subscribe({
            next: (account) => {
                this.senderAccount.set(account);
            },
            error: (error) => {
                console.error('Error fetching account details:', error);
            }
        });
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

    setFilterCategory(category: 'Status' | 'Type' | 'Date'): void {
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

    setDateSort(order: 'asc' | 'desc'): void {
        this.dateSortOrder.set(order);
    }

    clearFilters(): void {
        this.selectedStatusFilters.set([]);
        this.selectedTypeFilters.set([]);
        this.searchTerm.set('');
        this.dateSortOrder.set('desc');
    }

    onSearch(event: Event): void {
        const value = (event.target as HTMLInputElement).value;
        this.searchTerm.set(value);
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
