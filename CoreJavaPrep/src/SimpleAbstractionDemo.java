// File: SimpleAbstractionDemo.java

// 1. INTERFACE: Defines a contract ("what" a browser capability must do)
interface ScreenshotCapable {
    // Implicitly public static final constant
    String IMAGE_FORMAT = "PNG";

    // Abstract method (no body — implementing class MUST write it)
    void captureScreenshot(String fileName);

    // Java 8+ default method (provides ready-to-use fallback behavior)
    default void logCaptureInfo() {
        System.out.println("[Interface Default] Saving screenshot in " + IMAGE_FORMAT + " format.");
    }

    // Java 8+ static method (called via Interface name)
    static void printCapabilityName() {
        System.out.println("=== Screenshot Capability Enabled ===");
    }
}

// 2. ABSTRACT CLASS: Holds shared state, constructor, concrete helpers + abstract rules
abstract class BaseBrowser {
    protected String browserName;

    // Abstract classes CAN have constructors (called by child class via super())
    public BaseBrowser(String browserName) {
        this.browserName = browserName;
        System.out.println("[BaseBrowser Constructor] Setting up driver for: " + browserName);
    }

    // Concrete method: shared by all child browsers
    public void maximizeWindow() {
        System.out.println("[" + browserName + "] Maximizing browser window to 1920x1080.");
    }

    // Abstract method: forces every child browser to define its own launch logic
    public abstract void launchUrl(String url);
}

// 3. CONCRETE CLASS: Extends 1 Abstract Class AND Implements an Interface
class ChromeBrowser extends BaseBrowser implements ScreenshotCapable {

    public ChromeBrowser() {
        super("Google Chrome"); // Invokes BaseBrowser's constructor
    }

    @Override
    public void launchUrl(String url) {
        System.out.println("[" + browserName + "] Navigating to: " + url);
    }

    @Override
    public void captureScreenshot(String fileName) {
        logCaptureInfo(); // Calling default method inherited from interface
        System.out.println("[" + browserName + "] Saved screenshot: " + fileName + "." + IMAGE_FORMAT);
    }
}

public class SimpleAbstractionDemo {
    public static void main(String[] args) {
        // BaseBrowser b = new BaseBrowser("Chrome"); // COMPILE ERROR: Cannot instantiate abstract class!

        ScreenshotCapable.printCapabilityName(); // Calling static method on Interface

        // Upcasting to Abstract Class reference
        BaseBrowser browser = new ChromeBrowser();
        browser.maximizeWindow();
        browser.launchUrl("https://qa-portal.example.com");

        // Accessing Interface capability
        ((ScreenshotCapable) browser).captureScreenshot("login_page");
    }
}