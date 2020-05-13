package com.shubhambansal.solution.countingSet;

import com.shubhambansal.solution.constants.Operation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.MockitoAnnotations.initMocks;

/*
 * @author Shubham Bansal
 */
public class CountingSetImplTest {
    private static final String KEY = "a";

    CountingSetImpl countingSet;

    private int threadCount = 0;

    @Before
    public void init() {
        countingSet = new CountingSetImpl(true);
        initMocks(this);
    }

    @Test
    public void testAddInCountingSetImpl_whenSingleThread_shouldSucceed() {
        int add = countingSet.add(KEY);
        Assert.assertEquals(add, 1);
    }

    @Test
    public void testAddInCountingSetImpl_whenMultiThreaded_shouldSucceed() throws InterruptedException {
        //Action
        Thread t1 = createNewThreadWithOperation(10000, Operation.ADD);
        Thread t2 = createNewThreadWithOperation(10000, Operation.ADD);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Assert.assertEquals(countingSet.count(KEY), 20000);
    }

    @Test
    public void testRemoveInCountingSetImpl_whenSingleThread_shouldSucceed() {
        //Setup
        countingSet.add(KEY);
        countingSet.add(KEY);
        //Action
        int remove = countingSet.remove(KEY);
        Assert.assertEquals(remove, 1);
    }

    @Test
    public void testRemoveInCountingSetImpl_whenMultiThread_shouldSucceed() throws InterruptedException {
        //Setup
        Thread t0 = createNewThreadWithOperation(20000, Operation.ADD);
        t0.start();
        t0.join();

        //Action
        Thread t1 = createNewThreadWithOperation(10000, Operation.REMOVE);
        Thread t2 = createNewThreadWithOperation(10002, Operation.REMOVE);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Assert.assertEquals(countingSet.count(KEY), 0);
    }

    @Test
    public void testRemoveInCountingSetImpl_whenSingleThread_shouldFail() {
        int remove = countingSet.remove(KEY);
        Assert.assertEquals(remove, 0);
    }

    @Test
    public void count() {

    }

    private Thread createNewThreadWithOperation(int cycles, Operation operation) {
        Thread thread = new Thread("thread-" + threadCount++) {
            @Override
            public void run() {
                for (int i = 0; i < cycles; i++) {
                    if (Operation.ADD == operation)
                        countingSet.add(KEY);
                    else
                        countingSet.remove(KEY);
                }
            }
        };
        return thread;
    }
}