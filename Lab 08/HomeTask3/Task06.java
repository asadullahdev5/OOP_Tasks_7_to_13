// ==========================================
// 1. INTERFACE DEFINITION
// ==========================================
interface EventListener {
    void onClick(String eventSource);
}

// ==========================================
// 2. CONCRETE IMPLEMENTATIONS (CLASSES)
// ==========================================

// UserAction Class (Implements EventListener)
class UserAction implements EventListener {
    @Override
    public void onClick(String eventSource) {
        System.out.println("Button '" + eventSource + "' was clicked");
    }
}

// Button Class (Triggers the event callback)
class Button {
    private String name;
    private EventListener listener;
     
    public Button(String name) {
        this.name = name;
    }
     
    public void setEventListener(EventListener listener) {
        this.listener = listener;
    }
     
    public void click() {
        if (listener != null) {
            listener.onClick(name); // Callback mechanism execute ho raha hai
        }
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS
// ==========================================
/**
 * Note: Kyunki yeh Task06 chal raha hai, 
 * isliye public class aur file ka naam 'Task06' hona chahiye.
 */
public class Task06 {
    public static void main(String[] args) {
        System.out.println("=== Event Callback System Simulation ===\n");

        // 1. Button aur Action ke objects banana
        Button button = new Button("Submit");
        UserAction action = new UserAction();
         
        // 2. Listener attach karna (Callback register karna)
        button.setEventListener(action);
        
        // 3. Click event trigger karna
        System.out.println("[SIMULATION] User is clicking the button...");
        button.click();
    }
}