package com.tlabs.android.evanova.app.characters;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.characters.calendar.CharacterCalendarActivity;
import com.tlabs.android.evanova.app.characters.list.CharacterListActivity;
import com.tlabs.android.evanova.app.characters.main.CharacterViewActivity;
import com.tlabs.android.evanova.app.characters.training.CharacterTrainingActivity;
import com.tlabs.android.evanova.app.mails.MailModule;
import com.tlabs.android.evanova.app.skills.SkillDatabaseModule;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
                SkillDatabaseModule.class,
                CharacterModule.class,
                MailModule.class}
)
public interface CharacterComponent {

    void inject(CharacterListActivity activity);

    void inject(CharacterViewActivity activity);

    void inject(CharacterTrainingActivity activity);

    void inject(CharacterCalendarActivity activity);



}
