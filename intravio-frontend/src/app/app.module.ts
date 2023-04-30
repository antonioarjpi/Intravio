import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

// Imports para componentes do Angular Material
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from "@angular/material/table";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatDialogModule } from "@angular/material/dialog";
import {MatChipsModule} from '@angular/material/chips';

import { ToastrModule } from "ngx-toastr";

import { NavComponent } from './components/nav/nav.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthInterceptorProvider } from './interceptors/auth.interceptor';
import { LogoComponent } from './components/logo/logo.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    LoginComponent,
    LogoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,


     // Angular Material
     MatFormFieldModule,
     MatPaginatorModule,
     MatCheckboxModule,
     MatSnackBarModule,
     MatToolbarModule,
     MatSidenavModule,
     MatButtonModule,
     MatSelectModule,
     MatInputModule,
     MatRadioModule,
     MatTableModule,
     MatIconModule,
     MatListModule,
     MatCardModule,
     MatDialogModule,
     MatChipsModule,

     ToastrModule.forRoot({
      timeOut: 4000,
      closeButton: true,
      progressBar: true,
    }),


  ],
  providers: [AuthInterceptorProvider],
  bootstrap: [AppComponent]
})
export class AppModule { }
