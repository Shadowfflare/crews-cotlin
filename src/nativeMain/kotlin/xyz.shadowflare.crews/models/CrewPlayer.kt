package xyz.shadowflare.crews.models

class CrewPlayer(var javaUUID: String, var lastPlayerName: String) {
    var isBedrockPlayer = false
    var bedrockUUID: String? = null
    var pointBalance = 0
    var canChatSpy = false

}
