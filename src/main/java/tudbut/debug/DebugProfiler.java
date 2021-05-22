package tudbut.debug;

import tudbut.parsing.TudSort;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class DebugProfiler {
    
    private final String name;
    private final long start = currentTimeMillis();
    private long end;
    private final ArrayList<Section> sections = new ArrayList<>();
    private Section currentSection;
    private boolean locked = false;
    private Results results = null;
    private boolean dirty = true;

    public static final class Section {
        public final String name;
        private long start;
        private long end = 0;
        private Results results;
        
        private Section(String s) {
            name = s;
            start = new Date().getTime();
        }
        
        private void end() {
            end = new Date().getTime();
        }
        
        public long getTime() {
            return end - start;
        }
        
        public float getTimeRelative() {
            return (float) getTime() / (float) results.getTotalTime();
        }
    
        @Override
        public String toString() {
            return name + ": " + getTime() + " (/" + results.getTotalTime() + ") (" + (getTimeRelative() * 100) + "%)";
        }
    }
    
    public static class Results {
        private long time;
        private Section[] sections;
    
        public long getTotalTime() {
            return time;
        }
        
        public Section[] getSections() {
            return sections;
        }
        
        public Section getSectionByName(String name) {
            for (int i = 0 ; i < sections.length ; i++) {
                if(sections[i].name.equals(name))
                    return sections[i];
            }
            return null;
        }
    
    
        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            for (int i = 0 ; i < sections.length ; i++) {
                s.append(sections[i].toString());
                if(i < sections.length - 1)
                    s.append("\n");
            }
            return s.toString();
        }
    }
    
    public DebugProfiler(String name, String startingSection) {
        this.name = name;
        currentSection = new Section(startingSection);
    }
    
    public synchronized DebugProfiler next(String next) {
        checkLocked();
        dirty = true;
        currentSection.end();
        synchronized (sections) {
            sections.add(currentSection);
        }
        currentSection = new Section(next);
        return this;
    }
    
    public synchronized DebugProfiler endAll() {
        checkLocked();
        dirty = true;
        currentSection.end();
        synchronized (sections) {
            sections.add(currentSection);
        }
        currentSection = null;
        end = currentTimeMillis();
        
        locked = true;
        return this;
    }
    
    public synchronized Results getResults() {
        if(!locked)
            throw new RuntimeException("DebugProfiler results requested before call to endAll()!");
        if(results == null) {
            return createResults();
        }
        return results;
    }
    
    public synchronized Results getTempResults() {
        checkLocked();
        end = currentTimeMillis();
        return createResults();
    }
    
    private Results createResults() {
        results = new Results();
        results.time = end - start;
        optimize();
        results.sections = TudSort.sort(sections.toArray(new Section[0]), section -> -section.getTime());
        return results;
    }
    
    public void optimize() {
        synchronized (sections) {
            if(dirty) {
                ArrayList<Section> realSections = new ArrayList<>();
                for (int i = 0 ; i < sections.size() ; i++) {
                    Section sec = sections.get(i);
                    sec.results = results;
                    if (realSections.stream().noneMatch(section -> section.name.equals(sec.name))) {
                        Section section = new Section(sec.name);
                        section.start = 0;
                        section.end = sec.getTime();
                        section.results = results;
                        realSections.add(sec);
                    }
                    else {
                        for (int j = 0 ; j < realSections.size() ; j++) {
                            if (realSections.get(j).name.equals(sections.get(i).name)) {
                                realSections.get(j).end += sec.getTime();
                            }
                        }
                    }
                }
                sections.clear();
                sections.addAll(realSections);
                dirty = false;
            }
        }
    }
    
    private synchronized void checkLocked() throws RuntimeException {
        if(locked)
            throw new RuntimeException("DebugProfiler modify requested after call to endAll()!");
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public String getName() {
        return name;
    }
    
    public synchronized void delete() {
        locked = true;
        sections.clear();
        currentSection = null;
        results = null;
        dirty = true;
    }
    
    public void finalize() {
        delete();
    }
    
    @Override
    public String toString() {
        return name + ":\n" + getTempResults().toString();
    }
}
