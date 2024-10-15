import {Component, OnInit} from '@angular/core';
import {Cleaner, CreateCleanerRequest, LoginRequest, Status} from "../../model";
import {CleanerService} from "../../service/cleaner.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-cleaners',
  templateUrl: './add-cleaners.component.html',
  styleUrls: ['./add-cleaners.component.css']
})
export class AddCleanersComponent {

  cleanerName:string=""

  createRequest:CreateCleanerRequest={
    name:"",
    email:localStorage.getItem("userEmail")!
  }
  constructor(private cleanerService:CleanerService,private router:Router) {
  }

  createNewCleaner():void{
    this.createRequest.name=this.cleanerName

    this.cleanerService.addNewCleaner(this.createRequest).subscribe(value => {

      alert("Uspesno dodat!")
      this.router.navigate(["cleaners"]);

    },error =>{
      alert(error.message)
    })
  }
}
