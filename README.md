# Overlook Hotel

This is an app to manage the reserves for the overlook-hotel

## Usage

**Note: As the application is hosted in the free version of heroku, \
it might take a while to answer on the first call, this is because it sleeps after an idle time**
<br>
<br>
**Users:**
<br>
To be able to place a reservation in overlook-hotel, you'll need to create an user using the route: [POST] /users

````
curl --location --request POST 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/users' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "name": "Jack Torrance",
    "email": "jack@gmail.com",
    "birthday": "10/10/1962"
  }'
````

You can also modify the user created, in the route [PUT] /users/{id}

`````
curl --location --request PUT 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/users/3' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "name": "Jack Torrance Jr",
    "email": "jack@gmail.com",
    "birthday": "10/10/1962"
  }'
`````

To delete an user, use the route [DELETE] /users/{id}

`````
curl --location --request DELETE 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/users/3'
`````

And finally, you can list the users or get one by id on [GET] /users or /users/{id}

`````
curl --location --request GET 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/users?page=0&page_size=20'
`````

`````
curl --location --request GET 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/users/1'
`````

**Reservations:**
<br>
In order to place a reservation in the overlook-hotel, you'll have to call the route [POST] /reservations.

`````
curl --location --request POST 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/reservations' \
--header 'Content-Type: application/json' \
--data-raw '{
    "user_id": 1,
    "start_date": "21/09/2021",
    "end_date": "22/09/2021"
}'
`````

Note that some validations we'll be applied to the requisitions:

- Both dates must be future dates
- The stay can’t be longer than 3 days
- Can’t be reserved more than 30 days in advance
- All reservations start at least the next day of booking
- Dates are in the format dd/MM/yyyy
- The user informed must exist

To alter the date from a reservation, you can use the route [PATCH] /reservations/{id}

`````
curl --location --request PATCH 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/reservations/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "start_date": "30/11/2021",
    "end_date": "03/11/2021"
}'
`````

To see the reservation, you can call the route [GET] /reservations/{id}

`````
curl --location --request GET 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/reservations/1'
`````

And to cancel one reservation, [DELETE] /reservations/{id}

`````
curl --location --request DELETE 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/reservations/1'
`````

**Availability:**
You can check the availability for the room in two ways, by a period of time, using the route [GET] /availability,
passing the start_date and end_date as params:

`````
curl --location --request GET 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/availability?start_date=28/09/2021&end_date=28/09/2021'
`````

Or you can check the availability for all the days of a given month, using the route [GET] /availability/month passing
month and year as params

`````
curl --location --request GET 'https://overlook-hotel-app.herokuapp.com/overlook-hotel/availability/month?year=2021&month=9'
`````

## Starting app locally

First start the database, go to /overlook-hotel/infra and execute the command below:

`````
docker-compose up -d
`````

Then, go to the base folder (/overlook-hotel) and execute the following command in order to start the application:

````
mvn spring-boot:run -Dspring-boot.run.profiles=local
````

The application will start on the port 8080, so to use the examples above, just change the route for:

`````
http://localhost:8080/overlook-hotel/
`````

instead of

`````
https://overlook-hotel-app.herokuapp.com/overlook-hotel/
`````

You can also use swagger to access the apis:

https://overlook-hotel-app.herokuapp.com/overlook-hotel/swagger-ui/