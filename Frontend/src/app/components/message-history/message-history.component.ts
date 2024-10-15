import {Component, OnInit} from '@angular/core';
import {CleanerService} from "../../service/cleaner.service";
import {ErrorMessage} from "../../model";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-message-history',
  templateUrl: './message-history.component.html',
  styleUrls: ['./message-history.component.css']
})
export class MessageHistoryComponent implements OnInit{

  errorMessages:ErrorMessage[]=[]



  constructor(private cleanerService:CleanerService) {
  }


  ngOnInit() {
    this.cleanerService.getMessageHistory(localStorage.getItem("userEmail")!).subscribe(value => {
      this.errorMessages=value
    },error =>{
      alert(error.message);
    })
  }
}
