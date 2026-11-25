// File: SimpleEncapsulationDemo.java
class TestEnvironment {
    // 1. ENCAPSULATION: Variables are 'private' so outside classes cannot corrupt them directly
    private String envName;
    private int timeoutSeconds;

    // 2. Public Setter with validation logic (the real value of encapsulation)
    public void setEnvName(String envName) {
        if (envName == null || envName.trim().isEmpty()) {
            System.out.println("[Validation Blocked] Environment name cannot be blank!");
        } else {
            this.envName = envName.toUpperCase();
        }
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        if (timeoutSeconds <= 0) {
            System.out.println("[Validation Blocked] Timeout must be > 0. Defaulting to 10s.");
            this.timeoutSeconds = 10;
        } else {
            this.timeoutSeconds = timeoutSeconds;
        }
    }

    // 3. Public Getters to read the private state safely
    public String getEnvName() {
        return envName;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    // 4. Behavior method using the encapsulated state
    public void printSummary() {
        System.out.println("Running on [" + envName + "] with explicit wait timeout: " + timeoutSeconds + "s");
    }
}

public class SimpleEncapsulationDemo {
    public static void main(String[] args) {
        // Creating Object #1 in Heap; 'qaEnv' reference sits on Stack
        TestEnvironment qaEnv = new TestEnvironment();
        qaEnv.setEnvName("qa");
        qaEnv.setTimeoutSeconds(15);

        // Creating Object #2 in Heap (completely separate memory from qaEnv)
        TestEnvironment stagingEnv = new TestEnvironment();
        stagingEnv.setEnvName("staging");
        stagingEnv.setTimeoutSeconds(-5); // Triggers validation guard

        // qaEnv.timeoutSeconds = -999; // COMPILER ERROR: timeoutSeconds has private access!

        qaEnv.printSummary();      // Output: Running on [QA] with explicit wait timeout: 15s
        stagingEnv.printSummary(); // Output: Running on [STAGING] with explicit wait timeout: 10s
    }
}