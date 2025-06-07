import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  login(): void {
    const newUserId = '1';
    const currentUserId = this.getUserId();
    localStorage.setItem('userId', newUserId);
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
