// 1. INTERFACES DEFINITION
// ==========================================
interface Playable {
    void play();
    void pause();
}

interface Viewable {
    void zoomIn();
    void zoomOut();
}

// ==========================================
// 2. CONCRETE IMPLEMENTATIONS (CLASSES)
// ==========================================

// Audio Class (Only implements Playable)
class Audio implements Playable {
    private String title;
    private int duration;

    public Audio(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    @Override
    public void play() {
        System.out.println("Playing audio: " + title + " - " + duration + " sec");
    }

    @Override
    public void pause() {
        System.out.println("Audio paused: " + title);
    }
}

// Video Class (Implements both Playable and Viewable)
class Video implements Playable, Viewable {
    private String title;
    private String resolution;

    public Video(String title, String resolution) {
        this.title = title;
        this.resolution = resolution;
    }

    @Override
    public void play() {
        System.out.println("Playing video: " + title + " at " + resolution);
    }

    @Override
    public void pause() {
        System.out.println("Video paused: " + title);
    }

    @Override
    public void zoomIn() {
        System.out.println("Zooming in on video: " + title);
    }

    @Override
    public void zoomOut() {
        System.out.println("Zooming out on video: " + title);
    }
}

// Image Class (Implements both Playable and Viewable)
class Image implements Playable, Viewable {
    private String filename;
    private int sizeKB;

    public Image(String filename, int sizeKB) {
        this.filename = filename;
        this.sizeKB = sizeKB;
    }

    @Override
    public void play() {
        System.out.println("Displaying image: " + filename);
    }

    @Override
    public void pause() {
        System.out.println("Image view paused: " + filename);
    }

    @Override
    public void zoomIn() {
        System.out.println("Zooming in on image: " + filename);
    }

    @Override
    public void zoomOut() {
        System.out.println("Zooming out on image: " + filename);
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS
// ==========================================
/**
 * Note: Kyunki aapka Task03 chal raha hai, 
 * isliye file ka naam 'Task03.java' hona chahiye aur is class ka naam bhi 'Task03'.
 */
public class Task03 {
    public static void main(String[] args) {
        System.out.println("=== Media Player System Simulation ===\n");

        // Reference creation using Polymorphism
        Playable audio = new Audio("Song.mp3", 210);
        Playable video = new Video("Movie.mp4", "1080p");
        Playable image = new Image("Photo.jpg", 2048);

        // 1. Playable Actions Execution
        System.out.println("--- Playable Actions ---");
        audio.play();
        audio.pause();
        
        video.play();
        video.pause();
        
        image.play();
        image.pause();

        // 2. Viewable Actions Execution (Using Interface Casting safely)
        System.out.println("\n--- Viewable Actions ---");
        
        // Video casting to Viewable
        Viewable v = (Viewable) video;
        v.zoomIn();
        v.zoomOut();

        // Image casting to Viewable
        Viewable i = (Viewable) image;
        i.zoomIn();
        i.zoomOut();
    }
}