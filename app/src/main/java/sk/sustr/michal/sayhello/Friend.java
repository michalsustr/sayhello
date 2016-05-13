package sk.sustr.michal.sayhello;


/**
 * Created by Michal Sustr [michal.sustr@gmail.com] on 3/2/16.
 */
public class Friend  {
    private Integer id;
    private String name;
    private Integer days;
    private Integer lastContacted; // date in days

    public Friend(String name, Integer days) {
        this.name = name;
        this.days = days;
        this.lastContacted = MainActivity.getToday();
    }

    public Friend(String name, Integer days, Integer lastContacted) {
        this.name = name;
        this.days = days;
        this.lastContacted = lastContacted;
    }

    public Friend() {}

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getLastContacted() {
        return lastContacted;
    }

    public void setLastContacted(Integer lastContacted) {
        this.lastContacted = lastContacted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
