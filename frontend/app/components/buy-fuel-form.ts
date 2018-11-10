import Component from '@ember/component';
import { computed } from '@ember/object';

const USD_TO_BITCOIN_LINK = "https://blockchain.info/tobtc?currency=USD&value=1";
const ROUND_ZEROES = 5;

export default class BuyFuelForm extends Component.extend({
  positionalParams: ["model"],
  selectedFuelAmount: 0,
  exchangeRate: 0,
  isBitcoinModalOpen: false,
  representSelectedFuelAmount: computed("selectedFuelAmount", function() {
    return this.get("selectedFuelAmount") ? this.get("selectedFuelAmount") : 0;
  }),
  selctedFuelPrice: computed("representSelectedFuelAmount", function() {
    return Number((this.get("representSelectedFuelAmount") * this.get("model.price")).toPrecision(ROUND_ZEROES));
  }),
  bitcoinPrice: computed("exchangeRate", "selctedFuelPrice", function() {
    return  Number((this.get("representSelectedFuelAmount") * this.get("model.price") * this.get("exchangeRate")).toPrecision(ROUND_ZEROES));
  })
}) {
  actions = {
    fuelMoved(this: BuyFuelForm, event: any) {
      let selectedFuel = Number(event.target.value);
      let maxFuel = this.get("model.fuelLeft" as any);
      if (selectedFuel > maxFuel) {
        this.set("selectedFuelAmount", maxFuel);
        event.target.value = maxFuel;
      } else {
        this.set("selectedFuelAmount", selectedFuel);
      }
    },
    fuelUpdate(this: BuyFuelForm) {
      this.updateExchangeRate();
    },
    oneMoreLiter(this: BuyFuelForm) {
      let selectedFuel = this.get("selectedFuelAmount");
      let maxFuel = this.get("model.fuelLeft" as any);
      if (selectedFuel < maxFuel) {
        this.set("selectedFuelAmount", selectedFuel + 1);
      }
      this.updateExchangeRate();
    },
    oneLessLiter(this: BuyFuelForm) {
      let selectedFuel = this.get("selectedFuelAmount");
      if (selectedFuel) {
        this.set("selectedFuelAmount", selectedFuel - 1);
      }
      this.updateExchangeRate();
    },
    toggleBuyFuelModal(this: BuyFuelForm) {
      if (this.get("representSelectedFuelAmount")) {
        this.set("isBitcoinModalOpen", !this.get("isBitcoinModalOpen"));
      }
    }
  }
  updateExchangeRate(this: BuyFuelForm) {
    $.ajax({
      type: "GET",
      url: USD_TO_BITCOIN_LINK,
    }).then((rate: number) => {
      this.set("exchangeRate", rate);
    });
  }
};
