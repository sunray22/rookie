package items;

import util.DateRange;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class User {
    private DateRange licenseDuration;

    private LinkedList<ScanTest> testsToBeScanned;

    public DateRange getLicenseDuration() {
        return licenseDuration;
    }

    public void setLicenseDuration(DateRange licenseDuration) {
        this.licenseDuration = licenseDuration;
    }

    public LinkedList<ScanTest> getTestsToBeScanned() {
        return testsToBeScanned;
    }

    public void setTestsToBeScanned(LinkedList<ScanTest> testsToBeScanned) {
        this.testsToBeScanned = testsToBeScanned;
    }
}
