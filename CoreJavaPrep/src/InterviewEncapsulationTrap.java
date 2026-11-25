// File: InterviewEncapsulationTrap.java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
class FrameworkSession {
    // Read-only field: has a getter, NO setter
    private final String sessionId;

    // Mutable internal collection: even though 'private', returning it directly breaks encapsulation!
    private List<String> browserFlags;

    public FrameworkSession(String sessionId, List<String> initialFlags) {
        this.sessionId = sessionId;
        // Defensive copy on input so caller's outside list cannot mutate our internal state
        this.browserFlags = new ArrayList<>(initialFlags);
    }

    public String getSessionId() {
        return sessionId;
    }

    // TRAP A (Leaky Getter): Exposes the raw internal Heap reference of browserFlags!
    public List<String> getLeakyBrowserFlags() {
        return browserFlags;
    }

    // FIX (True Encapsulation): Returns an unmodifiable view / defensive copy
    public List<String> getSafeBrowserFlags() {
        return Collections.unmodifiableList(browserFlags);
    }
}

public class InterviewEncapsulationTrap {
    public static void main(String[] args) {
        List<String> startupFlags = new ArrayList<>();
        startupFlags.add("--headless=new");
        startupFlags.add("--window-size=1920,1080");

        // 1. Create Object A in Heap; 'session1' points to it
        FrameworkSession session1 = new FrameworkSession("SESS-101", startupFlags);

        // Mutating the original list after constructor runs:
        startupFlags.add("--disable-gpu");
        System.out.println("1. After mutating startupFlags: " + session1.getSafeBrowserFlags());

        // 2. Reference Aliasing Trap: 'session2' does NOT create a new object!
        // Both session1 and session2 on the Stack point to the EXACT SAME Heap object.
        FrameworkSession session2 = session1;

        // 3. Breaking Encapsulation via Leaky Getter (no setter was ever called!)
        session2.getLeakyBrowserFlags().clear();
        System.out.println("2. session1 flags after session2 cleared leaky list: " + session1.getSafeBrowserFlags());

        // 4. Null Reference Trap
        session2 = null; // Disconnects session2 from Heap; session1 STILL points to the Heap object
        System.out.println("3. session1 ID still alive: " + session1.getSessionId());
        // Calling session2.getSessionId() here would throw NullPointerException!
    }
}