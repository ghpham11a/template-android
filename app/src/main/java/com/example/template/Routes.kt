package com.example.template

import com.example.template.utils.Constants

sealed class Screen(val route: String) {
    object Features : Screen("features")
    object Profile : Screen("profile")
    object PublicProfile : Screen("public-profile/{userId}") {
        fun build(userId: String) = "public-profile/$userId"
    }
    object EditProfile : Screen("edit-profile")
    object FilterList : Screen("filter-list")
    object LoginAndSecurity : Screen("login_and_security")
    object ResetPassword : Screen("reset-password")
    object ResetPasswordSuccess : Screen("reset-password-success")
    object ThingIntro : Screen("thing-intro}")
    object Thing : Screen("thing/{thingId}") {
        fun build(thingId: String) = "thing/$thingId"
    }
    object ThingList : Screen("thing-list")
    object ThingBuilder: Screen("thing-builder/{thingId}?action={action}&mode={mode}&steps={steps}") {
        fun build(thingId: String, action: String, mode: String, steps: String) = "thing-builder/${thingId}?action=$action&mode=$mode&steps=$steps"
    }
    object AuthHub : Screen("auth-hub")
    object NewPassword : Screen("auth-hub/{username}/{code}") {
        fun build(username: String, code: String) = "auth-hub/$username/$code"
    }
    object CodeVerification : Screen("code-verification/{verificationType}/{username}/{password}") {
        fun build(verificationType: String, username: String, password: String) =
            "code-verification/$verificationType/$username/$password"
    }
    object EnterPassword : Screen("enter-password/{username}") {
        fun build(username: String) = "enter-password/$username"
    }
    object AddNewUserInfo : Screen("add-new-user-info/{username}/{phoneNumber}") {
        fun build(username: String = Constants.PATH_PLACEHOLDER, phoneNumber: String = Constants.PATH_PLACEHOLDER) = "add-new-user-info/$username/${phoneNumber}"
    }
    object Snag : Screen("snag")
    object PersonalInfo : Screen("personal-info")

    object PaymentsHub : Screen("payments-hub")

    object PaymentMethods : Screen("payments-hub/payment-methods")
    object YourPayments : Screen("payments-hub/your-payments")
    object PayoutMethods : Screen("payments-hub/payout-methods")
    object XMLView : Screen("xml-view")
    object Map : Screen("map")
    object AddPayout: Screen("add-payout")
    object AddBankInfo: Screen("add-bank-info/{country}") {
        fun build(country: String) = "add-bank-info/$country"
    }
    object Availability: Screen("availability")
    object TabbedList: Screen("tabbed-list")
    object SendPaymentHub: Screen("send-payment-hub")
    object PaymentAmount: Screen("payment-amount/{accountId}") {
        fun build(accountId: String) = "payment-amount/$accountId"
    }
    object ProxyCallHub: Screen("proxy-call-hub")

    object VideoCallHub: Screen("video-call-hub")
    object ChatHub: Screen("chat-hub")
}