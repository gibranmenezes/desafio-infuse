import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreditoService } from '../../services/credito.service';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-detalhe-credito',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './detalhe-credito.component.html',
  styleUrls: ['./detalhe-credito.component.scss']
})
export class DetalheCreditoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private creditoService = inject(CreditoService);
  private snackBar = inject(MatSnackBar);
  
  credito: Credito | null = null;
  loading = true;
  numeroCredito = '';
  
  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const numeroCredito = params.get('numeroCredito');
      if (numeroCredito) {
        this.numeroCredito = numeroCredito;
        this.carregarCredito(numeroCredito);
      } else {
        this.voltar();
      }
    });
  }
  
  carregarCredito(numeroCredito: string) {
    this.loading = true;
    this.creditoService.getCreditoByNumero(numeroCredito).subscribe({
      next: (response) => {
        this.loading = false;
        if (response) {
          // Corrigido para não acessar 'content'
          this.credito = response.content;
        } else {
          this.snackBar.open('Crédito não encontrado', 'Fechar', {
            duration: 3000
          });
          this.voltar();
        }
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open('Erro ao carregar dados do crédito', 'Fechar', {
          duration: 3000
        });
        console.error('Erro ao carregar crédito:', error);
        this.voltar();
      }
    });
  }
  
  voltar() {
    this.router.navigate(['/consulta']);
  }
}