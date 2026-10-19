package com.mycompany.ernestprog5121part2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ERNESTPROG5121PART2Test {

    @BeforeEach
    public void setUp() {
        // reset shared static state before every test so tests don't interfere with each other
        ERNESTPROG5121PART2.sentMessageIDs.clear();
        ERNESTPROG5121PART2.sentMessageHashes.clear();
        ERNESTPROG5121PART2.sentRecipients.clear();
        ERNESTPROG5121PART2.sentMessageTexts.clear();
        ERNESTPROG5121PART2.storedMessages.clear();
        ERNESTPROG5121PART2.messageCounter = 0;
    }

    // ==================== Part 1: Registration/Login tests ====================

    // --- Username tests ---

    @Test
    public void testUsername_CorrectlyFormatted() {
        // Test Data: "kyl_1"
        assertTrue(ERNESTPROG5121PART2.checkUserName("kyl_1"));
    }

    @Test
    public void testUsername_IncorrectlyFormatted() {
        // Test Data: "kyleeeeeeee" (no underscore, too long)
        assertFalse(ERNESTPROG5121PART2.checkUserName("kyleeeeeeee"));
    }

    // --- Password tests ---

    @Test
    public void testPassword_MeetsComplexityRequirements() {
        // Test Data: "Ch&&sec@1a99"
        assertTrue(ERNESTPROG5121PART2.checkPasswordComplexity("Ch&&sec@1a99"));
    }

    @Test
    public void testPassword_DoesNotMeetComplexityRequirements() {
        // Test Data: "password" (no capital, no number, no special char)
        assertFalse(ERNESTPROG5121PART2.checkPasswordComplexity("password"));
    }

    // --- Cell phone number tests ---

    @Test
    public void testCellNumber_CorrectlyFormatted() {
        // Test Data: "+27830968976"
        assertTrue(ERNESTPROG5121PART2.checkCellPhoneNumber("+27830968976"));
    }

    @Test
    public void testCellNumber_IncorrectlyFormatted() {
        // Test Data: "0830968976" (missing international code)
        assertFalse(ERNESTPROG5121PART2.checkCellPhoneNumber("0830968976"));
    }

    // --- Registration message tests ---

    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        String result = ERNESTPROG5121PART2.registerUser("kyleeeeeeee", "Ch&&sec@1a99");
        assertEquals("Username is not correct. Must have _ and max 5 chars.", result);
    }

    @Test
    public void testRegisterUser_PasswordDoesNotMeetComplexity() {
        String result = ERNESTPROG5121PART2.registerUser("kyl_1", "password");
        assertEquals("Password is not correct. Must have 8+ chars, capital letter, number, and special char.", result);
    }

    @Test
    public void testRegisterUser_Success() {
        String result = ERNESTPROG5121PART2.registerUser("kyl_1", "Ch&&sec@1a99");
        assertEquals("Username and password successfully captured. User registered!", result);
    }

    // --- Login tests ---

    @Test
    public void testLogin_Successful() {
        assertTrue(ERNESTPROG5121PART2.loginUser("kyl_1", "Ch&&sec@1a99", "kyl_1", "Ch&&sec@1a99"));
    }

    @Test
    public void testLogin_Failed() {
        assertFalse(ERNESTPROG5121PART2.loginUser("kyl_1", "wrongPassword1!", "kyl_1", "Ch&&sec@1a99"));
    }

    @Test
    public void testReturnLoginStatus_Success() {
        assertEquals("Login successful! Welcome back!", ERNESTPROG5121PART2.returnLoginStatus(true));
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        assertEquals("Username or password incorrect, please try again.", ERNESTPROG5121PART2.returnLoginStatus(false));
    }

    // ==================== Part 2: Sending Messages tests ====================

    // ---- checkMessageID ----

    @Test
    public void testCheckMessageID_ValidLength() {
        assertTrue(ERNESTPROG5121PART2.checkMessageID("0012345678")); // 10 chars
    }

    @Test
    public void testCheckMessageID_TooLong() {
        assertFalse(ERNESTPROG5121PART2.checkMessageID("00123456789")); // 11 chars
    }

    // ---- checkRecipientCell ----

    @Test
    public void testCheckRecipientCell_Success() {
        // Test Data: "+27718693002"
        String result = ERNESTPROG5121PART2.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testCheckRecipientCell_Failure() {
        // Test Data: "08575975089" (missing international code)
        String result = ERNESTPROG5121PART2.checkRecipientCell("08575975089");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    // ---- checkMessageLength ----

    @Test
    public void testCheckMessageLength_Success() {
        String msg = "Hi Mike, can you join me for dinner tonight?";
        assertEquals("Message ready to send.", ERNESTPROG5121PART2.checkMessageLength(msg));
    }

    @Test
    public void testCheckMessageLength_Failure() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 260) {
            sb.append("a");
        }
        String msg = sb.toString();
        int expectedOver = msg.length() - 250;
        String result = ERNESTPROG5121PART2.checkMessageLength(msg);
        assertEquals("Message exceeds 250 characters by " + expectedOver + "; please reduce the size.", result);
    }

    // ---- createMessageHash ----

    @Test
    public void testCreateMessageHash_MatchesBriefExample() {
        // Brief example: ID "0012345678", message number 0,
        // "Hi Mike, can you join me for dinner tonight?" -> "00:0:HITONIGHT"
        String hash = ERNESTPROG5121PART2.createMessageHash("0012345678", 0, "Hi Mike, can you join me for dinner tonight?");
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testCreateMessageHash_SecondExample() {
        // "Hi Keegan, did you receive the payment?" with ID "0112345678", number 1
        String hash = ERNESTPROG5121PART2.createMessageHash("0112345678", 1, "Hi Keegan, did you receive the payment?");
        assertEquals("01:1:HIPAYMENT", hash);
    }

    // ---- sentMessage ----

    @Test
    public void testSentMessage_Send() {
        String result = ERNESTPROG5121PART2.sentMessage(1, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        assertEquals("Message successfully sent.", result);
        assertEquals(1, ERNESTPROG5121PART2.returnTotalMessages());
    }

    @Test
    public void testSentMessage_Disregard() {
        String result = ERNESTPROG5121PART2.sentMessage(2, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        assertEquals("Press 0 to delete the message.", result);
        assertEquals(0, ERNESTPROG5121PART2.returnTotalMessages());
    }

    @Test
    public void testSentMessage_Store() {
        String result = ERNESTPROG5121PART2.sentMessage(3, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        assertEquals("Message successfully stored.", result);
        assertEquals(1, ERNESTPROG5121PART2.storedMessages.size());
        assertEquals(0, ERNESTPROG5121PART2.returnTotalMessages()); // not counted as "sent"
    }

    // ---- returnTotalMessages ----

    @Test
    public void testReturnTotalMessages_OnlyCountsSent() {
        ERNESTPROG5121PART2.sentMessage(1, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        ERNESTPROG5121PART2.sentMessage(2, "0112345678", "01:1:HIPAYMENT", "+27718693002", "Hi Keegan, did you receive the payment?");
        ERNESTPROG5121PART2.sentMessage(3, "0212345678", "02:2:HITEST", "+27718693002", "Hi this is a test");

        assertEquals(1, ERNESTPROG5121PART2.returnTotalMessages());
    }

    // ---- integration: full flow ----

    @Test
    public void testFullMessageFlow_SendAndPrint() {
        String id = "0012345678";
        String hash = ERNESTPROG5121PART2.createMessageHash(id, ERNESTPROG5121PART2.messageCounter, "Hi Mike, can you join me for dinner tonight?");
        String result = ERNESTPROG5121PART2.sentMessage(1, id, hash, "+27718693002", "Hi Mike, can you join me for dinner tonight?");

        assertEquals("Message successfully sent.", result);
        assertTrue(ERNESTPROG5121PART2.printMessages().contains("Hi Mike, can you join me for dinner tonight?"));
        assertTrue(ERNESTPROG5121PART2.printMessages().contains(hash));
    }
}