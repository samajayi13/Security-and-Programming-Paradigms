import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // called when application is opened
        deleteFileContents();

    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // called when application is closed
        deleteFileContents();

    }

    /**
     * Method deletes file contents of dir encryptedfiles and finally deletes this directory
     */
    private void deleteFileContents() {
        File dir = new File("encryptedfiles");
        if(dir.exists()) {
            for (File file : dir.listFiles())
                if (!file.isDirectory())
                    file.delete();

            dir.delete();
        }
    }
}