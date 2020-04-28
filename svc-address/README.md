##  Project name: 
Address service

### Description: 
Address REST service with Mongo DB support.

### Table of Contents: 

### Installation:
1. install Mongo DB database with default port(27017) and default host (127.0.0.1)

2. start your Mongo DB instance

3. go to GIT bucket source code repository `src-address`

4. run build (such as run the command `gradle build`)

5. go to folder `./build/libs`

6. run command `java -jar MongoRestGradle.jar`

7. use curl, or broswer, or postman to test Address service endpoints

### Usage
##### 1. Get all the addresses in data base
```GET  /api/address/all```

##### 2. Insert a new address

`POST /api/address`

content-type: `application/json`

request body: 

 `{
   "firstname": "Tom2",
   "middlename": null,
   "lastname": "Jacksons2",
   "addressLine1": "200 River street",
   "addressLine2": null,
   "city": "Hoboken",
   "state": "NJ",
   "zipCode": "00731"
 }`

##### 3. Delete an existing address
```DELETE /api/address/lastnameAndFirstname?lastName=Littles&firstName=Nancy'```

content-type: `application/json`

request body: 

`{
        "firstname": "Nancy",
        "middlename": null,
        "lastname": "Littles",
        "addressLine1": "121 River street",
        "addressLine2": null,
        "city": "Hoboken",
        "state": "NJ",
        "zipCode": "00730"
      }`


##### 4. Update an existing address
```PUT /api/address/lastnameAndFirstname?lastName=Littles&firstName=Nancy'```  

content-type: `application/json`

request body: 

`{
        "firstname": "Nancy",
        "middlename": null,
        "lastname": "Littles",
        "addressLine1": "121 River street",
        "addressLine2": null,
        "city": "Hoboken",
        "state": "NJ",
        "zipCode": "00730"
      }`

