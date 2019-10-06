import factory.SchedulerFactory;
import items.ScanTest;
import items.User;
import util.DateRange;

import java.util.*;

public class Scheduler {
    private static final long A_DAY_IN_MILLISECONDS = 86400000;

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

        // get new Test instance
        ScanTest newTest = SchedulerFactory.getScanTestInstanceWith(4, 6);
        newTest.setPriority("High");

        // get a Test user
        User user = SchedulerFactory.getUser();

        // get sample existing list of tests
        LinkedList<ScanTest> scanTestLinkedList = SchedulerFactory.getScheduledTests();

        try {
            // TODO: Doesn't handle weekends yet. Use isWeekend to implement this.
            scheduler.scheduleScanTests(scanTestLinkedList, newTest, user.getLicenseDuration());
        } catch (Exception e) {
            System.out.println("Your license exceeds expiry date with this schedule for scan. Please choose another range.\n\n");
        }
        System.out.println("Scheduled Tests: \n\n" + scanTestLinkedList.toString());
    }

    void scheduleScanTests(LinkedList<ScanTest> scanTestLinkedList, ScanTest newTest, DateRange sla) throws Exception {
        if (newTest.getPriority().equalsIgnoreCase("HIGH")) {
            assignFirstDatesOnlyOnPriority(scanTestLinkedList, newTest, sla.getEndDate());
        } else if (!assignFreeDates(scanTestLinkedList, newTest, sla.getEndDate())) {
            if (!assignTrailingDates(scanTestLinkedList, newTest, sla.getEndDate())) {
                // no other case to run
            }
        }
    }

    boolean assignFirstDatesOnlyOnPriority(LinkedList<ScanTest> scanTestLinkedList, ScanTest newTest, Calendar slaEndDate) throws Exception {
        boolean isAssignable = false;

        Calendar startDate = newTest.getTestDuration().getStarteDate();
        Calendar endDate = newTest.getTestDuration().getEndDate();
        Long newTestDurationInDays = noOfDays(newTest);

        startDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.DAY_OF_MONTH, 1 + newTestDurationInDays.intValue());

        scanTestLinkedList.offerFirst(newTest);
        isAssignable = true;

        ListIterator<ScanTest> itr = scanTestLinkedList.listIterator();
        itr.next(); // to skip the new test

        while (itr.hasNext()) {
            ScanTest next = itr.next();
            Calendar sd = next.getTestDuration().getStarteDate();
            Calendar ed = next.getTestDuration().getEndDate();
            sd.set(Calendar.DAY_OF_MONTH, sd.get(Calendar.DAY_OF_MONTH) + newTestDurationInDays.intValue() + 1);
            ed.set(Calendar.DAY_OF_MONTH, ed.get(Calendar.DAY_OF_MONTH) + newTestDurationInDays.intValue() + 1 );

            if(isLicenseExceeded(ed, slaEndDate, newTestDurationInDays)) {
                throw new Exception("Schedule Date exceeds SLA end date");
            }
        }

        return isAssignable;
    }

    boolean assignTrailingDates(LinkedList<ScanTest> scanTestLinkedList, ScanTest newTest, Calendar slaEndDate) throws Exception {
        boolean isAssignable = false;

        Calendar startDate = newTest.getTestDuration().getStarteDate();
        Calendar endDate = newTest.getTestDuration().getEndDate();
        Long newTestDurationInDays = noOfDays(newTest);

        Calendar lastTestEndDate = scanTestLinkedList.getLast().getTestDuration().getEndDate();
        if (!isLicenseExceeded(lastTestEndDate, slaEndDate, newTestDurationInDays)) {
            startDate.set(Calendar.DAY_OF_MONTH, lastTestEndDate.get(Calendar.DAY_OF_MONTH) + 1);
            endDate.set(Calendar.DAY_OF_MONTH, lastTestEndDate.get(Calendar.DAY_OF_MONTH) + newTestDurationInDays.intValue());
            scanTestLinkedList.offerLast(newTest);
            isAssignable = true;
        } else {
            throw new Exception("Schedule Date exceeds SLA end date");
        }

        return isAssignable;
    }

    boolean assignFreeDates(LinkedList<ScanTest> scanTestLinkedList, ScanTest newTest, Calendar slaEndDate) throws Exception {
        boolean isAssignable = false;

        Calendar startDate = newTest.getTestDuration().getStarteDate();
        Calendar endDate = newTest.getTestDuration().getEndDate();
        Long newTestDurationInDays = noOfDays(newTest);

        ListIterator<ScanTest> itr = scanTestLinkedList.listIterator();

        //check if there are already scheduled tests
        if (itr.hasNext()) {
            ScanTest first = itr.next();
            // skip the first to make the next loop work
        } else if(isLicenseExceeded(endDate, slaEndDate, newTestDurationInDays)) { // if no tests exist, add into list and return
            scanTestLinkedList.add(newTest);
            isAssignable = true;
            return isAssignable;
        } else {
            throw new Exception("Schedule Date exceeds SLA end date");
        }

        while (itr.hasNext()) {
            ScanTest next = itr.next();
            Calendar sd = next.getTestDuration().getStarteDate();
            Calendar ed = next.getTestDuration().getEndDate();
            int prevEndDate = itr.previous().getTestDuration().getEndDate().get(Calendar.DAY_OF_MONTH);
            if((sd.get(Calendar.DAY_OF_MONTH) - prevEndDate) >= newTestDurationInDays) {
                startDate.set(Calendar.DAY_OF_MONTH, prevEndDate + 1);
                endDate.set(Calendar.DAY_OF_MONTH, prevEndDate + newTestDurationInDays.intValue());
                isAssignable = true;
                break;
            }

            if(isLicenseExceeded(ed, slaEndDate, newTestDurationInDays)) {
                throw new Exception("Schedule Date exceeds SLA end date");
            }

            // to avoid traversing back
            itr.next();
        }

        return isAssignable;
    }

    Long noOfDays(ScanTest test) {
        Long noOfDays = 0l;

        Calendar startDate= test.getTestDuration().getStarteDate();
        Calendar endDate = test.getTestDuration().getEndDate();
        if(endDate.getTimeInMillis() == startDate.getTimeInMillis()) {
            noOfDays = 1l;
        }
        noOfDays = (endDate.getTimeInMillis() - startDate.getTimeInMillis())/A_DAY_IN_MILLISECONDS;

        return noOfDays;
    }

    boolean isWeekend(Calendar aDate) {
        boolean isWeekEnd = false;

        int weekDay = aDate.get(Calendar.DAY_OF_WEEK);
        if((weekDay == 6) || (weekDay == 7)) {
            isWeekEnd = true;
        }

        return  isWeekEnd;
    }

    boolean isLicenseExceeded(Calendar testDate, Calendar slaEndDate,Long noOfDays) {
         Long noOfDaysRemaining= (slaEndDate.getTimeInMillis() - testDate.getTimeInMillis())/A_DAY_IN_MILLISECONDS;
        return noOfDays >= noOfDaysRemaining;
    }
}
