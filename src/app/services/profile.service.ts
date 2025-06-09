import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserInfo1 } from '../models/UserInfo1';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private email: string = "";

  constructor(private http: HttpClient) {  
    // To get the email of principle
  /*  const token = localStorage.getItem('token');
    const payload = token ? JSON.parse(atob(token.split('.')[1])) : null;
    this.email = payload?.email;

    console.log("email: " + this.email); 
    console.log("token: " + token); */
  }

  getPersonalInfo1(): Observable<UserInfo1> {
    return this.http.get<any>("http://localhost:8085/users/profile")
    .pipe(
      map(res => {
        console.log("222222ressssssssssss: " + res);
        console.log("22222fname: " + res.data.userInfoDto.userName);
        console.log("2222222email: " + res.data.userInfoDto.email);
        console.log("22222222phone: " + res.data.userInfoDto.phoneNumber);

        return {
          firstName: res.data.userInfoDto.userName,
          email: res.data.userInfoDto.email,
          phone: res.data.userInfoDto.phoneNumber
        } as UserInfo1;
      })
    );
  }

  updatePersonalInfo(data: Partial<UserInfo1>): Observable<any> {
    return this.http.patch("http://localhost:8085/users", data);
  }

}
