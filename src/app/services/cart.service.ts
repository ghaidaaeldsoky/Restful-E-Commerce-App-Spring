import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface CartItem {
  productId: number;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartCountSubject = new BehaviorSubject<number>(0);
  public cartCount$ = this.cartCountSubject.asObservable();

  constructor() {
    this.updateCartCount();
  }

  getCartItems(): CartItem[] {
    const items = JSON.parse(localStorage.getItem('cartItems') || '[]');
    return Array.isArray(items) ? items : [];
  }

  getCartCount(): number {
    const items = this.getCartItems();
    return items.reduce((total, item) => total + item.quantity, 0);
  }

  getUniqueItemsCount(): number {
    return this.getCartItems().length;
  }

  addItem(productId: number, quantity: number = 1): void {
    const items = this.getCartItems();
    const existingItemIndex = items.findIndex(item => item.productId === productId);
    
    if (existingItemIndex > -1) {
      // product exists, increase quantity
      items[existingItemIndex].quantity += quantity;
    } else {
      // new product, add to cart
      items.push({ productId, quantity });
    }
    
    localStorage.setItem('cartItems', JSON.stringify(items));
    this.updateCartCount();
  }

  removeItem(productId: number): void {
    const items = this.getCartItems();
    const updatedItems = items.filter(item => item.productId !== productId);
    localStorage.setItem('cartItems', JSON.stringify(updatedItems));
    this.updateCartCount();
  }

  updateItemQuantity(productId: number, quantity: number): void {
    const items = this.getCartItems();
    const itemIndex = items.findIndex(item => item.productId === productId);
    
    if (itemIndex > -1) {
      if (quantity <= 0) {
        // remove item if quantity is 0 
        items.splice(itemIndex, 1);
      } else {
        items[itemIndex].quantity = quantity;
      }
      localStorage.setItem('cartItems', JSON.stringify(items));
      this.updateCartCount();
    }
  }

  getItemQuantity(productId: number): number {
    const items = this.getCartItems();
    const item = items.find(item => item.productId === productId);
    return item ? item.quantity : 0;
  }

  clearCart(): void {
    localStorage.removeItem('cartItems');
    this.updateCartCount();
  }

  private updateCartCount(): void {
    const count = this.getCartCount();
    this.cartCountSubject.next(count);
  }
}
