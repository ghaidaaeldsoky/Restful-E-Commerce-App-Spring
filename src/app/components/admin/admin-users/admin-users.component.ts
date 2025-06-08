import { Component, OnInit } from '@angular/core';

interface User {
  username: string;
  mobile: string;
  email: string;
  creditLimit: number;
  role: 'admin' | 'user';
}


@Component({
  selector: 'app-admin-users',
  standalone: false,
  templateUrl: './admin-users.component.html',
  styleUrl: './admin-users.component.css'
})


export class AdminUsersComponent implements OnInit{
  users: User[] = [];
  filteredUsers: User[] = [];
  paginatedUsers: User[] = [];

  currentPage = 1;
  itemsPerPage = 10;
  searchTerm = '';

  ngOnInit(): void {
     // Mock data – replace with real service
    this.users = Array.from({ length: 32 }).map((_, i) => ({
      username: `user${i + 1}`,
      mobile: `0100${i + 1}0000`,
      email: `user${i + 1}@mail.com`,
      creditLimit: Math.floor(Math.random() * 10000),
      role: i % 5 === 0 ? 'admin' : 'user',
    }));

    this.filteredUsers = [...this.users];
    this.updatePagination();
  }

  get totalPages(): number {
    return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
  }

  get totalPagesArray(): number[] {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i + 1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.updatePagination();
  }

  updatePagination(): void {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.paginatedUsers = this.filteredUsers.slice(start, end);
  }

  filterUsers(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredUsers = this.users.filter(
      u =>
        u.username.toLowerCase().includes(term) ||
        u.email.toLowerCase().includes(term) ||
        u.mobile.includes(term)
    );
    this.currentPage = 1;
    this.updatePagination();
  }

}
