import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NavComponent } from './components/nav/nav.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './auth/auth.guard';
import { HomeComponent } from './pages/home/home.component';

const routes: Routes = [
  {
    path: "",
    component: NavComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: "",
        component: HomeComponent,
      },
      {
        path: "home",
        component: HomeComponent,
      },
    ]
  },
  {
    path: "login",
    component: LoginComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
