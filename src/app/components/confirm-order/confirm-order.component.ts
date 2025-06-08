import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirm-order',
  standalone: false,
  templateUrl: './confirm-order.component.html',
  styleUrl: './confirm-order.component.css'
})
export class ConfirmOrderComponent {
  constructor (private router: Router) {}

  homeNavigate() {
    this.router.navigate(["home"]);
  }
}
