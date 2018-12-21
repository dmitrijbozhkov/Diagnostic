import Component from '@ember/component';
import $ from "jquery";

export default Component.extend({
    email: "",
    password: "",
    actions:{
        auth:function(){
            let email = this.get("email");
            let password = this.get("password");
            $.ajax({
                type: "POST",
                url: "src/main/java/org/nure/diagnosis/controllers/UserController.java",
                contentType: "application/json",
                data: JSON.stringify({
                    email: email,
                    password: password
                })
            }).then(function(response) {
                alert("Roooock");
            })
        }
    }
});
