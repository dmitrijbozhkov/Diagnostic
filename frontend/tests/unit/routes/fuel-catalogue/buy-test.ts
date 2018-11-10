import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | fuel-catalogue/buy', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:fuel-catalogue/buy');
    assert.ok(route);
  });
});
