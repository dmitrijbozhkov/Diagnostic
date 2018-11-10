import DS from 'ember-data';
import RSVP from 'rsvp';

export default class FuelFuelDetails extends DS.JSONAPIAdapter.extend({
  namespace: "/api/fuel",
  findAll() {
    return new RSVP.Promise((resolve, reject) => {
      $.ajax({
        type: "GET",
        url: this.get("namespace") + "/get-all",
        dataType: "json"
      }).then(function(data) {
        resolve(data);
      }, function(err) {
        reject(err);
      })
    });
  },
  findRecord(store: any, type: any, id: string) {
    return new RSVP.Promise((resolve, reject) => {
      $.ajax({
        type: "POST",
        url: this.get("namespace") + "/get",
        dataType: "json",
        data: JSON.stringify({
          fuelName: id
        })
      }).then(function(data) {
        resolve(data);
      }, function(err) {
        reject(err);
      });
    })
  },
  createRecord() {

  },
  updateRecord() {

  },
  deleteRecord() {

  },
  query() {

  }
}) {}

// DO NOT DELETE: this is how TypeScript knows how to look up your adapters.
declare module 'ember-data/types/registries/adapter' {
  export default interface AdapterRegistry {
    'fuel/fuel-details': FuelFuelDetails;
  }
}
