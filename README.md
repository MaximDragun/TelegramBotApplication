<h1 align="center">TelegramBotApplication</h1>
<h3 align="center">Developed telegram bot that can be a file hosting, adviser of movies and cats.
<a href="https://t.me/my_favorite_smart_bot" target="_blank" rel="noreferrer"> Click to try the bot </a></h3>
<br> 

<h3 align="center">Languages and Tools used in the project:</h3>
<p align="center">
  <a href="https://www.java.com" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="75" height="75"/> </a>
   <a href="https://spring.io/" target="_blank" rel="noreferrer"> 
    <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="75" height="75"/> </a>
  <a href="https://www.w3schools.com/css/" target="_blank" rel="noreferrer"> 
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/css3/css3-original-wordmark.svg" alt="css3" width="75" height="75"/>
   <a href="https://flywaydb.org/" target="_blank" rel="noreferrer"> 
  <img src="https://seeklogo.com/images/F/flyway-logo-4BD34A6273-seeklogo.com.png" alt="flyway" width="75" height="75"/> 
  </a> <a href="https://www.docker.com/" target="_blank" rel="noreferrer">
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original-wordmark.svg" alt="docker" width="75" height="75"/> </a>
  <a href="https://git-scm.com/" target="_blank" rel="noreferrer"> 
  <img src="https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg" alt="git" width="75" height="75"/> </a>
  <a href="https://www.w3.org/html/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/html5/html5-original-wordmark.svg" alt="html5" width="75" height="75"/>  </a>
  <a href="https://www.linux.org/" target="_blank" rel="noreferrer"> 
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/linux/linux-original.svg" alt="linux" width="75" height="75"/> </a> 
  <a href="https://www.postgresql.org" target="_blank" rel="noreferrer"> 
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-original-wordmark.svg" alt="postgresql" width="75" height="75"/> </a> 
  <a href="https://www.rabbitmq.com" target="_blank" rel="noreferrer">
    <img src="https://www.vectorlogo.zone/logos/rabbitmq/rabbitmq-icon.svg" alt="rabbitMQ" width="75" height="75"/> </a>
  </p><br>
  
 <h3 align="left"> <a>  You can use the bot as a file hosting service, the currently supported file upload type is photos and documents. Upload them to the bot and he will give you a link to download these files, the link will be available around the clock. To use a file hosting service, you need to register (verify your mail) and then use it without any restrictions. You can also change the mail, or send a message to it again.</a>
<a>You can also get a random movie or series from imdb, for this the bot uses the imdb api and a photo or gif of a random cat for this the bot uses the cat api.</a><br><br>
<a>Microservice architecture is used. There are 4 services in total:</a><br>
<a>1) Dispatcher service. Receives requests from telegram chat from users by telegram api, sorts them and sends them to the RabbitMQ message broker to the appropriate queue, after executing the main processing logic in the Main service, receives a response from it by listening to a specific queue and sends a response via telegram api to the user.</a><br>
<a>2) Main service. It contains all the logic of the application. All user messages are processed here and, depending on the content of the message, it generates a response to the user or sends a request to the rest service or mail service, and then, based on the received response, processes it and sends it to the message broker, from where it gets to the Dispatcher.</a><br>
<a>3) Rest service. Based on the request logic, it forms its own request and accesses the database or third-party api services to receive data, after which it sends the response along with the data to the Main service.</a><br>
<a>4) Mail service. It is used to send letters to the mail when registering a user or changing mail.</a> </h3>
   <h3 align="left">
  <a>Java - the application is written in Java using many frameworks of this language.</a><br>
<a>Spring is the main framework used to create the microservice architecture of the application, Spring Boot, Spring Data JPA and Spring Rest were also used.</a><br>
<a>CSS3 - to set the styles of pages sent to the user after registering or changing mail.</a><br>
<a>Flyway - used for easy migration of the used database.</a><br>
<a>Docker and Docker-Compose were used to test and deploy RabbitMQ and PostgreSQL containers locally and on a remote server to run the application.</a><br>
<a>Git - to apply version control of the application.</a><br>
<a>HTML5 - to create the structure of the sent page after registering or changing the mail and when sending a letter to the mail for registration.</a><br>
<a>Linux - for configuring the application on a remote VDS server on Ubuntu 22.</a><br>
<a>PostgreSQL - selected as the DBMS for the application, all data used by the application is stored in it.</a><br>
<a>RabbitMQ is a message broker used to send user messages and application-generated responses.</a><br>
  </h3>
