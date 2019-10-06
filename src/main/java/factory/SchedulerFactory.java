package factory;

import items.ScanTest;
import items.User;
import util.DateRange;

import java.util.Calendar;
import java.util.LinkedList;

public class SchedulerFactory {

    private static long  id = 0l;

    public static ScanTest getScanTestInstanceWith(int start,int end) {
        ScanTest test = new ScanTest();
        test.setId(id++);
        test.setDone(false);
        test.setTestDuration(getInstanceOfDateRangeWith(start, end));
        return test;
    }

    public static DateRange getInstanceOfDateRangeWith(int start,int end) {
        return new DateRange(new Calendar.Builder().setDate(2019, 1,start).build(),
                new Calendar.Builder().setDate(2019, 1,end).build());
    }

    public static LinkedList<ScanTest> getScheduledTests() {
        LinkedList<ScanTest> scanTestList = new LinkedList<>();
        scanTestList.add(SchedulerFactory.getScanTestInstanceWith(1,2));
        scanTestList.add(SchedulerFactory.getScanTestInstanceWith(3,3));
        scanTestList.add(SchedulerFactory.getScanTestInstanceWith(5,7));
        return scanTestList;
    }

    public static User getUser() {
        User user = new User();
        user.setLicenseDuration(SchedulerFactory.getInstanceOfDateRangeWith(1,10));
        return user;
    }

}
