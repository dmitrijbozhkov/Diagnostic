import EmberObject from '@ember/object';
import AuthenticatedAdminMixinMixin from 'frontend/mixins/authenticated-admin-mixin';
import { module, test } from 'qunit';

module('Unit | Mixin | AuthenticatedAdminMixin', function() {
  // Replace this with your real tests.
  test('it works', function (assert) {
    let AuthenticatedAdminMixinObject = EmberObject.extend(AuthenticatedAdminMixinMixin);
    let subject = AuthenticatedAdminMixinObject.create();
    assert.ok(subject);
  });
});
