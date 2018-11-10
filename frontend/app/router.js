import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route("login");
  this.route("register");
  this.route('profile', function() {});
  this.route('admin', function() {});
  this.route('fuel-catalogue', function() {
    this.route('buy', { path: "/buy/:fuel_id" });
  });
  this.route('about');

});

export default Router;
