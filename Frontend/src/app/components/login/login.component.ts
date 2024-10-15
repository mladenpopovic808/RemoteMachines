import {Component, OnInit} from '@angular/core';
import {LoginRequest} from "../../model";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  email:string=''
  password:string=''
  loginRequest:LoginRequest={
    email:'',
    password:''
  };

  constructor(private postService:UserService, private router:Router) {
  }

  ngOnInit() {
  }

  login(){
    this.loginRequest.email=this.email;
    this.loginRequest.password=this.password;


    this.postService.login(this.loginRequest).subscribe(loginResponse=>{
      localStorage.setItem("token",loginResponse.token);
      localStorage.setItem("userEmail",this.email);
      if(loginResponse.roles.length==0){
        alert("Nemate ni jednu permisiju :/ ")
      }else{
      localStorage.setItem("userRoles",JSON.stringify(loginResponse.roles));
      }

      this.router.navigate(['allUsers'])
    },error => {
          alert("U have entered wrong credentials")
        }

    )

  }

  protected readonly localStorage = localStorage;
}
