package talker.speech;

import talker.config.Configuration;
import talker.text.Text;
import talker.text.substituter.Substituter;
import talker.text.substituter.SubstituterFactory;
import talker.text.tokenisation.KuromojiTokeniser;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Queue to put text on to get it spoken.
 */
public class SpeechQueue {
    private final KuromojiTokeniser tokeniser;
    private final Substituter substituter;
    private final Speaker speaker;
    private final Queue<String> textQueue = new ConcurrentLinkedQueue<>();
    private volatile Thread thread;

    public SpeechQueue(Configuration configuration) {
        tokeniser = new KuromojiTokeniser();
        substituter = new SubstituterFactory().create(configuration);
        speaker = new SpeakerFactory().create(configuration);
    }

    public void post(String text) {
        textQueue.add(text);
        synchronized (textQueue) {
            textQueue.notifyAll();
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(new Consumer(), "SpeechQueue");
            thread.start();
        }
    }

    public void stop() {
        Thread thread = this.thread;
        if (thread != null) {
            this.thread = null;
            thread.interrupt();
        }
    }

    private class Consumer implements Runnable {
        @Override
        public void run() {
            while (thread != null) {
                String text;
                while ((text = textQueue.poll()) != null) {
                    Text tokenisedText = tokeniser.tokenise(text);
                    tokenisedText = substituter.substitute(tokenisedText);
                    speaker.speak(tokenisedText);
                }

                synchronized (textQueue) {
                    try {
                        textQueue.wait(100);
                    } catch (InterruptedException e) {
                        // Stop gracefully.
                        break;
                    }
                }
            }
        }
    }
}
