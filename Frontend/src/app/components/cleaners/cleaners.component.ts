import {Component, OnInit} from '@angular/core';
import {areCleanersEqual, Cleaner, Status} from "../../model";
import {Router} from "@angular/router";
import {CleanerService} from "../../service/cleaner.service";

@Component({
  selector: 'app-search-cleaners',
  templateUrl: './cleaners.component.html',
  styleUrls: ['./cleaners.component.css']
})
export class CleanersComponent implements OnInit{

   currentShownCleaners:Cleaner[]=[];


   //searching parameters:
  dateFrom:any=null;
  dateTo:any=null;
  runningStatus:boolean=false;
  stoppedStatus:boolean=false;
  dischargingStatus:boolean=false;
  name:string="";
  searchForm:any={}

  constructor(private router: Router, private cleanerService: CleanerService) {

  }

  filter(){

    //mora da se salje u stringu zato sto url.setParams ne prihvata listu
    let statusesString=""
    if(this.runningStatus==true){
      statusesString+="RUNNING,"
    }
    if(this.stoppedStatus==true){
      statusesString+="STOPPED,"
    }
    if(this.dischargingStatus==true){
      statusesString+="DISCHARGING,"
    }
    this.cleanerService.searchCleaners(localStorage.getItem("userEmail")!,this.name,statusesString,this.dateFrom,this.dateTo).subscribe(value => {

      this.currentShownCleaners=value

    },error=>{
      alert(error.message)
    })
  }

  ngOnInit() {
    //Prvi poziv sluzi iz razloga da se odmah prikazu podaci,a ne da se ceka 1-2 (svake) sekunde da bi se prikazalo
    //Ako prosledim 0, nece se pozivati fetch na svakih x milisekundi vec jednokratno.
    this.cleanerService.getAllCleanersByUser(localStorage.getItem("userEmail")!,0).subscribe(value => {
      this.currentShownCleaners=value
    },error =>{
      alert(error.message);
    })

    //SVAKE 2 SEKUNDE CE FRONTEND DA FETCHUJE USISVACE(Ovo radim zbog toga sto start/stop/discharge moraju odmah da vrate odgovor)
    this.cleanerService.getAllCleanersByUser(localStorage.getItem("userEmail")!,1000).subscribe(value => {


      //Zelim da prolazim kroz trenutno prikazane usisvace,i da pitam da li se trenutni usisivac razlikuje od novog rezultata,i ako da,switchujem vrednost
      this.currentShownCleaners.forEach(((cleaner1,index) => {
        const foundCleaner = value.find(cleaner2 => cleaner2.id === cleaner1.id);

        //Ako se u medjuvremenu promenila vrednost cleanera
        if (foundCleaner && !areCleanersEqual(cleaner1,foundCleaner)) {
          this.currentShownCleaners[index] = foundCleaner;
        }
      }));

    },error =>{
      alert(error.message);
    })

  }

  startCleaner(cleaner:Cleaner):void{

    if (cleaner.status.toString() !== Status[Status.STOPPED]) {
      alert("Usisivac nije STOPPED,vec je pokrenut");
      return;
    }

    this.cleanerService.startCleaner(cleaner.id).subscribe(value => {
      alert(value.message);
     },error =>{
       alert(error.message)
     })
  }
  stopCleaner(cleaner:Cleaner):void{
    if (cleaner.status.toString() !== Status[Status.RUNNING]) {
      alert("Usisivac nije RUNNING, ne mozemo ga zaustaviti");
      return;
    }
    this.cleanerService.stopCleaner(cleaner.id).subscribe(value => {
      alert(value.message)
    },error =>{
      alert(error.message)
    })
  }

  dischargeCleaner(cleaner:Cleaner):void{
    if (cleaner.status.toString() !== Status[Status.STOPPED]) {
      alert("Usisivac nije STOPPED, ne mozemo ga isprazniti!");
      return;
    }
    this.cleanerService.dischargeCleaner(cleaner.id).subscribe(value => {
      alert(value.message)
    },error =>{
      alert(error.message)
    })
  }

  removeCleaner(cleaner:Cleaner):void{
    if (cleaner.status.toString() !== Status[Status.STOPPED]) {
      alert("Usisivac nije STOPPED, ne mozemo ga ukloniti iz sistema!");
      return;
    }
    this.cleanerService.removeCleaner(cleaner.id).subscribe(value => {
      alert(value.message)

    },error =>{
      alert(error.message)
    })

  }
  goToSchedulingPage(cleaner:Cleaner){
    this.router.navigate([`scheduleCleaner/${cleaner.id}`]);

  }

  checkStartAuth():boolean{
    if(localStorage.getItem("userRoles")?.includes("can_start_cleaners")){
      return true
    }else{
      return false
    }
}
  checkStopAuth():boolean{
    if(localStorage.getItem("userRoles")?.includes("can_stop_cleaners")){
      return true
    }else{
      return false
    }
  }
  checkRemoveAuth():boolean{
    if(localStorage.getItem("userRoles")?.includes("can_remove_cleaners")){
      return true
    }else{
      return false
    }
  }
  checkDischargeAuth():boolean{
    if(localStorage.getItem("userRoles")?.includes("can_discharge_cleaners")){
      return true
    }else{
      return false
    }
  }
  checkSearchCleanersAuthorization():boolean{

    if(localStorage.getItem("userRoles")?.includes("can_search_cleaners")){
      return true
    }else{
      return false
    }

  }

  protected readonly Status = Status;
}
