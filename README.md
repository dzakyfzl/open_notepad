# Open Notepad
This program implements Object-Ortiented Programming architecture for the backend, and now its still on testing

## Specification
This Program uses :
- Spring Boot Framework
- Thymeleaf
- ZeroSSL Certificate
- Microsoft SQL database

## Preperation
Before run or build the program, make sure to do :
- empty the `\uploads\pdf` and `\uploads\photos` for safety purpose
- ask  to get the `keystore.p12` or do the self-assign with openssl
- put the `keystore.p12` file to the `\src\main\resources`
- make sure your `.env` are same as `example.env` (DO NOT CHANGE example.env FILE)

## How to Run
for devs, use this steps :
- in terminal run the command `.\mvnw spring-boot:run`
- in local server, it will use port `443` or HTTPS
- go to the browser and do it here [https://localhost](https://localhost)
- it will be told that was un-safe brcause of the certificate are assigned for website

