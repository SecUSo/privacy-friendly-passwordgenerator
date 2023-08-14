package org.secuso.privacyfriendlypasswordgenerator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGeneratorTask;

import java.util.concurrent.CountDownLatch;

public class PasswordGeneratorTaskTest {
    @Test
    public void testPasswordGeneratorTaskWithRandomValues() throws InterruptedException {
        int numTests = 10;
        final CountDownLatch signal = new CountDownLatch(numTests);
        for (int i = 0; i < numTests; i++) {
            PasswordGenerator generator = new PasswordGenerator(
                    "test1", "test2", Integer.toString(i), "test4", 5, 1000, "SHA256", "10"
            );
            String expected = generator.getPassword(1, 1, 1, 1, 16);
            new PasswordGeneratorTask() {
                @Override
                protected void onPostExecute(String s) {
                    assertEquals(expected, s);
                    signal.countDown();
                }
            }.execute(new PasswordGeneratorTask.PasswordGeneratorParameter(
                    "test1", "test2", Integer.toString(i), "test4", 5, 1000, "SHA256", "10",
                    1, 1, 1, 1, 16
            ));
        }
        signal.await();
    }
}
