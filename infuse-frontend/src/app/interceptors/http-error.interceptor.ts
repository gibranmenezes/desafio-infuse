import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);
  
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocorreu um erro desconhecido';
      
      if (error.error instanceof ErrorEvent) {
        // Cliente-side error
        errorMessage = `Erro: ${error.error.message}`;
      } else {
        // Server-side error
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        } else if (error.status === 0) {
          errorMessage = 'Erro de conexão com o servidor';
        } else if (error.status === 404) {
          errorMessage = 'Recurso não encontrado';
        } else if (error.status === 400) {
          errorMessage = 'Requisição inválida';
        } else if (error.status === 401) {
          errorMessage = 'Não autorizado';
        } else if (error.status === 403) {
          errorMessage = 'Acesso negado';
        } else if (error.status === 500) {
          errorMessage = 'Erro interno do servidor';
        }
      }
      
      if (error.status !== 204) {
        snackBar.open(errorMessage, 'Fechar', {
          duration: 5000,
          panelClass: ['error-snackbar']
        });
      }
      
      return throwError(() => new Error(errorMessage));
    })
  );
};