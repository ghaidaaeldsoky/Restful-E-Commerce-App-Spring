import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  cartItemCount = 0;

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.refreshState();
  }

  refreshState(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.cartItemCount = this.cartService.getCartCount();
  }

  logout(): void {
    this.authService.logout();
    this.refreshState();
    this.router.navigate(['/login']);
  }

  goToCart(): void {
    this.router.navigate(['/cart']);
  }

  simulateLogin(): void {
    this.authService.login();
    localStorage.setItem('productIds', JSON.stringify([101, 102, 103])); // simulated items
    this.refreshState();
  }
}