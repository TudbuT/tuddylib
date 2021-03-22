package tudbut.debug;

import tudbut.parsing.TudSort;

import java.util.ArrayList;
import java.util.Date;

public class DebugProfiler {

    public static final class Section {
        public final String name;
        private long start = 0;
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
    }
    
    private long start = new Date().getTime();
    private long end;
    private ArrayList<Section> sections = new ArrayList<>();
    private Section currentSection;
    private boolean locked = false;
    private Results results = null;
    
    public DebugProfiler(String startingSection) {
        currentSection = new Section(startingSection);
    }
    
    public synchronized DebugProfiler next(String next) {
        checkLocked();
        currentSection.end();
        sections.add(currentSection);
        currentSection = new Section(next);
        return this;
    }
    
    public synchronized DebugProfiler endAll() {
        checkLocked();
        currentSection.end();
        sections.add(currentSection);
        currentSection = null;
        end = new Date().getTime();
        
        locked = true;
        return this;
    }
    
    public synchronized Results getResults() {
        if(!locked)
            throw new RuntimeException("DebugProfiler results requested before call to endAll()!");
        if(results == null) {
            createResults();
        }
        return results;
    }
    
    private void createResults() {
        results = new Results();
        results.time = end - start;
        ArrayList<Section> allSections = new ArrayList<>();
        for (int i = 0 ; i < sections.size() ; i++) {
            Section sec = sections.get(i);
            sec.results = results;
            if(allSections.stream().noneMatch(section -> section.name.equals(sec.name))) {
                Section section = new Section(sec.name);
                section.start = 0;
                section.end = sec.getTime();
                section.results = results;
                allSections.add(sec);
            }
            else {
                for (int j = 0 ; j < allSections.size() ; j++) {
                    if(allSections.get(j).name.equals(sections.get(i).name)) {
                        allSections.get(j).end += sec.getTime();
                    }
                }
            }
        }
        results.sections = TudSort.sort(allSections.toArray(new Section[0]), section -> -section.getTime());
    }
    
    private synchronized void checkLocked() throws RuntimeException {
        if(locked)
            throw new RuntimeException("DebugProfiler modify requested after call to endAll()!");
    }
    
    public boolean isLocked() {
        return locked;
    }
}
