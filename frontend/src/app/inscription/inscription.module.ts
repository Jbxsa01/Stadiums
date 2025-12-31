import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { InscriptionDashboardComponent } from './inscription-dashboard.component';
import { InscriptionFormComponent } from './inscription-form.component';
import { CampaignsComponent } from './campaigns.component';
import { WorkflowComponent } from './workflow.component';
import { AdminComponent } from './admin.component';

const routes: Routes = [
  { path: '', component: InscriptionDashboardComponent },
  { path: 'form', component: InscriptionFormComponent },
  { path: 'campaigns', component: CampaignsComponent },
  { path: 'workflow', component: WorkflowComponent },
  { path: 'admin', component: AdminComponent }
];

@NgModule({
  declarations: [
    InscriptionDashboardComponent,
    InscriptionFormComponent,
    CampaignsComponent,
    WorkflowComponent,
    AdminComponent
  ],
  imports: [CommonModule, RouterModule.forChild(routes)]
})
export class InscriptionModule {}
