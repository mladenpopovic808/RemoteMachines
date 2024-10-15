import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginRequest, LoginResponse, Role, User, UserDto} from "../model";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {


   headers = new HttpHeaders({
    'Content-Type': 'application/json',
     'Access-Control-Allow-Origin': '*',
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  })

  constructor(private httpClient: HttpClient) {


   }




  //users

  getAllRoles() {
    return this.httpClient.get<Role[]>(`${environment.rolesUrl}/all`, { headers: this.headers });
  }


  updateUser(userDto: UserDto): Observable<User> {
    return this.httpClient.put<User>(`${environment.usersUrl}/update`, userDto, { headers: this.headers });
  }

  login(loginInfo:LoginRequest){

    //poslace se loginInfo u body
    return this.httpClient.post<LoginResponse>(`${environment.usersUrl}/login`, loginInfo);
  }
  getUserById(id:number){
    return this.httpClient.get<User>(`${environment.usersUrl}/get/${id}`,{ headers: this.headers });
  }
  public deleteUserById(id: number): Observable<any>{
    return this.httpClient.delete<any>(`${environment.usersUrl}/delete/${id}`, { headers: this.headers });
  }

  public createNewUser(user:User):Observable<any>{
    return this.httpClient.post<any>(`${environment.usersUrl}/add`, user,{ headers: this.headers });
  }


  getAllUsers(){
    return this.httpClient.get<User[]>(`${environment.usersUrl}/get`,{headers:this.headers})
  }

}
