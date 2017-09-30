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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiber.rest.executor.FiberRequestExecutor;
import com.fiber.rest.executor.WorkerThreadFactory;
import com.fiber.rest.util.RestHelper;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;

/**
 * Simple Jersey integration with Fibers, with the use of Spring (Uses
 * Jersey-Spring3) Tested with Tomcat 8.0.23/JDK 1.8.0_111 and
 * comsat-tomcat-loader-0.7.0-jdk8.jar in tomcat/lib
 *
 * @author Aditya Lad
 * @since
 */
@Path("/")
@Service("fiberController")
public class JerseyController {

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
    @GET
    @Path("/{param}")
    @Suspendable
    public Response sayHello(@PathParam("param") String msg) throws InterruptedException, SuspendExecution {
        Fiber.sleep(10);
        String output = "Jersey say : " + msg;
        return Response.status(200).entity(output).build();
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
    @GET
    @Path("/threaded/{param}")
    @Suspendable
    public Response runInSuspendableCallable(@PathParam("param") String msg)
            throws InterruptedException, SuspendExecution, ExecutionException, TimeoutException {
        String output = "Jersey say : ";
        ExecutorService executor = new ThreadPoolExecutor(4,
                10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10, true),
                new WorkerThreadFactory("jersey-fiber-pool"), new ThreadPoolExecutor.AbortPolicy());
        FiberScheduler fiberScheduler = new FiberExecutorScheduler("jersey-fiber", executor);
        List<FiberRequestExecutor> commandList = new ArrayList<FiberRequestExecutor>();
        FiberRequestExecutor requestCommand = new FiberRequestExecutor();
        commandList.add(requestCommand);
        Future<Object> future = new Fiber<Object>(fiberScheduler, requestCommand).start();
        String result = (String) future.get(6000L, TimeUnit.MILLISECONDS);
        return Response.status(200).entity(output + result).build();
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
    @GET
    @Path("/posthttp/{param}")
    @Suspendable
    public Response callComsatHttpPost(@PathParam("param") String msg)
            throws InterruptedException, SuspendExecution, ExecutionException {
        String output = "Jersey say : ";
        String result = restHelper.callPost();
        return Response.status(200).entity(output + result).build();
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
    @GET
    @Path("/gethttp/{param}")
    @Suspendable
    public Response callComsatHttpGet(@PathParam("param") String msg)
            throws InterruptedException, SuspendExecution, ExecutionException {
        String output = "Jersey say : ";
        String result = restHelper.callGet();
        return Response.status(200).entity(output + result).build();
    }


}
