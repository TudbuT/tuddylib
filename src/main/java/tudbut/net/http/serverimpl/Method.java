package tudbut.net.http.serverimpl;

import tudbut.net.http.HTTPRequestType;

import java.lang.annotation.*;

public class Method {
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Get {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Post {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Head {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Put {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Patch {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Delete {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Trace {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Options {
    
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Connect {
    
    }
    
    public static Class<? extends Annotation> from(HTTPRequestType type) {
        switch (type) {
            case GET: 		return Get.class;
            case PUT: 		return Put.class;
            case HEAD: 		return Head.class;
            case POST: 		return Post.class;
            case PATCH: 	return Patch.class;
            case TRACE: 	return Trace.class;
            case DELETE: 	return Delete.class;
            case CONNECT: 	return Connect.class;
            case OPTIONS: 	return Options.class;
            default: 	return null;
        }
    }
}
