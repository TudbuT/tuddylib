package tudbut.net.pbic2;

import java.io.IOException;

public interface PBIC2AListener {
    
    void onMessage(String message) throws IOException;
    
    void onError(Throwable throwable);
}
