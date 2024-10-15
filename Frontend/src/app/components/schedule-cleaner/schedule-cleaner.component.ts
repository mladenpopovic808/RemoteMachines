import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Cleaner, CreateCleanerRequest, ScheduleCleanerRequest} from "../../model";
import {CleanerService} from "../../service/cleaner.service";

@Component({
  selector: 'app-schedule-cleaner',
  templateUrl: './schedule-cleaner.component.html',
  styleUrls: ['./schedule-cleaner.component.css']
})
export class ScheduleCleanerComponent implements OnInit{


  cleaner:Cleaner={} as Cleaner

  scheduleRequest:ScheduleCleanerRequest={
    id:this.cleaner.id,
    date:"",
    time:"",
    action:"",
  }


  dropdownItems: string[] = ['Start', 'Stop', 'Remove', 'Discharge'];


  constructor(private cleanerService:CleanerService, private router:Router, private route:ActivatedRoute) {

  }
  ngOnInit() {
    const id: number = parseInt(<string>this.route.snapshot.paramMap.get('id'));
    this.cleanerService.getCleanerById(id).subscribe(value => {
      this.cleaner=value;
    })

  }
  scheduleCleaner(){
    if(this.scheduleRequest.time==""){
      alert("Unesite vreme")
      return;
    }
    if(this.scheduleRequest.date==""){
      alert("Unesite datum")
      return;
    }
    if(this.scheduleRequest.action==""){
      alert("Unesite akciju")
      return;
    }
    this.scheduleRequest.id=this.cleaner.id;

    this.cleanerService.scheduleCleaner(this.scheduleRequest).subscribe(value=>{

      alert("Uspesno zakazana akcija!")

    },error=>{
      alert(error.message)
    })
    this.router.navigate(["cleaners"]);



}


}
