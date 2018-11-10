import { faker } from 'ember-cli-mirage';

const TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXR2aWVpIiwic2NvcGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTU0MTE3NzczOCwiZXhwIjoxNTQxMTk1NzM4fQ.CtdZkkk343yQnCRyRqOoWSf07N9FTyVa9b50bNpPVFk"

const fuelList = [
  {
    fuelName: "95",
    price: 9.99,
    fuelLeft: 5000,
    maxFuel: 10000,
    description: faker.lorem.sentences()
  },
  {
    fuelName: "92",
    price: 7.99,
    fuelLeft: 3000,
    maxFuel: 10000,
    description: faker.lorem.sentences()
  },
  {
    fuelName: "95+",
    price: 13.99,
    fuelLeft: 8000,
    maxFuel: 10000,
    description: faker.lorem.sentences()
  }
]

const USER = {
  name: "Matviei",
  surname: "Serbull",
  username: "matviei",
  password: "pass1234"
}

export default function() {
  this.passthrough("https://blockchain.info/**");

  this.namespace = "/api";
  
  this.get("/fuel/get-all", function() {
    return {
      fuels: fuelList
    };
  });
  this.post("/fuel/get", function(schema, request) {
    const fuelName = JSON.parse(request.requestBody).fuelName;
    return fuelList.find((fuel) => {
      return fuel.fuelName === fuelName;
    });
  });
  this.post("/user/signin", function(schema, request) {
    const userDetails = JSON.parse(request.requestBody)
    if (userDetails.name === USER.name &&
        userDetails.surname === USER.surname &&
        userDetails.username === USER.surname &&
        userDetails.password === USER.password) {
      return 200;
    }
  });
  this.post("/user/login", function(schema, request) {
    const userDetails = JSON.parse(request.requestBody)
    if (userDetails.username === USER.surname &&
        userDetails.password === USER.password) {
      return {
        token: TOKEN
      };
    } else {
      return 403;
    }
  });

  // These comments are here to help you get started. Feel free to delete them.

  /*
    Config (with defaults).

    Note: these only affect routes defined *after* them!
  */

  // this.urlPrefix = '';    // make this `http://localhost:8080`, for example, if your API is on a different server
  // this.namespace = '';    // make this `/api`, for example, if your API is namespaced
  // this.timing = 400;      // delay for each request, automatically set to 0 during testing

  /*
    Shorthand cheatsheet:

    this.get('/posts');
    this.post('/posts');
    this.get('/posts/:id');
    this.put('/posts/:id'); // or this.patch
    this.del('/posts/:id');

    http://www.ember-cli-mirage.com/docs/v0.3.x/shorthands/
  */
}
