package gt.com.whoscounting.domain;

import static gt.com.whoscounting.service.OfyService.ofy;
import gt.com.whoscounting.form.QuestionForm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotDefault;

/**
 * Conference class stores conference information.
 */
@Entity
public class Question {
	
    private static final String DEFAULT_CATEGORY = "Default";
	
	@Id
    private long id;

    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> profileKey;

    private String question;
    
    @Index(IfNotDefault.class)
    private String category;
    
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Date questionDate;
    
    @Index
    private int popularity;

    @Index
    private int iDo;
    
    /**
     * The userId of the person who ask.
     */
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String askerId;
    

    /**
     * Just making the default constructor private.
     */
    private Question() {}

    public Question(final long id, final String askerId,
                      final QuestionForm questionForm) {
        Preconditions.checkNotNull(questionForm.getQuestion(), "The question is required");
        this.id = id;
        this.profileKey = Key.create(Profile.class, askerId);
        this.askerId = askerId;
        updateWithQuestionForm(questionForm);
    }

    public long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }


    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Profile> getProfileKey() {
        return profileKey;
    }

    // Get a String version of the key
    public String getWebsafeKey() {
        return Key.create(profileKey, Question.class, id).getString();
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getAskerId() {
        return askerId;
    }

    /**
     * Returns organizer's display name.
     *
     * @return organizer's display name. If there is no Profile, return his/her userId.
     */
    public String getAskerDisplayName() {
        // Profile organizer = ofy().load().key(Key.create(Profile.class, organizerUserId)).now();
        Profile asker = ofy().load().key(getProfileKey()).now();
        if (asker == null) {
            return askerId;
        } else {
            return asker.getDisplayName();
        }
    }

    /**
     * Returns a defensive copy of topics if not null.
     * @return a defensive copy of topics if not null.
     */
    public String getCategory() {
        return category;
    }


    /**
     * Returns a defensive copy of startDate if not null.
     * @return a defensive copy of startDate if not null.
     */
    public Date getQuestionDate() {
        return questionDate == null ? null : new Date(questionDate.getTime());
    }


    public int getPopularity() {
        return popularity;
    }

    public int getIDo() {
        return iDo;
    }

    /**
     * Updates the Conference with ConferenceForm.
     * This method is used upon object creation as well as updating existing Conferences.
     *
     * @param conferenceForm contains form data sent from the client.
     */
    public void updateWithQuestionForm(QuestionForm questionForm) {
        this.question = questionForm.getQuestion();
        this.category = questionForm.getCategory();
        
        this.questionDate = new Date();
        
        this.popularity = 0;
        this.iDo = 0;
    }

    public void addPopularity(final int number) {
        popularity += number;
    }

    public void addIDo(final int number) {
        iDo += number;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Id: " + id + "\n")
                .append("Question: ").append(question).append("\n")
                .append("Category: ").append(category).append("\n")
                .append("iDo: ").append(iDo).append("\n");
        return stringBuilder.toString();
    }

}
