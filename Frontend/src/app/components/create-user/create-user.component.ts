import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {Role, User} from "../../model";

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit{

  newUser:User
  canCreateRole:boolean=false
  canReadRole:boolean=false
  canDeleteRole:boolean=false
  canUpdateRole:boolean=false
  allRoles:Role[]=[]

  constructor(private router:Router,private postService:UserService) {
    this.newUser = {
      id: 0,
      name: '',
      lastName: '',
      email: '',
      password:'',
      roles: []
    }
  }

  ngOnInit() {
    this.getAllRoles()
  }

   getAllRoles(){
    this.postService.getAllRoles().subscribe((value) => {
      this.allRoles = value;

    },error =>{
      alert(error.message)
    });
  }

  createNewUser(){
    this.newUser.roles=[]
    if (this.canReadRole) this.addRoles('can_read_users')
    if (this.canCreateRole) this.addRoles('can_create_users')
    if (this.canUpdateRole) this.addRoles('can_update_users')
    if (this.canDeleteRole) this.addRoles('can_delete_users')


    this.postService.createNewUser(this.newUser).subscribe(value => {
      alert("Uspesno kreiran novi korisnik")
      this.router.navigate(['allUsers'])

    },error=>{
      alert(error.message())
    })
  }
  addRoles(name: string): void {
    this.allRoles.forEach(role => {
      if (role.name.includes(name)) {
        this.newUser.roles.push(role);
      }
    });
    this.newUser.roles.forEach(value => {
      console.log(value.name)
    })
  }

}
