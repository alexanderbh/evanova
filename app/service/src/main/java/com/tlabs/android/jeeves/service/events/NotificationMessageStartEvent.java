package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class NotificationMessageStartEvent extends CharacterMessageStartEvent {

    @ParcelConstructor
    public NotificationMessageStartEvent(long charID, long[] mailIds) {
        super(charID, mailIds);

    }

}
