import DS from 'ember-data';

export default class FuelFuelDetails extends DS.Model.extend({
  fuelName: DS.attr("string"),
  price: DS.attr("number"),
  fuelLeft: DS.attr("number"),
  maxFuel: DS.attr("number"),
  description: DS.attr("string")
}) {
  // normal class body definition here
}

// DO NOT DELETE: this is how TypeScript knows how to look up your models.
declare module 'ember-data/types/registries/model' {
  export default interface ModelRegistry {
    'fuel/fuel-details': FuelFuelDetails;
  }
}
