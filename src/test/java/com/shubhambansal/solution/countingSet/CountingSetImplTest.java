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
        Assert.assertEquals(1, add);
    }

    // This test case is written to specifically test the get thread unsafe operation
    // Uncomment the code in IMPL add method to test the functionality
    @Test
    public void testAddInCountingImpl_whenThreaded_betweenOperation_shouldSucceed() throws InterruptedException {
        // This is just a hack to fetch values from the thread, alternative is to implement callable
        final int[] count1 = {0};
        final int[] count2 = {0};
        Thread t1 = new Thread("thread-" + 1) {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
                    count1[0] += countingSet.add(KEY);
                }
            }
        };

        Thread t2 = new Thread("thread-" + 2) {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
                    count2[0] += countingSet.add(KEY);
                }
            }
        };

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //Either thread1 can run first or 2, so one of them will get 1 and the other 2, or vice-versa.
        Assert.assertTrue((count1[0] == 1 && count2[0] == 2) || (count1[0] == 2 && count2[0] == 1));
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

        Assert.assertEquals(20000, countingSet.count(KEY));
    }

    @Test
    public void testRemoveInCountingSetImpl_whenSingleThread_shouldSucceed() {
        //Setup
        countingSet.add(KEY);
        countingSet.add(KEY);
        //Action
        int remove = countingSet.remove(KEY);
        Assert.assertEquals(1, remove);
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

        Assert.assertEquals(0, countingSet.count(KEY));
    }

    @Test
    public void testRemoveInCountingSetImpl_whenSingleThread_shouldFail() {
        int remove = countingSet.remove(KEY);
        Assert.assertEquals(0, remove);
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