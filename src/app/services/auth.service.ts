import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  login(): void {
    localStorage.setItem('userId', '1'); // simulate login
  }

  logout(): void {
    localStorage.removeItem('userId');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('userId');
  }

  getUserId(): string | null {
    return localStorage.getItem('userId');
  }
}
