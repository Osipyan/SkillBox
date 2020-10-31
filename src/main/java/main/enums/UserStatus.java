package main.enums;

public enum UserStatus {
    INACTIVE (ModerationStatus.NEW, false),
    PENDING (ModerationStatus.NEW, true),
    DECLINED (ModerationStatus.DECLINED, true),
    PUBLISHED (ModerationStatus.ACCEPTED, true);

    private ModerationStatus moderationStatus;
    private boolean isActive;

    UserStatus(ModerationStatus moderationStatus, boolean isActive) {
        this.moderationStatus = moderationStatus;
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public boolean isActive() {
        return isActive;
    }
}
