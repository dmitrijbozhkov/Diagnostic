import Route from '@ember/routing/route';
import { inject } from "@ember/service";

export default class Login extends Route.extend({
  session: inject("session" as any)
}) {
  actions = {
    submit(this: Login, username: string, password: string) {
      if (username && password) {
        try {
          const credentials = { username: username, password: password };
          this.get("session").authenticate("authenticator:token", credentials);
        } catch(ex) {
          console.log(ex);
        }
      }
    }
  }
}
