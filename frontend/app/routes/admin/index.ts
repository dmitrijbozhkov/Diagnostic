import Route from '@ember/routing/route';
import authenticatedAdminMixin from 'frontend/mixins/authenticated-admin-mixin';

// @ts-ignore
export default class AdminIndex extends Route.extend(authenticatedAdminMixin, {
  // anything which *must* be merged to prototype here
}) {
  // normal class body definition here
}
