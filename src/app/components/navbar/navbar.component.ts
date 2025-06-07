import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  cartItemCount = 0;
  private cartSubscription: Subscription = new Subscription();

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.refreshState();
    
    // cart count change
    this.cartSubscription = this.cartService.cartCount$.subscribe(
      count => {
        this.cartItemCount = count;
       
        setTimeout(() => {
          this.cartItemCount = count;
        }, 0);
      }
    );
  }

  ngOnDestroy(): void {
    this.cartSubscription.unsubscribe();
  }

  refreshState(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.cartItemCount = this.cartService.getCartCount();
    setTimeout(() => {
      this.cartItemCount = this.cartService.getCartCount();
    }, 0);
  }

logout(): void {
  this.authService.logout();

  this.cartService.clearCart();
  this.refreshState();

  this.router.navigate(['/home']);
}



  goToCart(): void {
    this.router.navigate(['/cart']);
  }

  simulateLogin(): void {
    this.authService.login();
    this.refreshState();
  }
}