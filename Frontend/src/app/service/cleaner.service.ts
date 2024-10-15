import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Cleaner, CreateCleanerRequest, Role, ScheduleCleanerRequest} from "../model";
import {environment} from "../../environments/environment";
import {interval, Observable, switchMap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CleanerService {

  headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  })

  constructor(private httpClient:HttpClient) {

  }

  getAllCleanersByUser(email:string,miliseconds:number) {

    //queryParam je ovo
    const params=new HttpParams().set("email",email)

    //ako prosledimo 0,znaci da ne zelimo da se poziva na svakih x milisekundi
    if(miliseconds==0){
      return this.httpClient.get<Cleaner[]>(`${environment.cleanersUrl}/allByUser`, { headers: this.headers,params:params });
    }

    return interval(miliseconds).pipe(
      switchMap(() => this.httpClient.get<Cleaner[]>(`${environment.cleanersUrl}/allByUser`, { headers: this.headers, params: params }))
    );

  }

  addNewCleaner(request:CreateCleanerRequest) {
    return this.httpClient.post<Cleaner>(`${environment.cleanersUrl}/add`,request,{ headers: this.headers});
  }

  startCleaner(cleanerId:number) {
    return this.httpClient.get<any>(`${environment.cleanersUrl}/start/${cleanerId}`,{ headers: this.headers});
  }
  stopCleaner(cleanerId:number) {
    return this.httpClient.get<any>(`${environment.cleanersUrl}/stop/${cleanerId}`,{ headers: this.headers});
  }
  dischargeCleaner(cleanerId:number) {
    return this.httpClient.get<any>(`${environment.cleanersUrl}/discharge/${cleanerId}`,{ headers: this.headers});
  }

  removeCleaner(cleanerId:number):Observable<any> {
     return this.httpClient.get<any>(`${environment.cleanersUrl}/remove/${cleanerId}`,{ headers: this.headers});
  }

  getMessageHistory(email:string) {
    const params=new HttpParams().set("email",email)
    return this.httpClient.get<any>(`${environment.cleanersUrl}/errorsByUser`,{ headers: this.headers,params:params});
  }
  scheduleCleaner(request:ScheduleCleanerRequest) {
    return this.httpClient.post<any>(`${environment.cleanersUrl}/schedule`,request,{ headers: this.headers});
  }



  searchCleaners(email:string,name:string,statusList:string,dateFrom:any,dateTo:any){

    //sve se prosledjuje u query,pa se na bekendu ispituje da li je null...
    const params=new HttpParams().set("email",email)
      .set("name",name)
      .set("statusList",statusList)
      .set("dateFrom",dateFrom)
      .set("dateTo",dateTo);

    return this.httpClient.get<Cleaner[]>(`${environment.cleanersUrl}/filter`, { headers: this.headers,params:params });
  }

  getCleanerById(cleanerId:number) {
    return this.httpClient.get<Cleaner>(`${environment.cleanersUrl}/getCleaner/${cleanerId}`,{ headers: this.headers});
  }

}
