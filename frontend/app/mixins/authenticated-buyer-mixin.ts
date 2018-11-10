import Mixin from '@ember/object/mixin';
import { getOwner } from '@ember/application';
import AuthenticatedRouteMixin from 'ember-simple-auth/mixins/authenticated-route-mixin';
import Ember from 'ember';
import { inject } from '@ember/service';

const AUTH_CLAIM = "scopes";
export enum Roles {
  buyer = "ROLE_BUYER",
  admin = "ROLE_ADMIN"
}

export enum AuthErrors {
  not_authorized = "User is not authorized",
  no_token = "No token found in the store",
  no_authority = "User doesn't have proper role"
}

export default Mixin.create(AuthenticatedRouteMixin, {
  notify: inject("toast" as any) as any,

  transitionToAuthenticate(transition: any) {
    if (this.get('_isFastBoot' as any)) {
      const fastboot = getOwner(this).lookup('service:fastboot');
      const cookies = getOwner(this).lookup('service:cookies');
      cookies.write('ember_simple_auth-redirectTarget', transition.intent.url, {
        path: '/',
        secure: fastboot.get('request.protocol') === 'https'
      });
    } else {
      this.set('session.attemptedTransition' as any, transition);
    }
    (this as any).triggerAuthentication();
  },
  populateClaims() {
    const token = this.get('session.data.authenticated.token' as any);
    if (!Ember.isEmpty(token)) {
      var claims = JSON.parse(atob(token.split('.')[1]));
      this.set('session.claims' as any, claims);
      return claims;
    } else {
      throw new Error(AuthErrors.no_token);
    }
  },
  checkClaims() {
    let claims: string[] = this.get(('session.claims.' + AUTH_CLAIM) as any);
    if (!claims) {
      claims = this.populateClaims();
    }
    console.log(claims);
    if (this.authClaimChecker(claims)) {
      return this._super(...arguments);
    } else {
      throw new Error(AuthErrors.no_authority);
    }
  },
  beforeModel(transition: any) {
    try {
      if (!this.get('session.isAuthenticated' as any)) {
        throw new Error(AuthErrors.not_authorized);
      } else {
        return this.checkClaims(); 
      }
    } catch(err) {
      console.log(err);
      if (err.message === AuthErrors.not_authorized) {
        this.notAuthorized(transition, err.message);
      }
      if (err.message === AuthErrors.no_token) {
        this.noToken(transition, err.message);
      }
      if (err.message === AuthErrors.no_authority) {
        this.noAuthority(transition, err.message);
      }
    }
  },

  // Methods to re-implement

  // Check for proper claims
  authClaimChecker(claim: string[]) {
    return claim.find((claim) => {
      return claim === Roles.admin || claim === Roles.buyer;
    });
  },

  // Authentication error events
  notAuthorized(transition: any, message: string) {
    this.notify.error("Please enter your login and password", "You are not authorized");
    this.transitionToAuthenticate(transition);
  },
  noToken(transition: any, message: string) {
    this.notify.error("Please login one mre time", "Token not found");
    this.transitionToAuthenticate(transition);
  },
  noAuthority(transition: any, message: string) {
    this.notify.error("You don't have enough authority to access this resource", "Please connect with administrator");
    transition.abort();
  }
});
