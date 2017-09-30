package com.fiber.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fiber.rest.executor.FiberRequestExecutor;
import com.fiber.rest.executor.WorkerThreadFactory;
import com.fiber.rest.util.RestHelper;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;

/**
 * Simple Spring MVC integration with Fibers, with the use of Spring MVC Tested
 * with Tomcat 8.0.23/JDK 1.8.0_111 and comsat-tomcat-loader-0.7.0-jdk8.jar in
 * tomcat/lib
 *
 * @author Aditya Lad
 * @since
 */
@RestController("springController")
public class SpringController {

    @Autowired
    RestHelper restHelper;

    /**
     * Simplest suspendable call using Fiber.sleep(). Used to verify that
     * instrumentation happened successfully.
     *
     * @param msg
     * @return
     * @throws InterruptedException
     * @throws SuspendExecution
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rest/{param}", headers = "Accept=text/plain",
            produces = "text/plain")
    @ResponseBody
    @Suspendable
    public String sayHello(@PathVariable("param") String msg) throws InterruptedException, SuspendExecution {
        Fiber.sleep(10);
        String output = "Spring say : " + msg;
        return output;
    }

    /**
     * Calls a Fiber executor using ThreadPoolExecutor that has min and max pool
     * size and fiber worker pool name. Also a specified timeout for the request
     * that can be given through future.get()
     *
     * @param msg
     * @return
     * @throws InterruptedException
     * @throws SuspendExecution
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rest/threaded/{param}", headers = "Accept=text/plain",
            produces = "text/plain")
    @ResponseBody
    @Suspendable
    public String runInSuspendableCallable(@PathParam("param") String msg)
            throws InterruptedException, SuspendExecution, ExecutionException, TimeoutException {
        String output = "Spring say : ";
        ExecutorService executor = new ThreadPoolExecutor(4,
                10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10, true),
                new WorkerThreadFactory("spring-fiber-pool"), new ThreadPoolExecutor.AbortPolicy());
        FiberScheduler fiberScheduler = new FiberExecutorScheduler("spring-fiber", executor);
        List<FiberRequestExecutor> commandList = new ArrayList<FiberRequestExecutor>();
        FiberRequestExecutor requestCommand = new FiberRequestExecutor();
        commandList.add(requestCommand);
        Future<Object> future = new Fiber<Object>(fiberScheduler, requestCommand).start();
        String result = (String) future.get(6000L, TimeUnit.MILLISECONDS);
        return output + result;
    }

    /**
     * Calls Http POST request on a site using Comsat Quasar http Fiber client
     *
     * @param msg
     * @return
     * @throws InterruptedException
     * @throws SuspendExecution
     * @throws ExecutionException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rest/posthttp/{param}", headers = "Accept=text/plain",
            produces = "text/plain")
    @ResponseBody
    @Suspendable
    public String callComsatHttpPost(@PathParam("param") String msg)
            throws InterruptedException, SuspendExecution, ExecutionException {
        String output = "Spring say : ";
        String result = restHelper.callPost();
        return output + result;
    }

    /**
     * Calls Http GET request on stackoverflow using Comsat Quasar http Fiber
     * client
     *
     * @param msg
     * @return
     * @throws InterruptedException
     * @throws SuspendExecution
     * @throws ExecutionException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rest/gethttp/{param}", headers = "Accept=text/plain",
            produces = "text/plain")
    @ResponseBody
    @Suspendable
    public String callComsatHttpGet(@PathParam("param") String msg)
            throws InterruptedException, SuspendExecution, ExecutionException {
        String output = "Spring say : ";
        String result = restHelper.callGet();
        return output + result;
    }


}
