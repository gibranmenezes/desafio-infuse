import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MaterialModule } from '../../shared/material.module';
import { Subject, takeUntil } from 'rxjs';
import { ConsultaHandlerService } from '../../services/consulta-handler.service';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-consulta-credito',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MaterialModule
  ],
  templateUrl: './consulta-credito.component.html',
  styleUrls: ['./consulta-credito.component.scss']
})
export class ConsultaCreditoComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private consultaHandler = inject(ConsultaHandlerService);
  private destroy$ = new Subject<void>();
  
  consultaForm = this.fb.group({
    tipoConsulta: ['nfse', Validators.required],
    numeroConsulta: ['', [Validators.required, Validators.minLength(4)]]
  });
  
  creditos: Credito[] = [];
  credito: Credito | null = null;
  creditoSelecionadoId: string | null = null;
  creditoDetalhe: Credito | null = null;
  loadingDetalhe = false;
  loading = false;
  mensagemErro = '';
  mensagemErroDetalhe = '';
  displayedColumns: string[] = [
    'numeroCredito', 'numeroNfse', 'dataConstituicao', 
    'valorIssqn', 'tipoCredito', 'detalhes'
  ];
  
  tipoResultadoExibido: 'nfse' | 'credito' | null = null;

  ngOnInit(): void {
    this.consultaForm.get('tipoConsulta')?.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.consultaForm.get('numeroConsulta')?.setValue('');
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get tipoConsulta() {
    return this.consultaForm.get('tipoConsulta')?.value || 'nfse';
  }

  get labelConsulta() {
    return this.tipoConsulta === 'nfse' ? 'Número da NFS-e' : 'Número do Crédito';
  }

  consultar() {
    if (this.consultaForm.invalid) {
      this.consultaHandler.mostrarMensagem('Por favor, preencha o campo corretamente');
      return;
    }

    const numeroConsulta = this.consultaForm.get('numeroConsulta')?.value ?? '';
    this.resetarEstado();
    this.loading = true;
    this.tipoResultadoExibido = this.tipoConsulta as 'nfse' | 'credito';

    if (this.tipoConsulta === 'nfse') {
      this.executarConsultaPorNfse(numeroConsulta);
    } else {
      this.executarConsultaPorNumeroCredito(numeroConsulta);
    }
  }

  resetarEstado(): void {
    this.credito = null;
    this.creditos = [];
    this.creditoSelecionadoId = null;
    this.creditoDetalhe = null;
    this.mensagemErro = '';
    this.mensagemErroDetalhe = '';
  }

  executarConsultaPorNfse(numeroNfse: string): void {
    this.consultaHandler.consultarPorNfse(numeroNfse)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.loading = false;
          console.log('Resposta completa da consulta NFSe:', response);
          
          const resultado = this.consultaHandler.processarResposta(
            response, 
            'Nenhum crédito encontrado para este número de NFS-e'
          );
          
          if (resultado?.mensagemErro) {
            this.mensagemErro = resultado.mensagemErro;
            this.creditos = [];
          } else {
            this.mensagemErro = '';
            this.creditos = resultado || [];
          }
        },
        error: (error) => {
          this.loading = false;
          const resultado = this.consultaHandler.tratarErro(error, 'Erro ao consultar créditos');
          this.mensagemErro = resultado.mensagemErro;
          this.creditos = [];
        }
      });
  }

  executarConsultaPorNumeroCredito(numeroCredito: string): void {
    this.consultaHandler.consultarPorNumeroCredito(numeroCredito)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.loading = false;
          console.log('Resposta completa da consulta de crédito:', response);
          
          const resultado = this.consultaHandler.processarResposta(
            response, 
            'Crédito não encontrado para o número informado'
          );
          
          if (resultado?.mensagemErro) {
            this.mensagemErro = resultado.mensagemErro;
            this.credito = null;
          } else {
            this.mensagemErro = '';
            this.credito = resultado;
          }
        },
        error: (error) => {
          this.loading = false;
          if (error.status === 404 || 
              (error.error && error.error.message && 
               error.error.message.toLowerCase().includes('não encontrado'))) {
            this.mensagemErro = 'Crédito não encontrado para o número informado';
          } else {
            const resultado = this.consultaHandler.tratarErro(error, 'Erro ao consultar crédito');
            this.mensagemErro = resultado.mensagemErro;
          }
          this.credito = null;
        }
      });
  }

  verDetalhes(numeroCredito: string): void {
    if (this.creditoSelecionadoId === numeroCredito) {
      this.creditoSelecionadoId = null;
      this.creditoDetalhe = null;
      this.mensagemErroDetalhe = '';
      return;
    }

    this.creditoSelecionadoId = numeroCredito;
    this.loadingDetalhe = true;
    this.mensagemErroDetalhe = '';
    
    this.consultaHandler.consultarPorNumeroCredito(numeroCredito)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.loadingDetalhe = false;
          const resultado = this.consultaHandler.processarResposta(
            response, 
            'Não foi possível carregar os detalhes do crédito'
          );
          
          if (resultado?.mensagemErro) {
            this.mensagemErroDetalhe = resultado.mensagemErro;
            this.creditoDetalhe = null;
          } else {
            this.mensagemErroDetalhe = '';
            this.creditoDetalhe = resultado;
          }
        },
        error: (error) => {
          this.loadingDetalhe = false;
          const resultado = this.consultaHandler.tratarErro(error, 'Erro ao carregar os detalhes do crédito');
          this.mensagemErroDetalhe = resultado.mensagemErro;
          this.creditoDetalhe = null;
        }
      });
  }

  isCreditoSelecionado(numeroCredito: string): boolean {
    return this.creditoSelecionadoId === numeroCredito;
  }
}