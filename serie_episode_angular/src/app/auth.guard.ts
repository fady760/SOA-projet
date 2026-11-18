import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.token) {
    authService.loadToken();
  }

  if (authService.isloggedIn && !authService.isTokenExpired()) {
    return true;
  } else {
    router.navigate(['login']);
    return false;
  }
};
