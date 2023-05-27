package android.support.v4.speech.tts;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import java.util.Locale;
import java.util.Set;

class TextToSpeechICSMR1 {
  public static final String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";
  
  public static final String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";
  
  static Set<String> getFeatures(TextToSpeech paramTextToSpeech, Locale paramLocale) {
    return (Build.VERSION.SDK_INT >= 15) ? paramTextToSpeech.getFeatures(paramLocale) : null;
  }
  
  static void setUtteranceProgressListener(TextToSpeech paramTextToSpeech, final UtteranceProgressListenerICSMR1 listener) {
    if (Build.VERSION.SDK_INT >= 15) {
      paramTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            public void onDone(String param1String) {
              listener.onDone(param1String);
            }
            
            public void onError(String param1String) {
              listener.onError(param1String);
            }
            
            public void onStart(String param1String) {
              listener.onStart(param1String);
            }
          });
      return;
    } 
    paramTextToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
          public void onUtteranceCompleted(String param1String) {
            listener.onStart(param1String);
            listener.onDone(param1String);
          }
        });
  }
  
  static interface UtteranceProgressListenerICSMR1 {
    void onDone(String param1String);
    
    void onError(String param1String);
    
    void onStart(String param1String);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\speech\tts\TextToSpeechICSMR1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */