package de.tudbut.tools;

import java.util.HashMap;
import java.util.Map;

public class BetterJ {
    public static ForLoop f() {
        return new ForLoop();
    }

    public static Thread t(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public static void p(String t) {
        System.out.println(t);
    }

    public static void pn(String t) {
        System.out.print(t);
    }

    public static Map<String, String> newMap() {
        return new HashMap<>();
    }

    public static final class ForLoop {
        private final int step = 1;
        private final int to = 1;
        private int from = 0;

        ForLoop() {
        }

        public ForLoop1 from(int from) {
            this.from = from;
            return new ForLoop1(this);
        }

        public interface ForLoopRunnable {
            void run(int i);
        }

        public static final class ForLoop1 {
            protected int step;
            protected int from;
            protected int to;

            ForLoop1(ForLoop forLoop) {
                this.from = forLoop.from;
                this.to = forLoop.to;
                this.step = forLoop.step;
            }

            public ForLoop2 to(int to) {
                this.to = to;
                return new ForLoop2(this);
            }
        }

        public static final class ForLoop2 {
            protected int step;
            protected int from;
            protected int to;

            ForLoop2(ForLoop1 forLoop) {
                this.from = forLoop.from;
                this.to = forLoop.to;
                this.step = forLoop.step;
            }

            public ForLoop3 inclusively() {
                to += step;
                return new ForLoop3(this);
            }

            public ForLoop4 step(int step) {
                this.step = step;
                return new ForLoop4(this);
            }

            public void r(ForLoopRunnable r) {
                for (int i = from; i < to; i += step) {
                    r.run(i);
                }
            }
        }

        public static final class ForLoop3 {
            protected int step;
            protected int from;
            protected int to;

            ForLoop3(ForLoop2 forLoop) {
                this.from = forLoop.from;
                this.to = forLoop.to;
                this.step = forLoop.step;
            }

            public ForLoop4 step(int step) {
                this.step = step;
                return new ForLoop4(this);
            }

            public void r(ForLoopRunnable r) {
                for (int i = from; i < to; i += step) {
                    r.run(i);
                }
            }
        }

        public static final class ForLoop4 {
            protected int step;
            protected int from;
            protected int to;

            ForLoop4(ForLoop2 forLoop) {
                this.from = forLoop.from;
                this.to = forLoop.to;
                this.step = forLoop.step;
            }

            ForLoop4(ForLoop3 forLoop) {
                this.from = forLoop.from;
                this.to = forLoop.to;
                this.step = forLoop.step;
            }

            public void r(ForLoopRunnable r) {
                for (int i = from; i < to; i += step) {
                    r.run(i);
                }
            }
        }
    }
}
