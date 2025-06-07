import { Component, OnInit  } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

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
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.trendingProducts = this.productService.getTrendingProducts();
  }

  addToCart(product: Product): void {
    this.cartService.addItem(product.id);
    alert(`${product.name} added to cart!`);
  }
}