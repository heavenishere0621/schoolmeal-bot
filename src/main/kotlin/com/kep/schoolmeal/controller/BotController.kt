package com.kep.schoolmeal.controller

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.*
import com.kep.schoolmeal.model.bot.skill.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.client.utils.URIBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/bot/")
class BotController(
) {
    protected var logger = LoggerFactory.getLogger(BotController::class.java)!!

    @PostMapping("/today")
    fun today(httpEntity: HttpEntity<String>): SkillResponse {
        val request = Gson().fromJson (httpEntity.body, SkillRequest::class.java)
        println(request.toString())

	val formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val text = getMealText(formatDate)

        val outputs: List<Output> = listOf(
            SimpleText(text)
        )

        return SkillResponse(
            template = Template( outputs, listOf(BlockQuickReply("다른날 메뉴 보기", "62932d167befc3101c3bfff7")))
        )
    }

    @PostMapping("/another")
    fun another(httpEntity: HttpEntity<String>): SkillResponse {
	println(httpEntity.body.toString())
        val request = Gson().fromJson (httpEntity.body, SkillRequest::class.java)

	println(request.action.detailParams.sysdate?.origin.toString())

        val formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
	val date = (request.action.detailParams.sysdate?.origin?:formatDate).replace("-", "")
	println(date)

        val text = getMealText(date)

        val outputs: List<Output> = listOf(
            SimpleText(text)
        )

        return SkillResponse(
            template = Template( outputs, listOf(BlockQuickReply("다른날 메뉴 보기", "62932d167befc3101c3bfff7")))
        )
    }


    @PostMapping("/test2")
    fun test2(httpEntity: HttpEntity<String>): SkillResponse {
        val request = Gson().fromJson (httpEntity.body, SkillRequest::class.java)
        println(request.toString())

       val text = getMealText("20220527")

        val outputs: List<Output> = listOf(
            SimpleText(text)
        )

        return SkillResponse(
            template = Template( outputs, listOf(BlockQuickReply("다른날 메뉴 보기", "62932d167befc3101c3bfff7")))
        )
    }

    private fun getMealText(date: String): String {
        val client = OkHttpClient()
        val builder = Request.Builder()
            .addHeader("Content-type", "application/json")

        val url = URIBuilder("https://open.neis.go.kr/hub/mealServiceDietInfo?ATPT_OFCDC_SC_CODE=J10&SD_SCHUL_CODE=7751057&MLSV_YMD=$date&Type=json")
            .build().toURL()

        val request =  builder.url(url)
            .get()
            .build()

        val status: Int
        val message: String?
        try {
            val response = client.newCall(request).execute()
            status = response.code()
            message = response.body()?.string()
            response.body()?.close()
            response.close()

            if (status != 200)
                throw java.lang.RuntimeException("status: $status")

            val element = JsonParser.parseString(message).asJsonObject
            val mealInfo = element.getAsJsonArray("mealServiceDietInfo").get(1)
            val row = mealInfo.asJsonObject.getAsJsonArray("row").get(0).asJsonObject
            val dish = row.get("DDISH_NM").asString

            return dish.replace("<br/>", "\n")

        } catch(e: Exception) {
            logger.error("[okhttp] newCall fail!!", e)

            return "NEIS 정보 조회 오류 입니다."
        }


    }

}
