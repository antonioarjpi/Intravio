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
import { TransportadorListarComponent } from './pages/transportadores/transportador-listar/transportador-listar.component';
import { TransportadorCadastrarComponent } from './pages/transportadores/transportador-cadastrar/transportador-cadastrar.component';
import { TransportadorAtualizarComponent } from './pages/transportadores/transportador-atualizar/transportador-atualizar.component';
import { TransportadorDeletarComponent } from './pages/transportadores/transportador-deletar/transportador-deletar.component';
import { ProdutoListarComponent } from './pages/produtos/produto-listar/produto-listar.component';
import { ProdutoCadastrarComponent } from './pages/produtos/produto-cadastrar/produto-cadastrar.component';
import { ProdutoAtualizarComponent } from './pages/produtos/produto-atualizar/produto-atualizar.component';
import { ProdutoDeletarComponent } from './pages/produtos/produto-deletar/produto-deletar.component';
import { FuncionarioListarComponent } from './pages/funcionarios/funcionario-listar/funcionario-listar.component';
import { FuncionarioCadastrarComponent } from './pages/funcionarios/funcionario-cadastrar/funcionario-cadastrar.component';
import { FuncionarioAtualizarComponent } from './pages/funcionarios/funcionario-atualizar/funcionario-atualizar.component';
import { FuncionarioDeletarComponent } from './pages/funcionarios/funcionario-deletar/funcionario-deletar.component';
import { PedidoListarComponent } from './pages/pedidos/pedido-listar/pedido-listar.component';
import { PedidoCriarComponent } from './pages/pedidos/pedido-criar/pedido-criar.component';
import { PedidoAtualizarComponent } from './pages/pedidos/pedido-atualizar/pedido-atualizar.component';
import { PedidoDeletarComponent } from './pages/pedidos/pedido-deletar/pedido-deletar.component';
import { RomaneioListarComponent } from './pages/romaneios/romaneio-listar/romaneio-listar.component';
import { RomaneioCriarComponent } from './pages/romaneios/romaneio-criar/romaneio-criar.component';
import { RomaneioAtualizarComponent } from './pages/romaneios/romaneio-atualizar/romaneio-atualizar.component';
import { RomaneioDeletarComponent } from './pages/romaneios/romaneio-deletar/romaneio-deletar.component';
import { RastreioComponent } from './pages/rastreio/rastreio.component';
import { RomaneioFechamentoComponent } from './pages/romaneios/romaneio-fechamento/romaneio-fechamento.component';
import { UsuarioListarComponent } from './pages/usuarios/usuario-listar/usuario-listar.component';
import { UsuarioDeletarComponent } from './pages/usuarios/usuario-deletar/usuario-deletar.component';
import { UsuarioAtualizarComponent } from './pages/usuarios/usuario-atualizar/usuario-atualizar.component';
import { UsuarioCadastrarComponent } from './pages/usuarios/usuario-cadastrar/usuario-cadastrar.component';

const routes: Routes = [
  {
    path: "",
    component: NavComponent,
    canActivate: [AuthGuard],
    data: {
      role: ['ADMIN', 'STANDARD']
    },
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
      {
        path: "transportadores",
        component: TransportadorListarComponent
      },
      {
        path: "transportadores/cadastrar",
        component: TransportadorCadastrarComponent
      },
      {
        path: "transportadores/atualizar/:id",
        component: TransportadorAtualizarComponent
      },
      {
        path: "transportadores/deletar/:id",
        component: TransportadorDeletarComponent
      },
      {
        path: "produtos",
        component: ProdutoListarComponent
      },
      {
        path: "produtos/cadastrar",
        component: ProdutoCadastrarComponent
      },
      {
        path: "produtos/atualizar/:id",
        component: ProdutoAtualizarComponent
      },
      {
        path: "produtos/deletar/:id",
        component: ProdutoDeletarComponent
      },
      {
        path: "usuarios",
        component: FuncionarioListarComponent
      },
      {
        path: "usuarios/cadastrar",
        component: FuncionarioCadastrarComponent
      },
      {
        path: "usuarios/atualizar/:id",
        component: FuncionarioAtualizarComponent
      },
      {
        path: "usuarios/deletar/:id",
        component: FuncionarioDeletarComponent
      },
      {
        path: "pedidos",
        component: PedidoListarComponent
      },
      {
        path: "pedidos/cadastrar",
        component: PedidoCriarComponent
      },
      {
        path: "pedidos/atualizar/:id",
        component: PedidoAtualizarComponent
      },
      {
        path: "pedidos/deletar/:id",
        component: PedidoDeletarComponent
      },
      {
        path: "romaneios",
        component: RomaneioListarComponent
      },
      {
        path: "romaneios/cadastrar",
        component: RomaneioCriarComponent
      },
      {
        path: "romaneios/atualizar/:id",
        component: RomaneioAtualizarComponent
      },
      {
        path: "romaneios/deletar/:id",
        component: RomaneioDeletarComponent
      },
      {
        path: "romaneios/fechamento/:id",
        component: RomaneioFechamentoComponent
      }
    ]
  },
  //Admin
  {
    path: "",
    component: NavComponent,
    canActivate: [AuthGuard],
    data: {
      role: ['ADMIN']
    },
    children: [
      {
        path: "sistema/usuarios/cadastrar",
        component: UsuarioCadastrarComponent
      },
      {
        path: "sistema/usuarios/atualizar/:id",
        component: UsuarioAtualizarComponent
      },
      {
        path: "sistema/usuarios/deletar/:id",
        component: UsuarioDeletarComponent
      },
      {
        path: "sistema/usuarios",
        component: UsuarioListarComponent
      }
    ]
  },
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "rastreamento",
    component: RastreioComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
