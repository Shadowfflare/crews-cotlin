package xyz.shadowflare.crews.models

import java.util.Date

class CrewInvite(@set:Deprecated var inviter: String, @set:Deprecated var invitee: String) {

    val inviteTime: Date

    init {
        inviteTime = Date()
    }

    fun getInviteTime(): Date {
        return inviteTime
    }
}
