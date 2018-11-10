import Mixin from '@ember/object/mixin';
import authenticatedBuyerMixin, { Roles } from './authenticated-buyer-mixin';

export default Mixin.create(authenticatedBuyerMixin, {
  authClaimChecker(claim: string[]) {
    return claim.find((claim) => {
      return claim === Roles.admin;
    });
  }
});
