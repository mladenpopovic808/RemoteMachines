import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './components/app/app.component';
import { LoginComponent } from './components/login/login.component';
import { AllUsersComponent } from './components/all-users/all-users.component';
import {FormsModule} from "@angular/forms";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { CreateUserComponent } from './components/create-user/create-user.component';
import { HoverEffectDirective } from './components/all-users/hover-effect.directive';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { CleanersComponent } from './components/cleaners/cleaners.component';
import { MessageHistoryComponent } from './components/message-history/message-history.component';
import { AddCleanersComponent } from './components/add-cleaners/add-cleaners.component';
import { ScheduleCleanerComponent } from './components/schedule-cleaner/schedule-cleaner.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AllUsersComponent,
    CreateUserComponent,
    HoverEffectDirective,
    EditUserComponent,
    CleanersComponent,
    MessageHistoryComponent,
    AddCleanersComponent,
    ScheduleCleanerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
