import { Routes } from '@angular/router';
import { StadiumForm } from './component/stadium-form/stadium-form';

export const routes: Routes = [
	{
		path: '',
		component: StadiumForm
	},
	{
		path: 'stadium',
		component: StadiumForm
	}
];
