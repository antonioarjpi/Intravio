import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NavComponent } from './components/nav/nav.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './auth/auth.guard';
import { HomeComponent } from './pages/home/home.component';
import { CadastrarDepartamentoComponent } from './pages/departamentos/cadastrar-departamento/cadastrar-departamento.component';
import { ListarDepartamentoComponent } from './pages/departamentos/listar-departamento/listar-departamento.component';
import { DeletarDepartamentoComponent } from './pages/departamentos/deletar-departamento/deletar-departamento.component';
import { AtualizarDepartamentoComponent } from './pages/departamentos/atualizar-departamento/atualizar-departamento.component';
import { FilialListarComponent } from './pages/filiais/filial-listar/filial-listar.component';
import { FilialCadastrarComponent } from './pages/filiais/filial-cadastrar/filial-cadastrar.component';
import { FilialAtualizarComponent } from './pages/filiais/filial-atualizar/filial-atualizar.component';
import { FilialDeletarComponent } from './pages/filiais/filial-deletar/filial-deletar.component';

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
      {
        path: "departamentos",
        component: ListarDepartamentoComponent,
      },
      {
        path: "departamentos/cadastrar",
        component: CadastrarDepartamentoComponent,
      },
      {
        path: "departamentos/deletar/:id",
        component: DeletarDepartamentoComponent,
      },
      {
        path: "departamentos/atualizar/:id",
        component: AtualizarDepartamentoComponent,
      },
      {
        path: "filiais",
        component: FilialListarComponent
      },
      {
        path: "filiais/cadastrar",
        component: FilialCadastrarComponent
      },
      {
        path: "filiais/atualizar/:id",
        component: FilialAtualizarComponent
      },
      {
        path: "filiais/deletar/:id",
        component: FilialDeletarComponent
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
