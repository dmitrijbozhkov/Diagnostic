import Component from '@ember/component';
import $ from "jquery";

export default Component.extend({
    name: "",
    surName: "",
    lastName: "",
    gender: "",
    date: "",
    email: "",
    password: "",
    confPassword: "",
    actions:{
        emailChecker:function(){
            let email = this.get("username");
            $.ajax({
                type: "POST",
                url: "",
                contentType: "application/json",
                data: JSON.stringify({
                    email: email
                }),
                
            }).then(function(response) {
                
            })
        },
        passChecker:function(){
            let password = this.get("password");
            let confPassword = this.get("confPassword");
            if(password != confPassword){
                alert();
            }
        }
    }
});
