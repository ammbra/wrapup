# Wrapup

This application runs with Java 21. 
To start the application use the following command:

```shell
export PATH_TO_JSON=${HOME}/.m2/repository/org/json/json/20231013
        
java -classpath target/classes:$PATH_TO_JSON/json-20231013.jar \
        --enable-preview --source 21 \
        src/main/java/org/ammbra/advent/Wrapup.java
```

Now you can try some sample requests:
```shell
curl -X POST http://127.0.0.1:8081 -H 'Content-Type: application/json' -d '{"receiver":"Duke","sender":"Ana","celebration":"CURRENT_YEAR", "option":"NONE"}'

curl -X POST http://127.0.0.1:8081 -H 'Content-Type: application/json' -d '{"receiver":"Duke","sender":"Ana","celebration":"CURRENT_YEAR", "option":"COUPON", "itemPrice": "24.2"}'

curl -X POST http://127.0.0.1:8081 -H 'Content-Type: application/json' -d '{"receiver":"Duke","sender":"Ana","celebration":"BIRTHDAY", "option":"PRESENT", "itemPrice": "27.8", "boxPrice": "2.0"}'

curl -X POST http://127.0.0.1:8081 -H 'Content-Type: application/json' -d '{"receiver":"Duke","sender":"Ana","celebration":"NEW_YEAR", "option":"EXPERIENCE", "itemPrice": "47.5"}'
```