# fuelapp
## Sample Spring Boot REST application for fuel consumption management.

Several notes:

1. Startup. App can be easily dockerized or just put running on local machine. The jar archive for easy and fast trial is present.
2. Database. App uses embedded H2 database, which works in runtime (only) for convenience reasons. App can be easily migrated to another database by putting corresponding maven dependency and spring db driver in application.properties.
3. Service layer is covered by tests. 
4. Access REST API via swagger interface: `localhost:8080/swagger-ui.html`
5. Some sample data for testing `entry/bulk` endpoint: 

```
[
  {
    "date": "02.21.2018",
    "driverId": 1,
    "fuelType": "A95",
    "price": 110,
    "volume": 10
  },
 {
    "date": "02.22.2018",
    "driverId": 1,
    "fuelType": "A95",
    "price": 100,
    "volume": 10
  },
 {
    "date": "02.21.2018",
    "driverId": 2,
    "fuelType": "A95",
    "price": 110,
    "volume": 10
  },
 {
    "date": "03.21.2018",
    "driverId": 1,
    "fuelType": "A95",
    "price": 110,
    "volume": 10
  }
]
```

