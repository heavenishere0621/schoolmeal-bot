package com.kep.schoolmeal.controller

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.*
import com.kep.schoolmeal.model.bot.skill.*

@RestController
@RequestMapping("/bot/")
class BotController(
) {
    protected var logger = LoggerFactory.getLogger(BotController::class.java)!!

    
   @PostMapping("/test2")
    fun test2(httpEntity: HttpEntity<String>): SkillResponse {
        val request = Gson().fromJson (httpEntity.body, SkillRequest::class.java)
        println(request.toString())

        val button: List<Button> = listOf(MessageButton("버틀라벨", "이거슨버튼을누른거임"))
        val outputs: List<Output> = listOf(
            BasicCard("this is simple", "desc", "image1", button),
            ListCard("listTitle", "listDesc",
                listOf(ListCardItem("listTitle", "listDesc", "image2", "button")),
                button
            )
        )

        return SkillResponse(
            template = Template( outputs, listOf(MessageQuickReply("처음으로", "처음으로")))
        )
    }

}