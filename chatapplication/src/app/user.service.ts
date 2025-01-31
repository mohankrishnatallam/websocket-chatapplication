import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  username: string;

  isLoggedIn(): boolean {
    return !!this.username;
  }
}
