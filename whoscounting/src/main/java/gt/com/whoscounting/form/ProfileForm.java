package gt.com.whoscounting.form;

/**
 * Pojo representing a profile form on the client side.
 */
public class ProfileForm {

    private String displayName;

    private Country country;
    
    private Gender gender;

    private ProfileForm () {}

    /**
     * Constructor for ProfileForm, solely for unit test.
     * @param displayName A String for displaying the user on this system.
     * @param notificationEmail An e-mail address for getting notifications from this system.
     */
    public ProfileForm(String displayName, Country country,Gender gender) {
        this.displayName = displayName;
        this.country = country;
        this.gender = gender;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Country getCountry() {
        return country;
    }
    
    public Gender getGender() {
		return gender;
	}
    
    public static enum Country {
    	NOT_SPECIFIED,
        GT,
        US
    }
    
    public static enum Gender {
    	NOT_SPECIFIED,
        M,
        F
    }
}
