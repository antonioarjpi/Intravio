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
import { MatSortModule } from '@angular/material/sort';
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from "@angular/material/table";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatDialogModule } from "@angular/material/dialog";
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import { MatStepperModule } from '@angular/material/stepper';
import { CurrencyPipe } from '@angular/common';
import { ToastrModule } from "ngx-toastr";
import { NgxMaskModule } from 'ngx-mask';

import { NavComponent } from './components/nav/nav.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { AuthInterceptorProvider } from './interceptors/auth.interceptor';
import { LogoComponent } from './components/logo/logo.component';
import { CadastrarDepartamentoComponent } from './pages/departamentos/cadastrar-departamento/cadastrar-departamento.component';
import { AtualizarDepartamentoComponent } from './pages/departamentos/atualizar-departamento/atualizar-departamento.component';
import { DeletarDepartamentoComponent } from './pages/departamentos/deletar-departamento/deletar-departamento.component';
import { ListarDepartamentoComponent } from './pages/departamentos/listar-departamento/listar-departamento.component';
import { FilialAtualizarComponent } from './pages/filiais/filial-atualizar/filial-atualizar.component';
import { FilialCadastrarComponent } from './pages/filiais/filial-cadastrar/filial-cadastrar.component';
import { FilialDeletarComponent } from './pages/filiais/filial-deletar/filial-deletar.component';
import { FilialListarComponent } from './pages/filiais/filial-listar/filial-listar.component';
import { TransportadorCadastrarComponent } from './pages/transportadores/transportador-cadastrar/transportador-cadastrar.component';
import { TransportadorAtualizarComponent } from './pages/transportadores/transportador-atualizar/transportador-atualizar.component';
import { TransportadorListarComponent } from './pages/transportadores/transportador-listar/transportador-listar.component';
import { TransportadorDeletarComponent } from './pages/transportadores/transportador-deletar/transportador-deletar.component';
import { ProdutoCadastrarComponent } from './pages/produtos/produto-cadastrar/produto-cadastrar.component';
import { ProdutoAtualizarComponent } from './pages/produtos/produto-atualizar/produto-atualizar.component';
import { ProdutoListarComponent } from './pages/produtos/produto-listar/produto-listar.component';
import { ProdutoDeletarComponent } from './pages/produtos/produto-deletar/produto-deletar.component';
import { FuncionarioCadastrarComponent } from './pages/funcionarios/funcionario-cadastrar/funcionario-cadastrar.component';
import { FuncionarioAtualizarComponent } from './pages/funcionarios/funcionario-atualizar/funcionario-atualizar.component';
import { FuncionarioListarComponent } from './pages/funcionarios/funcionario-listar/funcionario-listar.component';
import { FuncionarioDeletarComponent } from './pages/funcionarios/funcionario-deletar/funcionario-deletar.component';
import { PedidoListarComponent } from './pages/pedidos/pedido-listar/pedido-listar.component';
import { PedidoCriarComponent } from './pages/pedidos/pedido-criar/pedido-criar.component';
import { PedidoAtualizarComponent } from './pages/pedidos/pedido-atualizar/pedido-atualizar.component';
import { PedidoDeletarComponent } from './pages/pedidos/pedido-deletar/pedido-deletar.component';
import { PedidoModal } from './pages/pedidos/pedido-listar/pedido-modal';
import { RomaneioListarComponent } from './pages/romaneios/listar-romaneios/romaneio-listar/romaneio-listar.component';
import { RomaneioCriarComponent } from './pages/romaneios/listar-romaneios/romaneio-criar/romaneio-criar.component';
import { RomaneioAtualizarComponent } from './pages/romaneios/listar-romaneios/romaneio-atualizar/romaneio-atualizar.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    LoginComponent,
    HomeComponent,
    LogoComponent,
    CadastrarDepartamentoComponent,
    AtualizarDepartamentoComponent,
    DeletarDepartamentoComponent,
    ListarDepartamentoComponent,
    FilialAtualizarComponent,
    FilialCadastrarComponent,
    FilialDeletarComponent,
    FilialListarComponent,
    TransportadorCadastrarComponent,
    TransportadorAtualizarComponent,
    TransportadorListarComponent,
    TransportadorDeletarComponent,
    ProdutoCadastrarComponent,
    ProdutoAtualizarComponent,
    ProdutoListarComponent,
    ProdutoDeletarComponent,
    FuncionarioCadastrarComponent,
    FuncionarioAtualizarComponent,
    FuncionarioListarComponent,
    FuncionarioDeletarComponent,
    PedidoListarComponent,
    PedidoCriarComponent,
    PedidoAtualizarComponent,
    PedidoDeletarComponent,
    PedidoModal,
    RomaneioListarComponent,
    RomaneioCriarComponent,
    RomaneioAtualizarComponent
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
    MatSortModule,
    MatListModule,
    MatCardModule,
    MatDialogModule,
    MatChipsModule,
    MatStepperModule,
    MatMenuModule,
    MatSlideToggleModule,

    ToastrModule.forRoot({
      timeOut: 4000,
      closeButton: true,
      progressBar: true,
    }),

    NgxMaskModule.forRoot(),
  ],
  providers: [AuthInterceptorProvider, CurrencyPipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
