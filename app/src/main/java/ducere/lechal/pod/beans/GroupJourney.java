package ducere.lechal.pod.beans;

import java.io.Serializable;

/**
 * Created by Siva on 24-05-2016.
 */
public class GroupJourney implements Serializable {
    String type;
    String groupId;
    String title;
    String description;
    String createdUser;
    String invitedUsers[];
    String destName;
    String destLocation;
    int journeyType ;

    public GroupJourney() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String[] getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(String[] invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getDestLocation() {
        return destLocation;
    }

    public void setDestLocation(String destLocation) {
        this.destLocation = destLocation;
    }

    public int getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(int journeyType) {
        this.journeyType = journeyType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
