package tudbut.logger;

public class EmptyLogger implements LoggerSink {
    private final String name;

    public EmptyLogger(String name) {
        this.name = name;
    }

    public EmptyLogger() {
        this.name = null;
    }

    public LoggerSink subChannel(String subChannel) {
        return new EmptyLogger(name + "] [" + subChannel);
    }
    
    @Override
    public void info(String string) {
    
    }
    
    @Override
    public void debug(String string) {
    
    }
    
    @Override
    public void warn(String string) {
    
    }
    
    @Override
    public void error(String string) {
    
    }
    
    @Override
    public String getName() {
        return name;
    }

}
