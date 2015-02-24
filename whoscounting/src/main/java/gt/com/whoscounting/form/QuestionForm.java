package gt.com.whoscounting.form;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotDefault;

import gt.com.whoscounting.domain.Profile;

import java.util.Date;
import java.util.List;

/**
 * A simple Java object (POJO) representing a Conference form sent from the client.
 */
public class QuestionForm {

    private String question;
    
    private String category;


    private QuestionForm() {}

    /**
     * Public constructor is solely for Unit Test.
     * @param name
     * @param description
     * @param topics
     * @param city
     * @param startDate
     * @param endDate
     * @param maxAttendees
     */
    public QuestionForm(String question, String category) {
        this.question = question;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCategory() {
        return category;
    }
}
