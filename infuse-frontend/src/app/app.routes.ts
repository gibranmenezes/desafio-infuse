import { Routes } from '@angular/router';
import { ConsultaCreditoComponent } from './components/consulta-credito/consulta-credito.component';

export const routes: Routes = [
  { path: '', redirectTo: '/consulta', pathMatch: 'full' },
  { path: 'consulta', component: ConsultaCreditoComponent },
  { path: '**', redirectTo: '/consulta' }
];