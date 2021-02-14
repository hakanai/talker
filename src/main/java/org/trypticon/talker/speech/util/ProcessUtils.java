package org.trypticon.talker.speech.util;

import java.io.IOException;
import java.util.Arrays;

/**
 * Utilities for running processes.
 */
public class ProcessUtils {
    private ProcessUtils() {
    }

    public static void execAndWait(String... arguments) {
        try {
            Process process = new ProcessBuilder(arguments).start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Execution returned non-successful exit code: " + exitCode);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't launch speech program: " + Arrays.toString(arguments), e);
        } catch (InterruptedException e) {
            // No problem.
            Thread.currentThread().interrupt();
        }
    }
}
