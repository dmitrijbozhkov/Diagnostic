import EmberObject from '@ember/object';
import AuthenticatedBuyerMixinMixin from 'frontend/mixins/authenticated-buyer-mixin';
import { module, test } from 'qunit';
import sinon from 'sinon';

module('Unit | Mixin | AuthenticatedBuyerMixin', function() {
  // Replace this with your real tests.
  function prepareMixin() {
    let mixin = EmberObject.extend(AuthenticatedBuyerMixinMixin).create();
    mixin.set("notify", sinon.spy());
    return mixin;
  }
  test('it works', function (assert) {
    let AuthenticatedUserMixinObject = EmberObject.extend(AuthenticatedBuyerMixinMixin);
    let subject = AuthenticatedUserMixinObject.create();
    assert.ok(subject);
  });

  test("", function(assert) {

  });q
});
