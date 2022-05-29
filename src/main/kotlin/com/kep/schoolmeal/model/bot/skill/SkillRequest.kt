package com.kep.schoolmeal.model.bot.skill

data class SkillRequest(
    val action: Action,
    val bot: Bot,
    val contexts: List<RequestContext>,
    val intent: Intent,
    val userRequest: UserRequest
)

data class Action(
    val clientExtra: ClientExtra,
    val detailParams: DetailParams,
    val id: String,
    val name: String,
    val params: Params
)

data class Bot(
    val id: String,
    val name: String
)

data class Intent(
    val extra: Extra,
    val id: String,
    val name: String
)

data class UserRequest(
    val block: Block,
    val lang: String,
    val params: Params,
    val timezone: String,
    val user: User,
    val utterance: String
)


class ClientExtra (
    val type: String?,
    val value: String?
)

data class Parameter(
    val groupName: String,
    val origin: String,
    val value: String
)


class DetailParams (
    val sysdate: Parameter?
)

data class Extra(
    val reason: Reason
)

data class Reason(
    val code: Int,
    val message: String
)

data class Block(
    val id: String,
    val name: String
)

data class User(
    val id: String,
    val properties: Properties,
    val type: String
)

data class Properties(
    val appUserId: String?,
    val appUserStatus: String?,
    val app_user_id: String?,
    val app_user_status: String,
    val botUserKey: String,
    val bot_user_key: String,
    val isFriend: Boolean,
    val plusfriendUserKey: String,
    val plusfriend_user_key: String
)

data class RequestContext(
    val lifespan: Int,
    val name: String,
    val params: RequestContextParams,
    val ttl: Int
)

data class RequestContextParams(
    val key1: RequestContextParamsKey1
)

data class RequestContextParamsKey1(
    val resolvedValue: String,
    val value: String
)

data class ValidateRequest(
    val bot: Bot,
    val isInSlotFilling: Boolean,
    val lang: String,
    val params: Params,
    val timezone: String,
    val user: User,
    val utterance: String,
    val value: Value
)

data class Params(
    val surface: String
)

data class Value(
    val origin: String,
    val resolved: String
)

