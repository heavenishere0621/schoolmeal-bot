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
        //val request = Gson().fromJson (httpEntity.body, SkillRequest::class.java)

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
	    val request = Gson().fromJson (httpEntity.body, SkillRequest::class.java)
        val formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
	    val date = (request.action.detailParams.sysdate?.origin?:formatDate).replace("-", "")

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
        //println(request.toString())

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

            if (status != 200 || message == null) {
                throw java.lang.RuntimeException("status: $status")
            }

            if ("해당하는 데이터가 없습니다." in message) {
                return "해당하는 데이터가 없습니다."
            }

            val alg = "알레르기 유발식품 정보 표시\n1.난류 2.우유 3.메밀 4.땅콩 5.대두 6.밀 7.고등어 8.게 9.새우 10.돼지고기 11.복숭아 12.토마토 13.아황산류 14.호두 15.닭고기 16.쇠고기 17.오징어 18.조개류(굴, 전복, 홍합 포함) 19.잣"

            val element = JsonParser.parseString(message).asJsonObject
            val mealInfo = element.getAsJsonArray("mealServiceDietInfo").get(1)
            val row = mealInfo.asJsonObject.getAsJsonArray("row").get(0).asJsonObject
            val dish = row.get("DDISH_NM").asString.replace("<br/>", "\n")
            val cal =  row.get("CAL_INFO").asString.replace("<br/>", "\n")
            val NTR_INFO =  row.get("NTR_INFO").asString.replace("<br/>", "\n")

            return "[메뉴] \n$dish \n\n[칼로리] \n$cal \n\n [영양정보] \n$NTR_INFO\n\n$alg"



        } catch(e: Exception) {
            logger.error("[okhttp] newCall fail!!", e)

            return "NEIS 정보 조회 오류 입니다."
        }


    }

}
