import DS from 'ember-data';

interface IFuelDetails {
  fuelName: string;
  price: number;
  fuelLeft: number;
  maxFuel: number;
  description: string;
}

export default class FuelFuelDetails extends DS.JSONAPISerializer.extend({
  normalizeFuel(fuel: IFuelDetails, className: string) {
    return {
      id: fuel.fuelName,
      type: className,
      attributes: fuel
    }
  },
  normalizeResponse(store: any, modelClass: any, payload: any, id: any, requestType: string) {
    if (requestType === "findAll") {
      payload.fuels["id"] = payload.fuels.fuelName;
      return this.normalize(modelClass, payload.fuels
        .map((fuel: any) => {
          return this.normalizeFuel(fuel, modelClass.modelName);
        }));
    }
    if (requestType === "findRecord") {
      return this.normalize(modelClass, this.normalizeFuel(payload, modelClass.modelName))
    }
    throw new Error("Method not implemented");
  },
  normalize(modelClass: any, resourceHash: any) {
    return {
      data: resourceHash
    };
  },
  serialize(snapshot: any, options: any) {
    return snapshot;
  }
}) {}

// DO NOT DELETE: this is how TypeScript knows how to look up your serializers.
declare module 'ember-data/types/registries/serializer' {
  export default interface SerializerRegistry {
    'fuel/fuel-details': FuelFuelDetails;
  }
}
