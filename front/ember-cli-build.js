'use strict';

const EmberApp = require('ember-cli/lib/broccoli/ember-app');

module.exports = function(defaults) {
  let app;
    let imgDir = "../src/main/resources/static/images";
    if (process.env.EMBER_ENV === "production") { // ember build --output-path="../src/main/resources" --environment="production"
        app = new EmberApp(defaults, {
            outputPaths: {
              app: {
                html: "/templates/index.html",
                css: {
                  "app": "/static/css/gas-station.css"
                },
                js: "/static/js/gas-station.js"
              },
              vendor: {
                css: "/static/css/vendor.css",
                js: "/static/js/vendor.js"
              }
            },
            fingerprint: {
                enabled: false
            },
            inlineContent: {
                "vendor-css": { content: "th:href='@{/css/vendor.css}'" },
                "app-css": { content: "th:href='@{/css/gas-station.css}'" },
                "vendor-js": { content: "th:src='@{/js/vendor.js}'" },
                "app-js": { content: "th:src='@{/js/gas-station.js}'" },
                "thymeleaf": { content: " xmlns:th='http://www.thymeleaf.org'" }
            }
          });
        if (!fs.existsSync(imgDir)){
            fs.mkdirSync(imgDir);
        }
    } else { // ember build
        app = new EmberApp(defaults, {
            inlineContent: {
                "vendor-css": { content: "href='/assets/vendor.css'" },
                "app-css": { content: "href='/assets/front.css'" },
                "vendor-js": { content: "src='/assets/vendor.js'" },
                "app-js": { content: "src='/assets/front.js'" },
                "thymeleaf": { content: "" }
            }
        });
    }
    app.import("vendor/bulma.min.css");
    app.import("vendor/bulma-extensions.min.css")
  return app.toTree();
};
