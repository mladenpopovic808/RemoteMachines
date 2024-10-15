import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {Role, User} from "../../model";
import {join} from "@angular/compiler-cli";

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.css']
})
export class AllUsersComponent implements OnInit {

  userList: User[] = []
  isUserReadAuthorized:boolean=false


  constructor(private router: Router, private postService: UserService) {


  }


  ngOnInit() {

   this.checkUserReadAuthorization();
   if(!this.isUserReadAuthorized){
     return
   }

    this.postService.getAllUsers().subscribe(value => {
      this.userList = value
    }, error => {
      alert(error.message)
    })
  };

  checkUserDeleteAuthorization():boolean{
    if(localStorage.getItem("userRoles")?.includes("can_delete_users")){
     return true
    }else{
      return false
    }
  }

  deleteUser(user:User):void{
    //Ako brisemo samog sebe:
    const userEmailFromLocalStorage = localStorage.getItem("userEmail");

    this.postService.deleteUserById(user.id).subscribe(value => {
      alert("Korisnik uspesno obrisan")

      //Ako korisnik obrise samog sebe,vracamo ga na login
      if(user.email === userEmailFromLocalStorage){
        localStorage.clear()
        this.router.navigate([''])
      }else{
      this.router.navigate(['allUsers'])

      }
    },error=>{
      alert(error.message)
    })
  }

  checkUserEditAuthorization():boolean{
    if(localStorage.getItem("userRoles")?.includes("can_update_users")){
      return true
    }else{
      return false
    }
  }

  goToUserPage(user:User){
    this.router.navigate([`editUser/${user.id}`]);
  }

  //da li korisnik ima pravo da cita korisnike
  checkUserReadAuthorization(){

    if(localStorage.getItem("userRoles")?.includes("can_read_users")){
      this.isUserReadAuthorized=true
    }else{
      this.isUserReadAuthorized=false
    }
  }

  getRolesString(user: User): String {
    // Extract role names and join them with a comma separator
    const roleNames = user.roles.map(role => role.name);
    return roleNames.join(', ');


  }
}
