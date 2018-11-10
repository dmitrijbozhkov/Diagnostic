import Route from '@ember/routing/route';

export default class Index extends Route.extend({
  model() {
    return this.store.findAll("fuel/fuel-details");
  }
  // anything which *must* be merged to prototype here
}) {
  
}
