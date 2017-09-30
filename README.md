# Fibers

/FiberJerseySpring
 * Simple Jersey integration with Fibers, with the use of Spring (Uses
 * Jersey-Spring3) Tested with Tomcat 8.0.23/JDK 1.8.0_111 and
 * comsat-tomcat-loader-0.7.0-jdk8.jar in tomcat/lib
 
 Threaded example (Runs a command in SuspendableCallable with the use of ThreadPoolExecutor)
 http://localhost:8080/FiberJerseySpring/rest/threaded/hello
 Jersey say : Hello World Fibers
 
 http://localhost:8080/FiberJerseySpring/rest/gethttp/hello (Calls HTTP GET in Comsat Fiber http client)
 Response:
 Jersey say : (Stackoverflow robots.txt content - )
 
 http://localhost:8080/FiberJerseySpring/rest/posthttp/hello (Calls HTTP POST in Comsat Fiber http client)
 Response: 
 Jersey say : Successfully dumped 0 post variables. View it at http://www.posttestserver.com/data/2017/09/30/05.04.36166946119 No Post body.
 
 
 /FiberSpringMvc
 * Simple Spring MVC integration with Fibers, with the use of Spring MVC Tested with
 * Tomcat 8.0.23/JDK 1.8.0_111 and comsat-tomcat-loader-0.7.0-jdk8.jar in
 * tomcat/lib
 http://localhost:8080/FiberSpringMvc/rest/hello
 Spring say : hello
 
 http://localhost:8080/FiberSpringMvc/rest/gethttp/hello
 Spring say : User-Agent: *
Disallow: /posts/
Disallow: /posts?
..

http://localhost:8080/FiberSpringMvc/rest/posthttp/hello
Spring say : Successfully dumped 0 post variables.
View it at http://www.posttestserver.com/data/2017/09/30/11.04.311112042101
No Post body.
 
 
