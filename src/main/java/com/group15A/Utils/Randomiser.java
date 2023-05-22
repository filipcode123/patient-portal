package com.group15A.Utils;

import java.util.Random;

/**
 * Mocks doctors and their inputs regarding a patient's booking prescriptions and details
 * (in lieu of the doctor application)
 *
 * @author Wenbo Wu
 * @author Milovan Gveric
 */
public class Randomiser {
    private final String[] prescriptions = new String[] {
            "Headache medicine",
            "Skin cream",
            "Pain-killer",
            "Oxprenolol",
            "Cardiovascular drugs",
            "Insulin",
            "Cold medication",
            "Antacids",
            "Stomach medicine",
            "Allergy medication",
            "Asthma medicine",
            "Antivirus medication",
            "Heart disease medication",
            "Hypertension medicine",
            "Gout medication",
            "Diabetes drugs",
            "Contraception pills",
            "Chronic disease medication",
            "Sore throat medicine",
            "Oral health medication"
    };

    private final String[] details = new String[] {
            "Weekly checkup was performed. Patient was in good health.",
            "Blood sample was taken.",
            "Old prescription is no longer being recommended. New prescription assigned.",
            "Patient found to have an allergy. Regular check-ups recommended to assess patient health.",
            "Patient was advised to drink warm water.",
            "Patient was advised to eat low-sugar food for daily meals, to prevent diabetes.",
            "Patient was advised to eat low-salt food for daily meals, to prevent CVD.",
            "Patient was advised to eat low-fat food for daily meals, to prevent cardiovascular disease.",
            "Examination shows patient has gluten allergy. A gluten free diet was recommended.",
            "Patient's bio-sample was taken.",
            "Patient took first vaccine. An appointment for the second vaccination must be made.",
            "Scheduled for future full Orthopantomogram X-Ray appointment.",
            "Scheduled for future appointment for patient to have orthopedic surgery.",

    };

    private final Random randomGen;

    public Randomiser() {
        this.randomGen = new Random();
    }

    /**
     * @return a random string from the prescriptions array
     */
    public String getRandPrescription() {
        return prescriptions[randomGen.nextInt(prescriptions.length)];
    }

    /**
     * @return a random string from the details array
     */
    public String getRandDetails() {
        return details[randomGen.nextInt(details.length)];
    }
}