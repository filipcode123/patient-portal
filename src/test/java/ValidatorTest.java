import com.group15A.Utils.ErrorCode;
import com.group15A.Validator.Validator;
import junit.framework.TestCase;
import org.junit.Test;

import java.sql.Time;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class ValidatorTest extends TestCase {
    private Validator validator;

    @Override
    protected void setUp() {
        this.validator = new Validator();
    }

    @Test
    public void testIsAlphaContainsDigits() {
        assertFalse(this.validator.isAlpha("helloworld123"));
    }

    @Test
    public void testIsAlphaContainsSpecialChars() {
        assertFalse(this.validator.isAlpha("hel$low£or%ld"));
    }

    @Test
    public void testIsAlphaCaseSensitive() {
        assertTrue(this.validator.isAlpha("HeLLoWorlD"));
    }

    @Test
    public void testIsAlphaAllCharacters() {
        assertTrue(this.validator.isAlpha("helloworld"));
    }

    @Test
    public void testIsNumContainsChars() {
        assertFalse(this.validator.isNum("12345a"));
    }

    @Test
    public void testIsNumContainsSpecialChars() {
        assertFalse(this.validator.isNum("123^42&34"));
    }

    @Test
    public void testIsAlphaAllDigits() {
        assertTrue(this.validator.isNum("12345"));
    }

    @Test
    public void testVerifyNameFailure() {
        assertTrue(this.validator.verifyName("    "));
    }

    @Test
    public void testVerifyNameSuccess() {
        assertFalse(this.validator.verifyName("John"));
    }

    @Test
    public void testVerifyFirstNameSuccess() {
        assertNull(this.validator.verifyFirstName("John"));
    }

    @Test
    public void testVerifyFirstNameFailureAndCorrectErrorCode() {
        assertEquals(this.validator.verifyFirstName("John2"), ErrorCode.WRONG_FIRST_NAME);
    }

    @Test
    public void testVerifyMiddleNameSuccessNonBlankName() {
        assertNull(this.validator.verifyMiddleName("John"));
    }

    @Test
    public void testVerifyMiddleNameSuccessBlankName() {
        assertNull(this.validator.verifyMiddleName(""));
    }

    @Test
    public void testVerifyMiddleNameFailureAndCorrectErrorCode() {
        assertEquals(this.validator.verifyMiddleName("John2"), ErrorCode.WRONG_MIDDLE_NAME);
    }

    @Test
    public void testVerifyLastNameSuccess() {
        assertNull(this.validator.verifyLastName("John"));
    }

    @Test
    public void testVerifyLastNameFailureAndCorrectErrorCode() {
        assertEquals(this.validator.verifyLastName("John2"), ErrorCode.WRONG_LAST_NAME);
    }

    @Test
    public void testVerifyDateIncorrectFormatAndCorrectErrorCode() {
        assertEquals(this.validator.verifyDate("03-03-2002"), ErrorCode.WRONG_DATE);
    }

    @Test
    public void testVerifyDateFailureAndCorrectErrorCode() {
        assertEquals(this.validator.verifyDate("not_a_date_at_all"), ErrorCode.WRONG_DATE);
    }

    @Test
    public void testVerifyDateCorrectFormatNoZeroPadding() {
        assertNull(this.validator.verifyDate("2002-1-5"));
    }

    @Test
    public void testVerifyDateCorrectFormat() {
        assertNull(this.validator.verifyDate("2002-01-05"));
    }

    @Test
    public void testVerifyGenderMale() {
        assertNull(this.validator.verifyGender("Male"));
    }

    @Test
    public void testVerifyGenderFemale() {
        assertNull(this.validator.verifyGender("Female"));
    }

    @Test
    public void testVerifyGenderOther() {
        assertNull(this.validator.verifyGender("Other"));
    }

    @Test
    public void testVerifyGenderFailureAndCorrectErrorCode() {
        assertEquals(this.validator.verifyGender("X"), ErrorCode.WRONG_GENDER);
    }

    @Test
    public void testVerifyPhoneNoNotIntAndCorrectErrorCode() {
        assertEquals(this.validator.verifyPhoneNo("079234a234"), ErrorCode.WRONG_PHONE_NO);
    }

    @Test
    public void testVerifyPhoneNoCorrectButTooShort() {
        assertEquals(this.validator.verifyPhoneNo("1234"), ErrorCode.WRONG_PHONE_NO);
    }

    @Test
    public void testVerifyPhoneNoCorrectButTooLong() {
        assertEquals(this.validator.verifyPhoneNo("1234567891234567"), ErrorCode.WRONG_PHONE_NO);
    }

    @Test
    public void testVerifyPhoneNoBlankAndCorrectErrorCode() {
        assertEquals(this.validator.verifyPhoneNo(""), ErrorCode.WRONG_PHONE_NO);
    }

    @Test
    public void testVerifyPhoneNoCorrectExactMinimum() {
        assertNull(this.validator.verifyPhoneNo("12345"));
    }

    @Test
    public void testVerifyPhoneNoCorrectExactMaximum() {
        assertNull(this.validator.verifyPhoneNo("123451234512345"));
    }

    @Test
    public void testVerifyEmailSpecialCharsAndCorrectErrorCode() {
        assertEquals(this.validator.verifyEmail("user@mail.c*om"), ErrorCode.WRONG_EMAIL);
    }

    @Test
    public void testVerifyEmailMisplacedAtAndCorrectErrorCode() {
        assertEquals(this.validator.verifyEmail("usermail.c@om"), ErrorCode.WRONG_EMAIL);
    }

    @Test
    public void testVerifyEmailNoDotAndCorrectErrorCode() {
        assertEquals(this.validator.verifyEmail("user@mailcom"), ErrorCode.WRONG_EMAIL);
    }

    @Test
    public void testVerifyEmailMissingAtAndCorrectErrorCode() {
        assertEquals(this.validator.verifyEmail("usermail.com"), ErrorCode.WRONG_EMAIL);
    }

    @Test
    public void testVerifyEmailSuccess() {
        assertNull(this.validator.verifyEmail("user@mail.com"));
    }

    @Test
    public void testVerifyPasswordTooShortAndCorrectErrorCode() {
        assertEquals(this.validator.verifyPassword("12eA5s!"), ErrorCode.WRONG_PASSWORD);
    }

    @Test
    public void testVerifyPasswordNoUppercaseAndCorrectErrorCode() {
        assertEquals(this.validator.verifyPassword("12ea5s!!"), ErrorCode.WRONG_PASSWORD);
    }

    @Test
    public void testVerifyPasswordNoLowercaesAndCorrectErrorCode() {
        assertEquals(this.validator.verifyPassword("12EA5S!"), ErrorCode.WRONG_PASSWORD);
    }

    @Test
    public void testVerifyPasswordNoDigitsAndCorrectErrorCode() {
        assertEquals(this.validator.verifyPassword("aBcDeFgH!JKLm"), ErrorCode.WRONG_PASSWORD);
    }

    @Test
    public void testVerifyPasswordNoSpecialsAndCorrectErrorCode() {
        assertEquals(this.validator.verifyPassword("12eA5s898588997866"), ErrorCode.WRONG_PASSWORD);
    }

    @Test
    public void testVerifyPasswordSuccess() {
        assertNull(this.validator.verifyPassword("12345678Aa!"));
    }

    @Test
    public void testMismatchingEmailsAndCorrectErrorCode() {
        assertEquals(this.validator.verifyMatchingEmails("email1@mail.com", "email2@mail.com"), ErrorCode.WRONG_CONFIRMED_EMAIL);
    }

    @Test
    public void testMismatchingPasswordsAndCorrectErrorCode() {
        assertEquals(this.validator.verifyMatchingPasswords("password1", "password2"), ErrorCode.WRONG_CONFIRMED_PASSWORD);
    }

    @Test
    public void testMatchingEmails() {
        assertNull(this.validator.verifyMatchingEmails("email1@mail.com", "email1@mail.com"));
    }

    @Test
    public void testMatchingPasswords() {
        assertNull(this.validator.verifyMatchingPasswords("password1", "password1"));
    }

    @Test
    public void testVerifyTimestampHourNotNumAndCorrectErrorCode() {
        assertEquals(this.validator.verifyTimestamp("hello", "55"), ErrorCode.WRONG_TIME);
    }

    @Test
    public void testVerifyTimestampMinuteNotNumAndCorrectErrorCode() {
        assertEquals(this.validator.verifyTimestamp("9", "hello"), ErrorCode.WRONG_TIME);
    }

    @Test
    public void testVerifyTimestampWrongHourRangeAndCorrectErrorCode() {
        assertEquals(this.validator.verifyTimestamp("8", "2"), ErrorCode.WRONG_TIME);
    }

    @Test
    public void testVerifyTimestampWrongMinuteRangeAndCorrectErrorCode() {
        assertEquals(this.validator.verifyTimestamp("9", "60"), ErrorCode.WRONG_TIME);
    }

    @Test
    public void testVerifyTimestampSuccess() {
        assertNull(this.validator.verifyTimestamp("9", "55"));
    }

    @Test
    public void testBookingDateBeforeTodayAndCorrectErrorCode() {
        assertEquals(this.validator.verifyDateBeforeToday("2015-05-04 12:00:00.00"), ErrorCode.IMPOSSIBLE_BOOKING);
    }

    @Test
    public void testBookingDateAfterTodaySuccess() {
        // Two days in advance
        String later = new Timestamp(System.currentTimeMillis()+172800000).toString();
        assertNull(this.validator.verifyDateBeforeToday(later));
    }

    @Test
    public void testBookingDateAfterTodayVeryCloseSuccess() {
        // One hour in advance
        String later = new Timestamp(System.currentTimeMillis()+3600000).toString();
        assertNull(this.validator.verifyDateBeforeToday(later));
    }

    @Test
    public void testVerifyBookingTypeFailureAndCorrectErrorCode() {
        assertEquals(this.validator.verifyBookingType("WrongType"), ErrorCode.WRONG_BOOKING_TYPE);
    }

    @Test
    public void testVerifyBookingTypeSuccess() {
        assertNull(this.validator.verifyBookingType("Telephone Session"));
    }

}
