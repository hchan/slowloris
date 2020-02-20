# Slowloris Attack

This app takes in a USERNAME and PASSWORD as arguments
and performs a Slowloris attack (https://en.wikipedia.org/wiki/Slowloris_(computer_security))
by sending a partial POST (Header + \r\n) and delays a random number of milliseconds before sending
a body.  It is the delay between the Header and Body that causes the server to queue requests

Example:
```
POST http://localhost:8080/gwtRequest HTTP/1.1
Cookie: JSESSIONID=OZEl9pN4QscEuUzH4IqM_9S5ADQO0xf8UV7e-02O.c02yr22slvcg\
Host: localhost:8080
accept: */*
content-type: application/json; charset=UTF-8
content-length: 375
```

<body>