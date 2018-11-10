import Component from '@ember/component';

export default class NavbarHeader extends Component.extend({
  tagName: "nav",
  classNames: ["navbar", "is-white"],
  isBurgerOpen: false,
  globalUnToggleEvent: null,
  didInsertElement() {
    this._super(...arguments);
    this.set("globalUnToggleEvent", this.$(document).click((e) => {
      if (!$(e.target).hasClass("navbar-burger")) {
        this.set("isBurgerOpen", false);
      }
    }));
  },
  didDestroyElement() {
    this._super(...arguments);
    (this.get("globalUnToggleEvent") as any).unbind();
  }
}) {
  actions = {
    toggleBurger(this: NavbarHeader) {
      console.log("This is clicked");
        this.toggleProperty("isBurgerOpen");
    }
}
};
