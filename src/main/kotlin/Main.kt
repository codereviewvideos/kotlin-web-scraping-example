import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jsoup.Jsoup

data class UserData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String
)

suspend fun main() {
    val url = "https://webscraper.io/test-sites/tables/tables-semantically-correct"

    HttpClient(CIO).use { client ->
        val response: HttpResponse = client.get(url)

        val html = response.bodyAsText()

        val document = Jsoup.parse(html)
        val table = document.select(".table-bordered").first()

        val userList = mutableListOf<UserData>()

        table?.select("tbody tr")?.forEach { row ->
            val cells = row.select("td")

            if (cells.size != 4) {
                return@forEach
            }

            val id = cells[0].text().toInt()
            val firstName = cells[1].text()
            val lastName = cells[2].text()
            val username = cells[3].text()

            userList.add(UserData(id, firstName, lastName, username))
        }

        for (user in userList) {
            println(user)
        }
    }
}
