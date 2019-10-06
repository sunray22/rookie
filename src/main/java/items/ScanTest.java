package items;

import util.DateRange;

import java.util.Objects;

public class ScanTest {
    private Long id;

    private DateRange testDuration;

    private boolean done;

    private String priority;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateRange getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(DateRange testDuration) {
        this.testDuration = testDuration;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanTest scanTest = (ScanTest) o;
        return done == scanTest.done &&
                id.equals(scanTest.id) &&
                testDuration.equals(scanTest.testDuration) &&
                priority.equals(scanTest.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testDuration, done, priority);
    }

    @Override
    public String toString() {
        return "ScanTest{" +
                "id=" + id +
                ", testDuration=" + testDuration +
                ", done=" + done +
                ", priority=" + priority +
                '}';
    }
}
