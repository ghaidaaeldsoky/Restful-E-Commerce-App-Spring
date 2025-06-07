import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductDto, ProductsService } from '../../../services/admin/products.service';

interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  brand: string;
  gender: string;
  size: string;
  imageUrl: string;
}

declare var $: any;

@Component({
  selector: 'app-admin-products',
  standalone: false,
  templateUrl: './admin-products.component.html',
  styleUrl: './admin-products.component.css'
})
export class AdminProductsComponent implements OnInit{

  products: ProductDto[] = [];
  // filteredProducts: Product[] = [];
  // paginatedProducts: Product[] = [];
  currentPage = 0;  ///Backend
  itemsPerPage = 10;
  searchTerm = '';
  totalPages = 0;

  successMessage: string | null = null;

  constructor(private router: Router, private productsService:ProductsService) {}


  ngOnInit(): void {
   // mock products
    // this.products = Array.from({ length: 25 }).map((_, i) => ({
    //   id:i+1,
    //   name: `Perfume ${i + 1}`,
    //   description: 'Luxury perfume',
    //   price: 150 + i,
    //   quantity: 10 + i,
    //   brand: 'MISK',
    //   gender: i % 2 === 0 ? 'male' : 'female',
    //   size: '100ml',
    //   imageUrl: 'https://via.placeholder.com/60',
    // }));

    // this.filteredProducts = [...this.products];
    // this.updatePagination();

     this.loadProducts();
  }

  loadProducts(): void {
    this.productsService.getProducts(this.currentPage, this.itemsPerPage, this.searchTerm)
      .subscribe(response => {
        this.products = response.content;
        this.totalPages = response.totalPages;
      });
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadProducts();
  }

  get totalPagesArray(): number[] {
    return Array(this.totalPages).fill(0).map((_, i) => i);
  }

  filterProducts(): void {
    this.currentPage = 0;
    this.loadProducts();
  }

   confirmDelete(product: ProductDto): void {
    const confirmDelete = window.confirm(`Are you sure you want to delete ${product.name}?`);
    if (confirmDelete) {
      // implement delete logic (call API), then reload:
      this.successMessage = 'Deleted successfully.';
      setTimeout(() => (this.successMessage = null), 2000);
      this.loadProducts();
    }
  }

  editProduct(product: Product) {
    this.router.navigate(['/products', product.id, 'edit']);
  }

  addNewProduct(): void {
    this.router.navigate(['/products/add']);
  }

  // updatePagination() {
  //   const start = (this.currentPage - 1) * this.itemsPerPage;
  //   const end = start + this.itemsPerPage;
  //   this.paginatedProducts = this.filteredProducts.slice(start, end);
  // }

  // get totalPagesArray() {
  //   return Array(this.totalPages).fill(0).map((_, i) => i + 1);
  // }

  // goToPage(page: number) {
  //   if (page < 1 || page > this.totalPages) return;
  //   this.currentPage = page;
  //   this.updatePagination();
  // }

  // filterProducts() {
  //   const term = this.searchTerm.toLowerCase();
  //   this.filteredProducts = this.products.filter(p =>
  //     p.name.toLowerCase().includes(term) || p.brand.toLowerCase().includes(term)
  //   );
  //   this.currentPage = 1;
  //   this.updatePagination();
  // }

  

//   // Modal:
//   openEditModal(product: Product): void {
//   this.selectedProduct = { ...product };
//   ($('#editModal') as any).modal('show');
// }

// saveProduct(): void {
//   if (!this.selectedProduct) return;

//   const index = this.products.findIndex(p => p.name === this.selectedProduct?.name);
//   if (index !== -1) {
//     this.products[index] = { ...this.selectedProduct };
//     this.filterProducts();
//     ($('#editModal') as any).modal('hide');
//   }
// }

// confirmDelete(product: Product): void {
//   const confirmDelete = window.confirm(`Are you sure you want to delete ${product.name}?`);
//   if (confirmDelete) {
//     this.products = this.products.filter(p => p !== product);
//     this.filterProducts();
//     this.successMessage = 'Deleted successfully.';

//     setTimeout(() => {
//       this.successMessage = null;
//     }, 2000);
//   }
// }



// Method to handle success messages from other components
  // handleProductSaved(message: string): void {
  //   this.loadProducts(); // Reload products
  //   this.showSuccessMessage(message);
  // }

}
