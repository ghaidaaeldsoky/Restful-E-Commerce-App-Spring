import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
declare var $: any;

@Component({
  selector: 'app-product-details',
  standalone: false,
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent implements OnInit {
  product: Product | null = null;
  quantity = 1;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const productId = +params['id'];
      const foundProduct = this.productService.getProductById(productId);
      this.product = foundProduct || null;
      this.quantity = 1;
      
      setTimeout(() => {
        if (typeof $ !== 'undefined' && $('.s_Product_carousel').length) {
          $('.s_Product_carousel').owlCarousel({
            items: 1,
            loop: true,
            dots: true,
            nav: false
          });
        }
      }, 0);
    });
  }

  increase(): void {
    if (this.product && this.quantity < this.product.quantity) {
      this.quantity++;
    }
  }

  decrease(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart(): void {
    if (!this.authService.isLoggedIn()) {
      alert('Please log in to add items to cart!');
      return;
    }

    if (!this.product) {
      return;
    }

    if (this.product.quantity === 0) {
      alert('This product is out of stock!');
      return;
    }

    const currentCartQuantity = this.cartService.getItemQuantity(this.product.id);
    const totalQuantityAfterAdd = currentCartQuantity + this.quantity;

    if (totalQuantityAfterAdd > this.product.quantity) {
      alert(`Cannot add ${this.quantity} items. Only ${this.product.quantity - currentCartQuantity} more available.`);
      return;
    }

    // add with quantity
    this.cartService.addItem(this.product.id, this.quantity);
    
    if (currentCartQuantity > 0) {
      alert(`${this.product.name} quantity updated in cart! Total: ${totalQuantityAfterAdd}`);
    } else {
      alert(`${this.product.name} added to cart! Quantity: ${this.quantity}`);
    }
    
    // reset
    this.quantity = 1;
  }
}