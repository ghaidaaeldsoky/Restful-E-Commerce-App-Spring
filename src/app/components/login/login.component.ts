import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: (res) => {
        if (res.success && res.data?.token) {
          this.authService.saveToken(res.data.token);
          const redirect = this.authService.getRedirectUrl();
          if (redirect) {
            this.authService.clearRedirectUrl();
            this.router.navigateByUrl(redirect);
          }
          else  this.router.navigate(['/home']);  
        }
      },
      error: (err) => {
        console.error('Login failed', err);
        alert('Invalid email or password!');
      },
    });
  }
}
