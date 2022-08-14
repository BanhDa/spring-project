package com.tuantv.security.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationKey {

    public static final String ROLES = "roles";

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String PREFERRED_USERNAME = "preferred_username";
    public static final String SUB = "sub";
    public static final String SESSION_STATE = "session_state";
    public static final String NAME = "name";
    public static final String CIF = "cif";
    public static final String AZP = "azp";
    public static final String USER_LEVEL = "user_level";
    public static final String BEARER = "Bearer ";
    public static final String BRANCH = "branch";

    public static final String EMAIL_VERIFIED = "email_verified";
    public static final String EMAIL = "email";
    public static final String FAMILY_NAME = "family_name";
    public static final String GIVEN_NAME = "given_name";


}
