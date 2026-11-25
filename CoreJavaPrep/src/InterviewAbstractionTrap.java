// File: InterviewAbstractionTrap.java

// Interface A with a Java 8 default method
interface UiLogger {
    default void logStep(String step) {
        System.out.println("[UI-LOG] " + step);
    }
}

// Interface B with the EXACT SAME default method signature!
interface ApiLogger {
    default void logStep(String step) {
        System.out.println("[API-LOG] " + step);
    }
}

// Abstract parent class representing a framework BasePage
abstract class AbstractBasePage {
    protected String pageStatus = "INIT";

    public AbstractBasePage() {
        System.out.println("1. AbstractBasePage constructor starts.");
        // TRAP: Calling an overridden method inside a parent constructor!
        verifyPageReady();
        System.out.println("3. AbstractBasePage constructor ends.");
    }

    // Subclasses MUST implement this
    public abstract void verifyPageReady();
}

// Concrete Page class extending AbstractBasePage AND implementing BOTH conflicting interfaces
class LoginPage extends AbstractBasePage implements UiLogger, ApiLogger {
    // Child class instance variable initialized AFTER super() constructor finishes!
    private String expectedHeader = "Welcome Back";

    public LoginPage() {
        super(); // Runs AbstractBasePage() FIRST, before expectedHeader = "Welcome Back" executes!
        System.out.println("4. LoginPage constructor finishes. expectedHeader is now: " + expectedHeader);
    }

    @Override
    public void verifyPageReady() {
        // When called from AbstractBasePage(), expectedHeader has NOT been assigned yet!
        System.out.println("2. verifyPageReady() running -> expectedHeader = " + expectedHeader);
    }

    // DIAMOND CONFLICT RESOLUTION:
    // Because BOTH UiLogger and ApiLogger define default logStep(String), Java forces LoginPage
    // to override logStep() and explicitly choose which parent implementation to call!
    @Override
    public void logStep(String step) {
        UiLogger.super.logStep(step);  // Explicitly invokes UiLogger's default method
        ApiLogger.super.logStep(step); // Explicitly invokes ApiLogger's default method
    }
}

public class InterviewAbstractionTrap {
    public static void main(String[] args) {
        System.out.println("--- Instantiating LoginPage ---");
        AbstractBasePage page = new LoginPage();

        System.out.println("\n--- Calling verifyPageReady() AFTER construction ---");
        page.verifyPageReady();

        System.out.println("\n--- Resolving Multiple Interface Default Methods ---");
        ((UiLogger) page).logStep("Clicked Sign In button");
    }
}