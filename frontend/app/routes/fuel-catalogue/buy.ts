import Route from '@ember/routing/route';
import authenticatedBuyerMixin from 'frontend/mixins/authenticated-buyer-mixin';

// @ts-ignore
export default class FuelCatalogueBuy extends Route.extend(authenticatedBuyerMixin, {
}) {
  model({ fuel_id } : { fuel_id: string }) {
    return this.store.findRecord("fuel/fuel-details", fuel_id);
  }
}
