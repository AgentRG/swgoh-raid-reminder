package utilities;

public class Functions {

    public Functions sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }
}
