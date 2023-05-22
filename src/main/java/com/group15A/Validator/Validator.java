package com.group15A.Validator;

import com.group15A.Utils.ErrorCode;
import org.apache.commons.validator.GenericValidator;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class for validating form details on the register panel
 *
 * @author Milovan Gveric
 */
public class Validator {
    private final Pattern containsUpperCase = Pattern.compile("^(?=.*[a-z]).+$");
    private final Pattern containsLowerCase = Pattern.compile("^(?=.*[A-Z]).+$");
    private final Pattern containsDigit = Pattern.compile("^(?=.*\\d).+$");
    private final Pattern containsSpecials = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

    /**
     * @param str a string
     * @return true if string is purely alphabetic
     */
    public Boolean isAlpha(String str) {
        return str.chars().allMatch(Character::isLetter);
    }

    /**
     * @param str a string
     * @return true if string is purely digits
     */
    public Boolean isNum(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    /**
     * @param name a first, middle or last name
     * @return true if name is blank or isn't purely alphabetic
     */
    public Boolean verifyName(String name) {
        return (name.isBlank() || !this.isAlpha(name));
    }

    /**
     * @param fName first name
     * @return Error code if first name is invalid format, otherwise null
     * indicating no error
     */
    public ErrorCode verifyFirstName(String fName) {
        if (this.verifyName(fName)) {
            return ErrorCode.WRONG_FIRST_NAME;
        }
        return null;
    }

    /**
     * @param mName middle name
     * @return Error code if middle name is invalid format, otherwise null
     * indicating no error
     */
    public ErrorCode verifyMiddleName(String mName){
        if (!this.isAlpha(mName)) {
            return ErrorCode.WRONG_MIDDLE_NAME;
        }
        return null;
    }

    /**
     * @param lName last name
     * @return Error code if last name is invalid format, otherwise null
     * indicating no error
     */
    public ErrorCode verifyLastName(String lName) {
        if (this.verifyName(lName)) {
            return ErrorCode.WRONG_LAST_NAME;
        }
        return null;
    }

    /**
     * @param date date of birth
     * @return Error code if DoB is invalid format, otherwise null
     * indicating no error
     */
    public ErrorCode verifyDate(String date) {
        if (!GenericValidator.isDate(date, "yyyy-MM-dd", false)) {
            return ErrorCode.WRONG_DATE;
        }
        return null;
    }

    /**
     * @param gender gender
     * @return Error code if gender is not Male, Female or Other, otherwise null
     * indicating no error
     */
    public ErrorCode verifyGender(String gender) {
        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            return ErrorCode.WRONG_GENDER;
        }
        return null;
    }

    /**
     * @param phoneNo phone number
     * @return Error code if phone number is not purely digits, in the min and max ranges
     * or blank, otherwise null indicating no error
     */
    public ErrorCode verifyPhoneNo(String phoneNo) {
        if (!this.isNum(phoneNo) || !GenericValidator.isInRange(phoneNo.length(), 5, 15) || phoneNo.isBlank()) {
            return ErrorCode.WRONG_PHONE_NO;
        }
        return null;
    }

    /**
     * @param email email
     * @return Error code if email is invalid format, otherwise null
     * indicating no error
     */
    public ErrorCode verifyEmail(String email) {
        if (!GenericValidator.isEmail(email)) {
            return ErrorCode.WRONG_EMAIL;
        }
        return null;
    }

    /**
     * @param password password
     * @return Error code if password is less than 8 characters, doesn't
     * contain at least one uppercase letter, lowercase letter, digit
     * and special character, otherwise null, indicating no error
     */
    public ErrorCode verifyPassword(String password) {
        // must have min 8 chars, 1 upper, 1 lower, 1 digit, 1 special character
        Boolean invalidLength = password.length() < 8;
        Boolean noUpperCase = !containsUpperCase.matcher(password).matches();
        Boolean noLowerCase = !containsLowerCase.matcher(password).matches();
        Boolean noDigit = !containsDigit.matcher(password).matches();
        Boolean noSpecials = !containsSpecials.matcher(password).find();

        if (invalidLength || noUpperCase || noLowerCase || noDigit || noSpecials) {
            return ErrorCode.WRONG_PASSWORD;
        }

        return null;
    }

    /**
     * @param email email
     * @param confirmEmail confirmation email
     * @return Error code if emails don't match, otherwise null,
     * indicating no error
     */
    public ErrorCode verifyMatchingEmails(String email, String confirmEmail) {
        if (!email.equals(confirmEmail)) {
            return ErrorCode.WRONG_CONFIRMED_EMAIL;
        }

        return null;
    }

    /**
     * @param password password
     * @param confirmPassword confirmation password
     * @return Error code if passwords don't match, otherwise null,
     * indicating no error
     */
    public ErrorCode verifyMatchingPasswords(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return ErrorCode.WRONG_CONFIRMED_PASSWORD;
        }

        return null;
    }

    /**
     * @param hour
     * @param minute
     * @return Error code if hour or minute are not correct time values,
     * otherwise null, indicating no error
     */
    public ErrorCode verifyTimestamp(String hour, String minute) {
        Boolean hourNotNum = !isNum(hour);
        Boolean minuteNotNum = !isNum(minute);

        if (hourNotNum || minuteNotNum) {
            return ErrorCode.WRONG_TIME;
        }

        Boolean wrongHourRange = !GenericValidator.isInRange(Integer.parseInt(hour), 9, 17);
        Boolean wrongMinuteRange = !GenericValidator.isInRange(Integer.parseInt(minute), 0, 55);

        if (wrongHourRange || wrongMinuteRange) {
            return ErrorCode.WRONG_TIME;
        }

        return null;
    }

    /**
     * @param timestamp
     * @return Error code if the selected booking time is before the current date and time,
     * otherwise null, indicating no error
     */
    public ErrorCode verifyDateBeforeToday(String timestamp) {
        Timestamp bookingTimestamp = Timestamp.valueOf(timestamp);
        Timestamp today = new Timestamp(System.currentTimeMillis());
        if (bookingTimestamp.before(today)) {
            return ErrorCode.IMPOSSIBLE_BOOKING;
        }

        return null;
    }

    /**
     * @param type the booking type selected in the dropdown in AddBookingPanel
     * @return Error code if the selected booking type is not one of the strings from the dropdown,
     * otherwise null, indicating no error
     */
    public ErrorCode verifyBookingType(String type) {
        List<String> types = Arrays.asList(
            "Other", "Routine Checkup", "Emergency Checkup",
            "Telephone Session", "Surgery", "Physical Checkup",
            "Mental Health Checkup", "Blood Testing",
            "General Consultation"
        );

        if (!types.contains(type)) {
            return ErrorCode.WRONG_BOOKING_TYPE;
        }

        return null;
    }
}
