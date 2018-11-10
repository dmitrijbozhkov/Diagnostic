import Route from '@ember/routing/route';

export default class FuelCatalogueIndex extends Route.extend({
  // anything which *must* be merged to prototype here
}) {
  model() {
    return this.store.findAll("fuel/fuel-details");
  }
}
