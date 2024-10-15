import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'domaci3Front';
    protected readonly localStorage = localStorage;

    constructor(private router:Router) {
    }

    navigateToAllUsers(){
      this.router.navigate(['allUsers'])
    }
    navigateToCreateUser(){
      this.router.navigate(['createUser'])
     };

    navigateToCleaners(){
      this.router.navigate(['cleaners'])
    };
    navigateToAddCleaners(){
      this.router.navigate(['addCleaners'])
    };

    navigateToMessageHistory(){
      this.router.navigate(['messageHistory'])
    };

    logout(){
      localStorage.removeItem("token");
      localStorage.removeItem("userRoles");
      localStorage.removeItem("userEmail");
      this.router.navigate([''])
    }


    isUserLoggedIn():boolean{
      if(localStorage.getItem('token')=='' || localStorage.getItem('token')==null){
        return false
      }
      return true

    }
    isUserAuthorizedForCreatingUsers():boolean{
      if(localStorage.getItem("token")!==null && localStorage.getItem("userRoles")?.includes("can_create_users")){ //trazimo substring
        return true
      }else{
        return false;
      }
    }
    isUserAuthorizedForAddingCleaners():boolean{
      if(localStorage.getItem("token")!==null && localStorage.getItem("userRoles")?.includes("can_add_cleaners")){ //trazimo substring
        return true
      }else{
        return false;
      }
    }
}
