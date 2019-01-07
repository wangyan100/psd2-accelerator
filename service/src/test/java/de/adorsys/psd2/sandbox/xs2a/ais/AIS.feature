Feature: AIS

    ################################################################################################
    #                                                                                              #
    # Dedicated Consent Creation                                                                   #
    #                                                                                              #
    ################################################################################################
  Scenario Outline: Dedicated Consent Creation
    Given PSU created a consent on dedicated accounts for account information <accounts>, balances <balances> and transactions <transactions>
    When PSU accesses the consent data
    Then the appropriate data and response code <code> are received
    Examples:
      | accounts                                      | balances               | transactions           | code |
      | DE94500105178833114935                        | DE94500105178833114935 | DE94500105178833114935 | 200  |
      | DE94500105178833114935                        | null                   | DE94500105178833114935 | 200  |
      | null                                          | DE94500105178833114935 | null                   | 200  |
      | DE94500105178833114935;DE96500105179669622432 | DE94500105178833114935 | null                   | 200  |

    ################################################################################################
    #                                                                                              #
    # Consent Status                                                                               #
    #                                                                                              #
    ################################################################################################
  Scenario Outline: Consent Status Received
    Given PSU created a consent on dedicated accounts for account information <accounts>, balances <balances> and transactions <transactions>
    When PSU requests the consent status
    Then the appropriate status <status> and response code <code> are received
    Examples:
      | accounts               | balances               | transactions           | status   | code |
      | DE94500105178833114935 | DE94500105178833114935 | DE94500105178833114935 | received | 200  |

  Scenario Outline: Consent Status Valid
    Given PSU created a consent on dedicated accounts for account information <accounts>, balances <balances> and transactions <transactions>
    And PSU authorised the consent with psu-id <psu-id>, password <password>, sca-method <sca-method> and tan <tan>
    When PSU requests the consent status
    Then the appropriate status <status> and response code <code> are received
    Examples:
      | accounts               | balances               | transactions           | psu-id | password | sca-method | tan   | status | code |
      | DE94500105178833114935 | DE94500105178833114935 | DE94500105178833114935 | PSU-1  | 12345    | SMS_OTP    | 54321 | valid  | 200  |
