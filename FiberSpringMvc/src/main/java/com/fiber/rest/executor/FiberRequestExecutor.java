package com.fiber.rest.executor;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.SuspendableCallable;

public class FiberRequestExecutor implements SuspendableCallable<Object> {

    /**
     *
     */
    private static final long serialVersionUID = -4491542148479962865L;

    @Suspendable
    public Object run() throws SuspendExecution, InterruptedException {
        Fiber.sleep(10);
        return "Hello World Fibers";
    }

}
