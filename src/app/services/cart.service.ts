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
  private cartItems: { id: number; name: string; price: number; quantity: number; imageUrl?: string }[] = [];

  constructor() {
    this.updateCartCount();
  }

  getCartItems(): { id: number; name: string; price: number; quantity: number; imageUrl?: string }[] {
    return [...this.cartItems];
  }

  getCartCount(): number {
    const items = this.getCartItems();
    return items.reduce((total, item) => total + item.quantity, 0);
  }

  getUniqueItemsCount(): number {
    return this.getCartItems().length;
  }

  addItem(productId: number, quantity: number, name?: string, price?: number, imageUrl?: string): void {
    const existingItem = this.cartItems.find(item => item.id === productId);
    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      this.cartItems.push({ id: productId, name: name || 'Unknown', price: price || 0, quantity, imageUrl });
    }
    this.saveCart();
    this.updateCartCount();
  }

  private saveCart(): void {
    localStorage.setItem('cart', JSON.stringify(this.cartItems));
  }

  removeItem(productId: number): void {
    const items = this.getCartItems();
    const updatedItems = items.filter(item => item.id !== productId);
    localStorage.setItem('cartItems', JSON.stringify(updatedItems));
    this.updateCartCount();
  }

  updateItemQuantity(productId: number, quantity: number): void {
    const items = this.getCartItems();
    const itemIndex = items.findIndex(item => item.id === productId);
    
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
    const item = items.find(item => item.id === productId);
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
