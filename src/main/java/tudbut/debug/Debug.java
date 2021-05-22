package tudbut.debug;

import java.util.HashMap;
import java.util.Map;

public class Debug {
    
    private static final Map<Thread, Map<Object, DebugProfiler>> debugProfilerMap = new HashMap<>();

    public static DebugProfiler getDebugProfiler(java.lang.reflect.GenericDeclaration o, boolean allowFinished) {
        if(!debugProfilerMap.containsKey(Thread.currentThread()))
            debugProfilerMap.put(Thread.currentThread(), new HashMap<>());
        
        if(debugProfilerMap.get(Thread.currentThread()).containsKey(o)) {
            DebugProfiler profiler = debugProfilerMap.get(Thread.currentThread()).get(o);
            if(allowFinished && profiler.isLocked()) {
                return profiler;
            }
            else if(!profiler.isLocked()) {
                return profiler;
            }
        }
        debugProfilerMap.get(Thread.currentThread()).put(o, new DebugProfiler(o.toString(), "init"));
        return debugProfilerMap.get(Thread.currentThread()).get(o);
    }
}
