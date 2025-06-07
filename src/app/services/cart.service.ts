import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  getCartItems(): number[] {
    const items = JSON.parse(localStorage.getItem('productIds') || '[]');
    return Array.isArray(items) ? items : [];
  }

  getCartCount(): number {
    return this.getCartItems().length;
  }

  addItem(productId: number): void {
    const items = this.getCartItems();
    items.push(productId);
    localStorage.setItem('productIds', JSON.stringify(items));
  }

  clearCart(): void {
    localStorage.removeItem('productIds');
  }
}
