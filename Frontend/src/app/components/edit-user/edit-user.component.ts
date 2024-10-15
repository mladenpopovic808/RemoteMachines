import {Component, OnInit} from '@angular/core';
import {Role, User, UserDto} from "../../model";
import {UserService} from "../../service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit{

  allRoles:Role[]=[]

  canCreateRole:boolean=false
  canReadRole:boolean=false
  canDeleteRole:boolean=false
  canUpdateRole:boolean=false
  user:User


  constructor(private postService:UserService, private router:Router, private route:ActivatedRoute) {
    this.user = {
      id: 0,
      name: '',
      lastName: '',
      email: '',
      password:'',
      roles: []
    }
  }

   async ngOnInit() {
    //kroz constructor importujem trenutnu rutu i izvlacim parametre
    const id: number = parseInt(<string>this.route.snapshot.paramMap.get('id'));


     await this.getUserById(id) //setovace this.user=user

     await this.getAllRoles()


   //  this.setCheckBoxValues() pokrecem u subcribe-u od getAllRoles
  }

   setCheckBoxValues(){


    this.user.roles.forEach((role) => {
      if (role.name === 'can_create_users') {
        this.canCreateRole = true;
      } else if (role.name === 'can_read_users') {
        this.canReadRole = true;
      } else if (role.name === 'can_delete_users') {
        this.canDeleteRole = true;
      } else if (role.name === 'can_update_users') {
        this.canUpdateRole = true;
      }
    });
  }
  addRoles(name: string): void {
    this.allRoles.forEach(role => {
      if (role.name.includes(name)) {
        this.user.roles.push(role);
      }
    });
    this.user.roles.forEach(value => {
      console.log(value.name)
    })
  }
  saveUser(){
    //Prolazimo kroz checkbox-ove i za svaki stiklirani pushujemo objekat Role u usera
    this.user.roles=[]
    if (this.canReadRole) this.addRoles('can_read_users')
    if (this.canCreateRole) this.addRoles('can_create_users')
    if (this.canUpdateRole) this.addRoles('can_update_users')
    if (this.canDeleteRole) this.addRoles('can_delete_users')


    let userDto: UserDto = {
      id: this.user.id,
      name: this.user.name,
      lastName: this.user.lastName,
      email:this.user.email,
      roles:this.user.roles

    };

    //TODO ako user menja svoje role-ove,treba da ih i azuriramo u localStorage-u
    if(localStorage.getItem("userEmail")?.includes(this.user.email)){
      localStorage.setItem("userRoles",JSON.stringify(this.user.roles));
    }



    this.postService.updateUser(userDto).subscribe(value => {
      alert("Uspesno promenjen korisnik")
    },error =>{
      alert(error.message)
    })
  }

  checkUserRoleAndDisableButton() {
    //Ukoliko korisnik nema role za brisanje korisnika,delete ce biti disable-ovan
    const storedRolesString = localStorage.getItem('userRoles');
    const userRoles = storedRolesString ? JSON.parse(storedRolesString) : [];
    const canDeleteUsers = userRoles.includes('can_delete_user'); // Replace 'deletion_role' with the actual role name
    const deleteButton = document.getElementById('deleteButton') as HTMLButtonElement;
    if (!canDeleteUsers) {
      deleteButton.disabled = true;
    }
  }

  deleteUser():void{
    this.postService.deleteUserById(this.user.id).subscribe(value => {
      alert("Korisnik uspesno obrisan")
      this.router.navigate(['allUsers'])
    },error=>{
      alert(error.message)
    })
  }



  async getAllRoles(){

    this.postService.getAllRoles().subscribe((value) => {
      this.allRoles = value;

    },error =>{
      alert(error.message)
    });

  }
  async getUserById(id:number){

    this.postService.getUserById(id).subscribe(value => {
      this.user=value
      this.setCheckBoxValues()
    })
  }

  protected readonly localStorage = localStorage;
}
