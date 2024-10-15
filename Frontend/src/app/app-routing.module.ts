import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AllUsersComponent} from "./components/all-users/all-users.component";
import {LoginGuard} from "./guards/login.guard";
import {CreateUserComponent} from "./components/create-user/create-user.component";
import {CreateUserGuard} from "./guards/create-user.guard";
import {EditUserComponent} from "./components/edit-user/edit-user.component";
import {EditUserGuard} from "./guards/edit-user.guard";
import {CleanersComponent} from "./components/cleaners/cleaners.component";
import {MessageHistoryComponent} from "./components/message-history/message-history.component";
import {AddCleanersComponent} from "./components/add-cleaners/add-cleaners.component";
import {AddCleanersGuard} from "./guards/add-cleaners.guard";
import {ScheduleCleanerComponent} from "./components/schedule-cleaner/schedule-cleaner.component";
import {ScheduleCleanersGuard} from "./guards/schedule-cleaners.guard";


const routes: Routes = [
  {
    path:"",
    component:LoginComponent,
    canDeactivate:[LoginGuard]
  },
  {
    path:"allUsers",
    component:AllUsersComponent,
  },
  {
    path:"createUser",
    component:CreateUserComponent,
    canActivate:[CreateUserGuard]
  },
  {
    path:"editUser/:id",
    component:EditUserComponent,
    canActivate:[EditUserGuard]
  },
  {
    path:"scheduleCleaner/:id",
    component:ScheduleCleanerComponent,
    canActivate:[ScheduleCleanersGuard],
  },
  {
    path:"cleaners",
    component:CleanersComponent,
  },
  {
    path:"messageHistory",
    component:MessageHistoryComponent,
  },
  {
    path:"addCleaners",
    component:AddCleanersComponent,
    canActivate:[AddCleanersGuard],
  }



];




@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
