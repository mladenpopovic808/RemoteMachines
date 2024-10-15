import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanDeactivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EditUserGuard implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot,state: RouterStateSnapshot):boolean {
    if(localStorage.getItem("token")!==null && localStorage.getItem("userRoles")?.includes("can_update_users")){ //trazimo substring
      return true
    }else{
      alert("Nemate pravo da editujete usere")
      return false;
    }

  }



}
