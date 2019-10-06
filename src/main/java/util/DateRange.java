package util;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateRange {
    private Calendar starteDate;

    private Calendar endDate;

    public DateRange(Calendar starteDate, Calendar endDate) {
        this.starteDate = starteDate;
        this.endDate = endDate;
    }

    public Calendar getStarteDate() {
        return starteDate;
    }

    public void setStarteDate(Calendar starteDate) {
        this.starteDate = starteDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(starteDate, dateRange.starteDate) &&
                Objects.equals(endDate, dateRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(starteDate, endDate);
    }

    @Override
    public String toString() {
        return "DateRange{\n" +
                "\nstarteDate=" + starteDate.get(Calendar.DAY_OF_MONTH) +
                ",\nendDate=" + endDate.get(Calendar.DAY_OF_MONTH) +
                "\n}";
    }
}
