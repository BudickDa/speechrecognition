package eu.budick;

/**
 * Created by daniel on 20.02.17.
 */
public class Blackboard {
    private boolean listening = false;


    public void toggleMicrophone(){
        this.listening = !this.listening;
    }
    public boolean isListing(){
        return this.listening;
    }


    private static Blackboard ourInstance = new Blackboard();

    public static Blackboard getInstance() {
        return ourInstance;
    }

    private Blackboard() {
    }
}
