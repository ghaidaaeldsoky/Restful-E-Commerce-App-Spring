import { Component, OnInit } from '@angular/core';

interface Order {
  username: string;
  totalAmount: number;
  address: string;
  orderDate: string;
  products: { name: string; quantity: number }[];
}

@Component({
  selector: 'app-admin-orders',
  standalone: false,
  templateUrl: './admin-orders.component.html',
  styleUrl: './admin-orders.component.css'
})
export class AdminOrdersComponent implements OnInit {

  orders: Order[] = [];
  filteredOrders: Order[] = [];
  paginatedOrders: Order[] = [];
  currentPage = 1;
  itemsPerPage = 5;
  searchTerm = '';


  ngOnInit(): void {
    this.orders = Array.from({ length: 12 }).map((_, i) => ({
      username: `User ${i + 1}`,
      totalAmount: 100 + i * 10,
      address: `Cairo, Street ${i + 1}`,
      orderDate: new Date().toLocaleDateString(),
      products: [
        { name: 'Perfume A', quantity: 2 },
        { name: 'Perfume B', quantity: 1 },
      ],
    }));

    this.filteredOrders = [...this.orders];
    this.updatePagination();
  }

   get totalPages() {
    return Math.ceil(this.filteredOrders.length / this.itemsPerPage);
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
    this.paginatedOrders = this.filteredOrders.slice(start, end);
  }

  filterOrders(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredOrders = this.orders.filter(order =>
      order.username.toLowerCase().includes(term) || order.address.toLowerCase().includes(term)
    );
    this.currentPage = 1;
    this.updatePagination();
  }

}
