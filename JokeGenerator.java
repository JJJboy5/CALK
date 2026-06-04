import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;

public class JokeGenerator extends MIDlet {
    private Display display;
    private JokeGeneratorForm form;

    public JokeGenerator() {
        super();
    }

    public void startApp() {
        display = Display.getDisplay(this);
        form = new JokeGeneratorForm(this);
        display.setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void quit() {
        destroyApp(true);
        notifyDestroyed();
    }
}

class JokeGeneratorForm extends Form implements CommandListener, Runnable {
    private JokeGenerator midlet;
    private Command getJokeCmd;
    private Command exitCmd;
    private StringItem jokeDisplay;
    private Gauge loadingGauge;
    private Thread fetchThread;
    private static final String API_URL = "https://official-joke-api.appspot.com/random_joke";

    public JokeGeneratorForm(JokeGenerator midlet) {
        super("Joke Generator");
        this.midlet = midlet;

        jokeDisplay = new StringItem(null, "Press 'Get Joke' to fetch a random joke!");
        append(jokeDisplay);

        loadingGauge = new Gauge("Loading...", false, Gauge.INDEFINITE, Gauge.CONTINUOUS);
        loadingGauge.setValue(0);

        getJokeCmd = new Command("Get Joke", Command.OK, 1);
        exitCmd = new Command("Exit", Command.EXIT, 2);

        addCommand(getJokeCmd);
        addCommand(exitCmd);
        setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable disp) {
        if (cmd == getJokeCmd) {
            if (fetchThread == null || !fetchThread.isAlive()) {
                append(loadingGauge);
                fetchThread = new Thread(this);
                fetchThread.start();
            }
        } else if (cmd == exitCmd) {
            midlet.quit();
        }
    }

    public void run() {
        try {
            String joke = fetchJoke();
            deleteAll();
            if (joke != null) {
                jokeDisplay.setText(joke);
            } else {
                jokeDisplay.setText("Failed to fetch joke. Try again!");
            }
            append(jokeDisplay);
            addCommand(getJokeCmd);
            addCommand(exitCmd);
        } catch (Exception e) {
            deleteAll();
            jokeDisplay.setText("Error: " + e.getMessage());
            append(jokeDisplay);
            addCommand(getJokeCmd);
            addCommand(exitCmd);
        }
    }

    private String fetchJoke() {
        HttpConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpConnection) Connector.open(API_URL);
            conn.setRequestMethod(HttpConnection.GET);
            conn.setRequestProperty("User-Agent", "MIDlet");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpConnection.HTTP_OK) {
                is = conn.openInputStream();
                String response = readInputStream(is);
                return parseJoke(response);
            } else {
                return "HTTP Error: " + responseCode;
            }
        } catch (Exception e) {
            return "Connection Error: " + e.getMessage();
        } finally {
            try {
                if (is != null) is.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
            }
        }
    }

    private String readInputStream(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[256];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, bytesRead));
        }
        return sb.toString();
    }

    private String parseJoke(String jsonResponse) {
        try {
            String setup = extractValue(jsonResponse, "setup");
            String punchline = extractValue(jsonResponse, "punchline");
            if (setup != null && punchline != null) {
                return setup + "\n\n" + punchline;
            }
            return "Could not parse joke";
        } catch (Exception e) {
            return "Parse Error: " + e.getMessage();
        }
    }

    private String extractValue(String json, String key) {
        String searchStr = "\"" + key + "\":\"";
        int startIdx = json.indexOf(searchStr);
        if (startIdx != -1) {
            startIdx += searchStr.length();
            int endIdx = json.indexOf("\"", startIdx);
            if (endIdx != -1) {
                return json.substring(startIdx, endIdx);
            }
        }
        return null;
    }
}
