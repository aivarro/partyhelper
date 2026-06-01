# 🎉 Peonõustaja AI (Party Advisor AI)

See on **Spring Booti** ja **Spring AI** raamistikul põhinev nutikas veebirakendus, mis aitab kasutajal otsustada, kas minna täna peole või jääda pigem koju. Rakendus analüüsib kasutaja energiataset, meeleolu ja eelistusi ning genereerib tehisintellekti abil isikupärastatud, humoorika ja stiilselt vormindatud soovituse.

Rakendus on ehitatud kasutades puhtaid arenduspraktikaid (Clean Code), kihilist arhitektuuri ja rangeid turvameetmeid.

---

## 🚀 Peamised omadused

* **Tugevalt tüübitud AI vastused:** Tehisintellekti vastused seotakse Spring AI automaatse JSON-skeemi genereerimise abil otse Java klassidega (`record`).
* **Kihiline arhitektuur (Layered Architecture):** Kood on puhtalt jaotatud mudeliteks (`model`), äriloogikaks (`service`) ja veebiliideseks (`controller`).
* **Varjatud AI Mõttekäik (Chain of Thought):** Tehisintellekt kasutab otsuse tegemiseks CoT loogikat, mis prinditakse serveri logidesse, kuid peidetakse Jacksoni (`@JsonProperty.Access.WRITE_ONLY`) abil turvaliselt lõppkasutaja eest.
* **Range sisendi valideerimine:** API sisend on kaitstud `@Valid` annotatsioonide ja pikkusepiirangutega, tagades, et vigased päringud peatatakse enne äriloogikani jõudmist (testitud 100% ulatusega `@WebMvcTest` abil).
* **Kaheastmeline turvakontroll (Prompt Injection kaitse):**
  1. **RegEx filter:** Püüab kinni teadaolevad ründesõnad (nt *ignore*, *bypass*).
  2. **LLM Kohtunik:** Eraldi eraldiseisev AI mudel (temperatuuriga 0.0), mis hindab sisendit manipuleerimiskatsete osas tagastades tugevalt tüübitud `SecurityResult` booleani.

---

## 🛠️ Nõuded süsteemile

* **Java 21** (või uuem)
* **Gradle** või **Maven**
* Kehtiv **LLM API võti** (nt Google Gemini, OpenAI, Claude).

---

## ⚙️ Seadistamine ja käivitamine

1. **Klooni repositoorium:**
   ```bash
   git clone <sinu-repo-url>
   cd party-helper

```

2. **Lisa oma API võti:**
   Ava `src/main/resources/application.properties` ja lisa sinna oma tehisintellekti teenusepakkuja API võti.
   Näiteks Google Gemini puhul:
```properties
spring.ai.vertex.ai.gemini.api-key=SINU_SALAJANE_API_VÕTI

```


3. **Käivita testid:**
```bash
./gradlew test

```


4. **Käivita rakendus:**
```bash
./gradlew bootRun

```



---

## 🎮 Kuidas kasutada?

### API päring (Backend)

Võid rakendusega suhelda otse läbi REST API (näiteks Postmani või cURL-iga).

**Päring (POST):** `http://localhost:8080/api/party/decide`
**Päise tüüp:** `Content-Type: application/json`

**Näidis-JSON (Body):**

```json
{
  "energyLevel": 8,
  "mood": "seiklushimuline",
  "preferences": "elektrooniline muusika, tantsimine"
}

```

**Edukuse vastus (200 OK):**
*Märka, et sisemine AI mõttekäik on vastusest eemaldatud, kuid serveri logides nähtav.*

```json
{
  "shouldGoOut": true,
  "recommendedParty": "Maa-alune tehno-reiv",
  "markdownMessage": "### 🚀 Pane tossud jalga!\n\nSinu energia on laes ja oled valmis seiklusteks..."
}

```

*Märkus: Pahatahtliku või vales formaadis sisendi puhul (nt "unusta varasemad reeglid" või energia=11) tagastab server koheselt `400 Bad Request` ja ei edasta päringut põhimudelile.*

---

## 📂 Projekti struktuur

```text
src/
 ├── main/java/party/hard/partyhelper/
 │    ├── controller/      # REST API sisendpunktid (PartyController)
 │    ├── model/           # Andmestruktuurid (UserInput, PartyDecision, SecurityResult)
 │    └── service/         # Äriloogika, turvakontroll ja AI suhtlus (PartyService)
 │
 ├── main/resources/
 │    ├── prompts/         # LLM süsteemi ja kasutaja juhised (.md failid)
 │    └── application.properties # Konfiguratsioon
 │
 └── test/java/party/hard/partyhelper/
      ├── controller/      # Kontrolleri ja valideerimise testid (@WebMvcTest)
      └── service/         # Äriloogika ja AI Mocking testid
