import Component from '@ember/component';
import { inject as service } from '@ember/service';

export default class LoginForm extends Component.extend({
  username: "",
  password: ""
}) {
  session = (service("session" as any) as any)
  actions = {
    loginUser(this: LoginForm) {
      this.get("session").authenticate('authenticator:token', this.get("username"), this.get("password"));
      console.log(this.get("username") + this.get("password"));
    }
  }
};
