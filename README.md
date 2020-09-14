# HotSauces

![hotsauce title banner](http://globalnerdy.com/wp-content/uploads/2020/09/hotsauce-title-banner.jpg)


## Introduction

This is the repository for the article ***Build and Secure an API with Spring Boot, Kotlin, and Auth0***. 

It’s made up of two separate projects, each in its own directory:

* **app**: The code for the project, which is an API implemented in Spring Boot and Kotlin that exposes a single resource: A catalog of hot sauces. The project’s structure uses Spring Initializr scaffolding and uses Gradle as its build tool.
* **article**: The article, which first shows the reader how to build an API with Spring Boot and Kotlin, and then walks them through the process of securing the API with Auth0. The article is written in Markdown.


## Requirements

In order to follow the steps to build this project, you'll need the following:

* **JDK 11**. This has been tested using both Oracle’s Java SE 11.0.8+10 and OpenJDK 11.0.8+10.
* **An internet connection,** as this project uses Gradle to download a number of dependencies, including those for Kotlin, Spring Boot, and OAuth2.


## Running the project

***HotSauces*** is a demo project and meant to be run on your development machine. Running the project starts up Spring Boot’s local web server, which operates on port 8080 by default. Once the project is running, you can issue calls to the API at **http://localhost:8080/api/hotsauces**. All but one of the API calls requires authentication from Auth0.

To run the project, open a terminal window and navigate to the project’s **app** directory.

Run the following command: `./gradlew bootRun` and watch the output, which will look like the following...

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.3.3.RELEASE)
 
# (A whole lot of console messages will appear here)

> :bootRun
```

If you don’t see an error message and the last line of the output is **`> :bootRun`**, it means that the project is running and you can now start making API calls.


## Making calls to the API

### The public API: `api/hotsauces/test`

The API has one call that *doesn’t* require authentication: **test**. It’s there to show that you can divide an API into calls that are available to the general public and calls that require authentication.

Calling **test** returns a short message that confirms that the API is up and running. You call it making an **HTTP GET** request to **http://localhost:8080/api/hotsauces/test**.

Here’s how you use cURL to call the API and include the HTTP response headers:

```
$ curl -i http://localhost:8080/api/hotsauces/test
```

The response will look like this:

```
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/plain;charset=UTF-8
Content-Length: 14
Date: [ The date and time go here ]

Yup, it works!
```

The [**200** HTTP status code (“OK”)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/200) in the header confirms that the call was successful. This is backed up by the string returned by the API: “Yup, it works!”


### The private API

All the other API calls, which perform the standard CRUD operations, require authentication.

To see what happens when you make a CRUD API call without authentication, enter the following into the terminal:

```
$ curl -i http://localhost:8080/api/hotsauces/
```

The response will look like this:

```
HTTP/1.1 401
Set-Cookie: JSESSIONID=2DC4DE7D0D39042F0E661F78CB4B31CE; Path=/; HttpOnly
WWW-Authenticate: Bearer
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: [ The date and time go here ]
```

It’s all header and no results. The [**401** HTTP status code (“Unauthorized”)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/401) indicates that only authorized users can use this API call.


#### Getting authorized

To use the private API, you need to include the application’s access token in the request header.

You can get this token by issuing the following command:

```
curl --request POST \
  --url https://dev-ne4fe9k3.us.auth0.com/oauth/token \
  --header 'content-type: application/json' \
  --data '{"client_id":"zqucnZrXAcort4iJOTd5HpYZz2cYONwd","client_secret":"z8e5xYUOwXxBnHTz0-HurgZ8ZHTxy2NIbRX5hwsb8J5hn2trshPEw1toyiBzS4B9","audience":"http://hotsauces.demo","grant_type":"client_credentials"}'
```

The response will be:

```
{"access_token":"{ACCESS-TOKEN}":86400,"token_type":"Bearer"}
```

(Note that in the example above, **`{ACCESS-TOKEN}`** is a stand-in for the real access token that the Auth0 system will provide.)

With the access token, you can now issue API calls to the app.


#### Getting the complete list of hot sauces: `GET api/hotsauces`

To get a list of all the hot sauces in the database, issue a GET request to **`api/hotsauces`**, including the access token in the request header.

Here’s how you would make the request with cURL:

```
curl --request GET \
  --url http://localhost:8080/api/hotsauces \
  -H "authorization: Bearer {ACCESS-TOKEN}"
```

(Be sure to replace **`{ACCESS-TOKEN}`** with the actual access token.)

The API response will be a JSON array of dictionaries, with each dictionary representing a hot sauce.


#### Getting a single hot sauce by id: `GET api/hotsauces/{id}`

To get a list of a single hot sauce by id, issue a GET request to **`api/hotsauces/{id}`**, where **`{id}`** is the id of the sauce.

For example, the following cURL retrieves the hot sauce whose `id` is **5**:

```
curl --request GET \
  --url http://localhost:8080/api/hotsauces/5 \
  -H "authorization: Bearer {ACCESS-TOKEN}"
```

The API reponse will be a single JSON dictionary similar to the example below:

```
{"id":5,"brandName":"Hot Ones","sauceName":"Fiery Chipotle","description":"This hot sauce was created with one goal in mind: to get celebrity interviewees on Hot Ones to say  \"damn that's tasty, and DAMN that's HOT!\" and then spill their deepest secrets to host Sean Evans. The tongue tingling flavors of chipotle, pineapple and lime please the palate while the mix of ghost and habanero peppers make this sauce a scorcher. Hot Ones Fiery Chipotle Hot Sauce is a spicy masterpiece.","url":"https://chillychiles.com/products/hot-ones-fiery-chipotle-hot-sauce","heat":15600}
```


#### Getting a count of all the hot sauces: `GET api/hotsauces/count`

To get a count of all the hot sauces in the database, issue a GET request to **`api/hotsauces/count`**.

The example below shows how you would make the request with cURL:

```
curl --request GET \
  --url http://localhost:8080/api/hotsauces/count \
  -H "authorization: Bearer {ACCESS-TOKEN}"
```

The API reponse will be a single number.


#### Adding a new sauce: `POST api/hotsauces`

To add a hot sauce, issue a POST request to **`api/hotsauces/`**, and include the sauce data as a JSON dictionary.

The example below shows the the use of cURL to add a sauce with the following properties:

* `brandName`: **Dave’s Gourmet**
* `sauceName`: **Temporary Insanity**
* `url`: **https://store.davesgourmet.com/ProductDetails.asp?ProductCode=DATE**
* `description`: This sauce has all the flavor of Dave’s Original Insanity with less heat. Finally, there’s sauce for when you only want to get a little crazy in the kitchen. Add to stews, burgers, burritos, and pizza, or any food that needs an insane boost. As with all super hot sauces, this sauce is best enjoyed one drop at a time!
* `heat`: **57000**

```
curl --request POST \
  --url http://localhost:8080/api/hotsauces/ \
  -H "Content-Type: application/json" \
  --data '{"brandName": "Dave’s Gourmet", "sauceName": "Temporary Insanity", "url": "https://store.davesgourmet.com/ProductDetails.asp?ProductCode=DATE", "description": "This sauce has all the flavor of Dave’s Original Insanity with less heat. Finally, there’s sauce for when you only want to get a little crazy in the kitchen. Add to stews, burgers, burritos, and pizza, or any food that needs an insane boost. As with all super hot sauces, this sauce is best enjoyed one drop at a time!", "heat": 57000}' \
  -H "authorization: Bearer {ACCESS-TOKEN}"
```


#### Editing an existing sauce: `PUT api/hotsauces/{id}`

To edit a hot sauce, issue a PUT request to **`api/hotsauces/{id}`**, where **`{id}`** is the `id` of the sauce to be edited, and include the sauce data as a JSON dictionary. Any sauce attributes not included in the dictionary will remain unchanged.

The example below shows the the use of cURL to edit the sauce whose `id` value is **3** so that the following attributes are updated:

* `brandName` is changed to **Tampa Bay Hot Sauce Company**
* `heat` is changed to **2600**

```
curl --request PUT \
  --url http://localhost:8080/api/hotsauces/3 \
  -H "Content-Type: application/json" \
  --data '{"brandName": "Tampa Bay Hot Sauce Company", "heat": 2600}' \
  -H "authorization: Bearer {ACCESS-TOKEN}"
```


#### Deleting an existing sauce: `DELETE api/hotsauces/{id}`

To get a hot sauce, issue a DELETE request to **`api/hotsauces/{id}`**, where **`{id}`** is the `id` of the sauce to be deleted.

The example below shows the use of cURL to delete the sauce whose `id` value is **6**:

```
curl --request DELETE \
  --url http://localhost:8080/api/hotsauces/6 \
  -H "authorization: Bearer {ACCESS-TOKEN}"
```