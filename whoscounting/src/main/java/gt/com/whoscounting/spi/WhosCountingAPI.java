package gt.com.whoscounting.spi;

import static gt.com.whoscounting.service.OfyService.ofy;
import gt.com.whoscounting.Constants;
import gt.com.whoscounting.domain.Profile;
import gt.com.whoscounting.domain.Question;
import gt.com.whoscounting.form.ProfileForm;
import gt.com.whoscounting.form.ProfileForm.Country;
import gt.com.whoscounting.form.ProfileForm.Gender;
import gt.com.whoscounting.form.QuestionForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.cmd.Query;

/**
 * Defines question APIs.
 */
@Api(name = "whosCounting", version = "v1", scopes = {Constants.EMAIL_SCOPE },
    clientIds = {
		Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID,
		Constants.ANDROID_CLIENT_ID,
		Constants.IOS_CLIENT_ID},
    description = "API for answers.")

public class WhosCountingAPI {
	/*
	 * Get the display name from the user's email. For example, if the email is
	 * lemoncake@example.com, then the display name becomes "lemoncake."
	 */
	private static String extractDefaultDisplayNameFromEmail(String email) {
		return email == null ? null : email.substring(0, email.indexOf("@"));
	}

	/**
	 * Creates or updates a Profile object associated with the given user
	 * object.
	 *
	 * @param user
	 *            A User object injected by the cloud endpoints.
	 * @param profileForm
	 *            A ProfileForm object sent from the client form.
	 * @return Profile object just created.
	 * @throws UnauthorizedException
	 *             when the User object is null.
	 */
	
////////////////////////////////////////////////////Profile////////////////////////////////////////////////////
	
    // Declare this method as a method available externally through Endpoints
	@ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
	
	// The request that invokes this method should provide data that
	// conforms to the fields defined in ProfileForm

	// TODO 1 Pass the ProfileForm parameter
	// TODO 2 Pass the User parameter
	public Profile saveProfile(final User user, ProfileForm profileForm)
			throws UnauthorizedException {

		String userId = "";
		String mainEmail = "";
		String displayName = "Your name will go here";
		Country country = null;
		Gender gender = null;

		// TODO 2
		// If the user is not logged in, throw an UnauthorizedException
		if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

		// TODO 1
	    // Set the teeShirtSize to the value sent by the ProfileForm, if sent
        // otherwise leave it as the default value
		country = profileForm.getCountry();
		gender = profileForm.getGender();

		// TODO 1
        // Set the displayName to the value sent by the ProfileForm, if sent
        // otherwise set it to null
		displayName = profileForm.getDisplayName();
		
		// TODO 2
		// Get the userId and mainEmail
		mainEmail = user.getEmail();
		userId = user.getUserId();

        // TODO 2
        // If the displayName is null, set it to the default value based on the user's email
        // by calling extractDefaultDisplayNameFromEmail(...)
		 

		// Create a new Profile entity from the
		// userId, displayName, mainEmail and teeShirtSize
		
		Profile profile = getProfile(user);
		if(profile == null) {
			if (displayName == null) {
				displayName = extractDefaultDisplayNameFromEmail(user.getEmail());
			}
			if(country == null){
				country = country.NOT_SPECIFIED;
			}
			if(gender == null){
				gender = gender.NOT_SPECIFIED;
			}
			profile = new Profile(userId, displayName, mainEmail, country, gender);
		}
		else {
			profile.updateProfile(displayName, country, gender);
		}

		// TODO 3 (In lesson 3)
		// Save the entity in the datastore
		ofy().save().entity(profile).now();
		
		// Return the profile
		return profile;
	}

	/**
	 * Returns a Profile object associated with the given user object. The cloud
	 * endpoints system automatically inject the User object.
	 *
	 * @param user
	 *            A User object injected by the cloud endpoints.
	 * @return Profile object.
	 * @throws UnauthorizedException
	 *             when the User object is null.
	 */
	@ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
	public Profile getProfile(final User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		// TODO
		// load the Profile Entity
		String userId = user.getUserId(); // TODO
		Key key = Key.create(Profile.class, userId); // TODO
		Profile profile = (Profile) ofy().load().key(key).now(); // TODO load the Profile entity
		return profile;
	}
	
	
	
	 /**
     * Gets the Profile entity for the current user
     * or creates it if it doesn't exist
     * @param user
     * @return user's Profile
     */
    private static Profile getProfileFromUser(User user) {
        // First fetch the user's Profile from the datastore.
        Profile profile = ofy().load().key(
                Key.create(Profile.class, user.getUserId())).now();
        if (profile == null) {
            // Create a new Profile if it doesn't exist.
            // Use default displayName and teeShirtSize
            String email = user.getEmail();
            profile = new Profile(user.getUserId(),
                    extractDefaultDisplayNameFromEmail(email), email, Country.NOT_SPECIFIED, Gender.NOT_SPECIFIED);
        }
        return profile;
    }
    
////////////////////////////////////////////////////Question////////////////////////////////////////////////////
    

    /**
     * Creates a new Question object and stores it to the datastore.
     *
     * @param user A user who invokes this method, null when the user is not signed in.
     * @param questionForm A QuestionForm object representing user's inputs.
     * @return A newly created Question Object.
     * @throws UnauthorizedException when the user is not signed in.
     */
    @ApiMethod(name = "createQuestion", path = "question", httpMethod = HttpMethod.POST)
    public Question createQuestion(final User user, final QuestionForm questionForm)
        throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // TODO (Lesson 4)
        // Get the userId of the logged in User
        String userId = user.getUserId();

        // TODO (Lesson 4)
        // Get the key for the User's Profile
        Key<Profile> profileKey = Key.create(Profile.class, user.getUserId());

        // TODO (Lesson 4)
        // Allocate a key for the question -- let App Engine allocate the ID
        // Don't forget to include the parent Profile in the allocated ID
        ObjectifyFactory data = new ObjectifyFactory();
        final Key<Question> questionKey = data.allocateId(profileKey,Question.class);
        
        // TODO (Lesson 4)
        // Get the Question Id from the Key
        final long questionId = questionKey.getId();

        // TODO (Lesson 4)
        // Get the existing Profile entity for the current user if there is one
        // Otherwise create a new Profile entity with default values
        Profile profile = getProfileFromUser(user);

        // TODO (Lesson 4)
        // Create a new Question Entity, specifying the user's Profile entity
        // as the parent of the question
        Question question = new Question(questionId, userId, questionForm);

        // TODO (Lesson 4)
        // Save Question and Profile Entities
         ofy().save().entities(profile,question).now();

         return question;
    }

	
    @ApiMethod(name = "queryQuestions", path="queryQuestions", httpMethod = HttpMethod.GET)
    public List<Question> queryQuestions(){
    	Query<Question> query = ofy().load().type(Question.class).order("popularity");
    	return query.list();
    }
    
    @ApiMethod(name = "getQuestionsCreated", path="getQuestionsCreated", httpMethod = HttpMethod.POST)
    public List<Question> getQuestionsCreated(final User user)throws UnauthorizedException {
		if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
		Key userKey = Key.create(Profile.class, user.getUserId());
    	Query<Question> query = ofy().load().type(Question.class).ancestor(userKey).order("popularity");
    	return query.list();
    }


////////////////////////////////////////////////////Answers////////////////////////////////////////////////////
    
    @ApiMethod(name = "getQuestion", path="question/{websafeQuestionKey}", httpMethod = HttpMethod.GET)
    public Question getQuestion(@Named("websafeQuestionKey") final String websafeQuestionKey) throws NotFoundException{
    	Key<Question> questionKey = Key.create(websafeQuestionKey);
    	Question question = ofy().load().key(questionKey).now();
    	if(question == null){
    		throw new NotFoundException("No Question found with key: "+websafeQuestionKey);
    	}
    	return question;
    }
    
    @ApiMethod(name = "answerQuestion", path="answerQuestion", httpMethod = HttpMethod.GET)
    public WrappedBoolean answerQuestion(final User user,
            @Named("websafeQuestionKey") final String websafeQuestionKey, 
            @Named("answer") final Boolean answer)
            throws UnauthorizedException, NotFoundException,
            ForbiddenException, ConflictException {
        // If not signed in, throw a 401 error.
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        
        // Get the userId
        final String userId = user.getUserId();

        // Get the question key
        Key<Question> questionKey = Key.create(websafeQuestionKey);

        // Get the Question entity from the datastore
        Question question = ofy().load().key(questionKey).now();

        // 404 when there is no Question with the given questionId.
        if (question == null) {
            return new WrappedBoolean (false,
                    "No Question found with key: "
                            + websafeQuestionKey);
        }

        // Get the user's Profile entity
        Profile profile = getProfileFromUser(user);
        
        if((!profile.getMiIDoAnswers().contains(websafeQuestionKey))
        		&& (!profile.getMiIDontAnswers().contains(websafeQuestionKey))){
        	question.addPopularity(1);
        }

        int valorIDo = profile.answerQuestion(websafeQuestionKey, answer);
        question.addIDo(valorIDo);

        // Save the Question and Profile entities
        ofy().save().entities(profile, question).now();
        // We are booked!
           
        
        return new WrappedBoolean(true);
    }


   /**
     * Returns a collection of Question Object that the user is going to attend.
     *
     * @param user An user who invokes this method, null when the user is not signed in.
     * @return a Collection of Questions that the user is going to attend.
     * @throws UnauthorizedException when the User object is null.
     */
    @ApiMethod(
            name = "getMiIDoAnswers",
            path = "getMiIDoAnswers",
            httpMethod = HttpMethod.GET
    )
    public Collection<Question> getMiIDoAnswers(final User user)
            throws UnauthorizedException, NotFoundException {
        // If not signed in, throw a 401 error.
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        if (profile == null) {
            throw new NotFoundException("Profile doesn't exist.");
        }
        List<String> keyStringsToAttend = profile.getMiIDoAnswers();
        List<Key<Question>> keysToAttend = new ArrayList<>();
        for (String keyString : keyStringsToAttend) {
            keysToAttend.add(Key.<Question>create(keyString));
        }
        return ofy().load().keys(keysToAttend).values();
    }
    
    @ApiMethod(
            name = "getMiIDontAnswers",
            path = "getMiIDontAnswers",
            httpMethod = HttpMethod.GET
    )
    public Collection<Question> getMiIDontAnswers(final User user)
            throws UnauthorizedException, NotFoundException {
        // If not signed in, throw a 401 error.
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        if (profile == null) {
            throw new NotFoundException("Profile doesn't exist.");
        }
        List<String> keyStringsToAttend = profile.getMiIDontAnswers();
        List<Key<Question>> keysToAttend = new ArrayList<>();
        for (String keyString : keyStringsToAttend) {
            keysToAttend.add(Key.<Question>create(keyString));
        }
        return ofy().load().keys(keysToAttend).values();
    }

 
    public static class WrappedBoolean {

        private final Boolean result;
        private final String reason;

        public WrappedBoolean(Boolean result) {
            this.result = result;
            this.reason = "";
        }

        public WrappedBoolean(Boolean result, String reason) {
            this.result = result;
            this.reason = reason;
        }

        public Boolean getResult() {
            return result;
        }

        public String getReason() {
            return reason;
        }
    }
    
    
    @ApiMethod(
            name = "pruebaPost",
            path = "pruebaPost",
            httpMethod = HttpMethod.POST
    )
    public HelloClass pruebaPost(@Named("name") String name) {
    	HelloClass regreso = new HelloClass(name);
    	return regreso;
    }
	
}
