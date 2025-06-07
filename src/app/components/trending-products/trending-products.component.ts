import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-trending-products',
  standalone: false,
  templateUrl: './trending-products.component.html',
  styleUrl: './trending-products.component.css'
})
export class TrendingProductsComponent implements OnInit {
  trendingProducts: Product[] = [];

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.trendingProducts = this.productService.getTrendingProducts();
  }

  addToCart(product: Product): void {
    if (!this.authService.isLoggedIn()) {
      alert('Please log in to add items to cart!');
      return;
    }

    if (product.quantity === 0) {
      alert('This product is out of stock!');
      return;
    }

    const currentCartQuantity = this.cartService.getItemQuantity(product.id);
    
    if (currentCartQuantity >= product.quantity) {
      alert(`${product.name} is already at maximum quantity in your cart!`);
      return;
    }

    // Add one item to cart
    this.cartService.addItem(product.id, 1);
    
    const newQuantity = currentCartQuantity + 1;
    if (currentCartQuantity > 0) {
      alert(`${product.name} quantity updated in cart! Total: ${newQuantity}`);
    } else {
      alert(`${product.name} added to cart!`);
    }
  }
}