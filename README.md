## INSTRUCTION

start up application and use this link for test functions: <br>
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/

### endpoints

* add new coffee machine with specified model: <br>
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/coffee-machine-controller/add

* after all machines has been added you can check list of all them: <br>
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/coffee-machine-controller/getAll

* first you should turn on those machines you will use further:<br>
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/coffee-machine-controller/turnStatus
by transmitting COFFEE_MACHINE_ID and TURNED_ON as parameters

* then you can choose what type of coffee toy want coffee machine made for you. There are 3 types 
LATTE, CAPPUCCINO, AMERICANO and make server do this command:
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/coffee-machine-controller/turnStatus 
by transmitting COFFEE_MACHINE_ID and [MAKES_LATTE | MAKES_CAPPUCCINO | MAKES_AMERICANO] as parameters

* after coffee machine has made coffee it automatically goes to WAINING status and if 
it has been broken down it automatically goes to BROKEN status

* you can check status of specified coffee machine by
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/coffee-machine-controller/getCoffeeMachine

* after you complete your work you can turned off all coffee machine by
  http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/coffee-machine-controller/turnedOffAll