package talker.text.substituter;

import talker.text.Text;

/**
 * Interface for something which can substitute text.
 */
public interface Substituter {
    Text substitute(Text text);
}
