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
export class AllUsersGuard implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot,state: RouterStateSnapshot):boolean {
    if(localStorage.getItem("token")!==null && localStorage.getItem("userRoles")?.includes("can_read_users")){ //trazimo substring
      return true
    }else{

      return false;
    }

  }



}
