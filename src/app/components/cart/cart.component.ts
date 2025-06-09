import { Component, OnInit, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: false,
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  cartItems: any[] = [];
  subtotal: number = 0;

  @ViewChild('orderPrice') orderPrice!: ElementRef;

  constructor(
    private cartService: CartService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadCart();
  }

  loadCart(): void {
    this.cartItems = this.cartService.getCartItems().map(item => ({
      ...item,
     productPhoto: item.imageUrl || 'img/gallery/1.jpg', 
     productName: item.name,
     productPrice: item.price,
     storage: item.quantity 
    }));
    this.updateSubtotal();
  }

  updateSubtotal(): void {
    this.subtotal = this.cartItems.reduce((sum, item) => sum + (item.productPrice * item.quantity), 0);
    if (this.orderPrice) {
      this.orderPrice.nativeElement.textContent = `${this.subtotal} EGP`;
    }
  }

  increaseQuantity(item: any): void {
    if (item.quantity < item.storage) {
      item.quantity++;
      this.cartService.updateItemQuantity(item.id, item.quantity);
      this.updateSubtotal();
      this.cdr.detectChanges();
    }
  }

  decreaseQuantity(item: any): void {
    if (item.quantity > 1) {
      item.quantity--;
      this.cartService.updateItemQuantity(item.id, item.quantity);
      this.updateSubtotal();
      this.cdr.detectChanges();
    }
  }

  removeItem(itemId: number): void {
    this.cartService.removeItem(itemId);
    this.loadCart();
    this.cdr.detectChanges();
  }

  continueShopping(): void {
    this.router.navigate(['/products']);
  }

  proceedToCheckout(): void {
    this.router.navigate(['/checkout']);
  }

  submitForm(target: string, event: Event): void {
    event.preventDefault();
    if (target === 'home') {
      this.continueShopping();
    } else if (target === 'checkoutServlet') {
      this.proceedToCheckout();
    }
  }
}
