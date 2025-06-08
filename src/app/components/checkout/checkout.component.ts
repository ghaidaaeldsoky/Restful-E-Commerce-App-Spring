import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent {
  addresses = [
    { addressId: 1, name: 'rejk', recipient: 'Alex', street: 'fnnfd', city: 'fdkjk' },
    { addressId: 2, name: 'fkkd', recipient: 'fdkj', street: 'fdf', city: 'fdfd' }
  ];

  cartItems = [
    { name: 'llfkflg', price: 125, quantity: 1 }
  ];

  shippingFee = 50;
  selectedAddressId: number | null = null;
  acceptTerms = false;
  showTermsModal = false;

  constructor (private router: Router) {}

  get subtotal(): number {
    return this.cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }

  get total(): number {
    return this.subtotal + this.shippingFee;
  }

  setAddress(addressId: number): void {
    this.selectedAddressId = addressId;
  }

  submitOrder(): void {
    if (!this.selectedAddressId) {
      alert('Please select a shipping address');
      return;
    }

    if (!this.acceptTerms) {
      alert('Please accept the terms and conditions');
      return;
    }

    // Here you would typically call a service to submit the order
    console.log('Order submitted with address ID:', this.selectedAddressId);

    this.router.navigate(["confirm-order"]);
  }

  discardOrder(): void {
    // Redirect to cart
    console.log('Order discarded');
  }

  openTermsModal(): void {
    this.showTermsModal = true;
  }

  closeTermsModal(): void {
    this.showTermsModal = false;
  }
}
